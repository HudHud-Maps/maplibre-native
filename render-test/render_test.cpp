#include "allocation_index.hpp"

#include <mbgl/render_test.hpp>
#include <mbgl/storage/network_status.hpp>
#include <mbgl/util/io.hpp>
#include <mbgl/util/logging.hpp>
#include <mbgl/util/run_loop.hpp>

#include <args.hxx>

#include "file_source.hpp"
#include "manifest_parser.hpp"
#include "metadata.hpp"
#include "parser.hpp"
#include "runner.hpp"

#if MLN_RENDER_BACKEND_METAL
#include <mbgl/gfx/backend.hpp>
#endif // MLN_RENDER_BACKEND_METAL

#ifdef SHOW_ANSI_COLORS
#define ANSI_COLOR_RED "\x1b[31m"
#define ANSI_COLOR_GREEN "\x1b[32m"
#define ANSI_COLOR_YELLOW "\x1b[33m"
#define ANSI_COLOR_BLUE "\x1b[34m"
#define ANSI_COLOR_MAGENTA "\x1b[35m"
#define ANSI_COLOR_CYAN "\x1b[36m"
#define ANSI_COLOR_GRAY "\x1b[37m"
#define ANSI_COLOR_LIGHT_GRAY "\x1b[90m"
#define ANSI_COLOR_RESET "\x1b[0m"
#else
#define ANSI_COLOR_RED ""
#define ANSI_COLOR_GREEN ""
#define ANSI_COLOR_YELLOW ""
#define ANSI_COLOR_BLUE ""
#define ANSI_COLOR_MAGENTA ""
#define ANSI_COLOR_CYAN ""
#define ANSI_COLOR_GRAY ""
#define ANSI_COLOR_LIGHT_GRAY ""
#define ANSI_COLOR_RESET ""
#endif

#if !defined(SANITIZE)
void* operator new(std::size_t sz) {
    void* ptr = AllocationIndex::allocate(sz);
    if (!ptr) throw std::bad_alloc{};

    return ptr;
}

void operator delete(void* ptr) noexcept {
    AllocationIndex::deallocate(ptr);
}

void operator delete(void* ptr, size_t) noexcept {
    AllocationIndex::deallocate(ptr);
}
#endif

namespace {

using ArgumentsTuple = std::tuple<bool, bool, bool, uint32_t, std::string, TestRunner::UpdateResults, std::string>;
ArgumentsTuple parseArguments(int argc, char** argv) {
    const static std::unordered_map<std::string, TestRunner::UpdateResults> updateResultsFlags = {
        {"default", TestRunner::UpdateResults::DEFAULT},
        {"platform", TestRunner::UpdateResults::PLATFORM},
        {"metrics", TestRunner::UpdateResults::METRICS},
        {"rebaseline", TestRunner::UpdateResults::REBASELINE}};

    args::ArgumentParser argumentParser("MapLibre Native Test Runner");

    args::HelpFlag helpFlag(argumentParser, "help", "Display this help menu", {'h', "help"});

    args::Flag recycleMapFlag(argumentParser, "recycle map", "Toggle reusing the map object", {'r', "recycle-map"});
    args::Flag shuffleFlag(argumentParser, "shuffle", "Toggle shuffling the tests order", {'s', "shuffle"});
    args::Flag onlineFlag(
        argumentParser, "online", "Toggle online mode (by default tests will run offline)", {'o', "online"});
    args::ValueFlag<uint32_t> seedValue(argumentParser, "seed", "Shuffle seed (default: random)", {"seed"});
    args::ValueFlag<std::string> testPathValue(
        argumentParser, "manifestPath", "Test manifest file path", {'p', "manifestPath"}, args::Options::Required);
    args::ValueFlag<std::string> testFilterValue(argumentParser, "filter", "Test filter regex", {'f', "filter"});
    args::MapFlag<std::string, TestRunner::UpdateResults> testUpdateResultsValue(
        argumentParser,
        "update",
        "Test results update mode. Supported values are: \n\"default\" Updates generic render test expectations. \
                                                         \n\"platform\" Updates platform specific render test expectations. \
                                                         \n\"metrics\" Updates expected metrics for configuration defined by a manifest.\
                                                         \n\"rebaseline\" Updates or creates expected metrics for configuration defined by a manifest.",
        {'u', "update"},
        updateResultsFlags);

    try {
        argumentParser.ParseCLI(argc, argv);
    } catch (const args::Help&) {
        std::ostringstream stream;
        stream << argumentParser;
        mbgl::Log::Info(mbgl::Event::General, stream.str());
        exit(0);
    } catch (const args::ParseError& e) {
        std::ostringstream stream;
        stream << argumentParser;
        mbgl::Log::Info(mbgl::Event::General, stream.str());
        mbgl::Log::Error(mbgl::Event::General, e.what());
        exit(1);
    } catch (const args::ValidationError& e) {
        std::ostringstream stream;
        stream << argumentParser;
        mbgl::Log::Info(mbgl::Event::General, stream.str());
        mbgl::Log::Error(mbgl::Event::General, e.what());
        exit(2);
    }

    mbgl::filesystem::path manifestPath = args::get(testPathValue);
    if (!mbgl::filesystem::exists(manifestPath) || !manifestPath.has_filename()) {
        mbgl::Log::Error(mbgl::Event::General,
                         "Provided test manifest file path '" + manifestPath.string() + "' does not exist");
        exit(3);
    }

    auto testFilter = testFilterValue ? args::get(testFilterValue) : std::string{};
    const auto shuffle = shuffleFlag ? args::get(shuffleFlag) : false;
    const auto online = onlineFlag ? args::get(onlineFlag) : false;
    const auto seed = seedValue ? args::get(seedValue) : 1u;
    TestRunner::UpdateResults updateResults = testUpdateResultsValue ? args::get(testUpdateResultsValue)
                                                                     : TestRunner::UpdateResults::NO;
    return ArgumentsTuple{recycleMapFlag ? args::get(recycleMapFlag) : false,
                          shuffle,
                          online,
                          seed,
                          manifestPath.generic_string(),
                          updateResults,
                          std::move(testFilter)};
}
} // namespace
namespace mbgl {

int runRenderTests(int argc, char** argv, std::function<void()> testStatus) {
    int returnCode = 0;
    bool recycleMap;
    bool shuffle;
    bool online;
    uint32_t seed;
    std::string manifestPath;
    std::string testFilter;

    Log::useLogThread(false);
    TestRunner::UpdateResults updateResults;

    std::tie(recycleMap, shuffle, online, seed, manifestPath, updateResults, testFilter) = parseArguments(argc, argv);

    ProxyFileSource::setOffline(!online);

    auto manifestData = ManifestParser::parseManifest(manifestPath, testFilter);
    if (!manifestData) {
        exit(5);
    }
    mbgl::util::RunLoop runLoop;
    TestRunner runner(std::move(*manifestData), updateResults);
    if (shuffle) {
        printf(ANSI_COLOR_YELLOW "Shuffle seed: %d" ANSI_COLOR_RESET "\n", seed);
        runner.doShuffle(seed);
    }

    NetworkStatus::Set(online ? NetworkStatus::Status::Online : NetworkStatus::Status::Offline);

#if MLN_RENDER_BACKEND_METAL
    printf(ANSI_COLOR_YELLOW "Using GPU Expression Evaluation" ANSI_COLOR_RESET "\n");
    mbgl::gfx::Backend::setEnableGPUExpressionEval(true);
#endif // MLN_RENDER_BACKEND_METAL

    const auto& manifest = runner.getManifest();
    const auto& ignores = manifest.getIgnores();
    const auto& testPaths = manifest.getTestPaths();
    std::vector<TestMetadata> metadatas;
    metadatas.reserve(testPaths.size());

    TestStatistics stats;

    for (auto& testPath : testPaths) {
        TestMetadata metadata = parseTestMetadata(testPath);

        if (!recycleMap) {
            runner.reset();
        }

        std::string& id = metadata.id;
        std::string& status = metadata.status;
        std::string& color = metadata.color;

        const std::string::size_type rootLength = manifest.getTestRootPath().length();
        id = testPath.defaultExpectations();
        id = id.substr(rootLength + 1, id.length() - rootLength - 2);

        bool shouldIgnore = false;
        std::string ignoreReason;

        const mbgl::filesystem::path ignoreName(id);
        const auto it = std::find_if(
            ignores.cbegin(), ignores.cend(), [&ignoreName](auto pair) { return pair.first == ignoreName; });
        if (it != ignores.end()) {
            metadata.ignoredTest = shouldIgnore = true;
            ignoreReason = it->second;
            if (ignoreReason.rfind("skip", 0) == 0) {
                printf(ANSI_COLOR_GRAY "* skipped %s (%s)" ANSI_COLOR_RESET "\n", id.c_str(), ignoreReason.c_str());
                mbgl::Log::Info(mbgl::Event::General, "* skipped " + id + "(" + ignoreReason + ")");
                continue;
            }
        }

        if (metadata.document.ObjectEmpty()) {
            metadata.metricsErrored++;
            metadata.renderErrored++;
        } else {
            runner.run(metadata);
        }

        bool errored = metadata.metricsErrored || metadata.renderErrored || metadata.labelCutOffFound;
        bool passed = !errored && !metadata.metricsFailed && !metadata.renderFailed;

        if (shouldIgnore) {
            if (passed) {
                status = "ignored passed";
                color = "#E8A408";
                stats.ignorePassedTests++;
                printf(ANSI_COLOR_YELLOW "* ignore %s (%s)" ANSI_COLOR_RESET "\n", id.c_str(), ignoreReason.c_str());
                mbgl::Log::Info(mbgl::Event::General, "* ignore " + id + " (" + ignoreReason + ")");
            } else {
                status = "ignored failed";
                color = "#9E9E9E";
                stats.ignoreFailedTests++;
                printf(
                    ANSI_COLOR_LIGHT_GRAY "* ignore %s (%s)" ANSI_COLOR_RESET "\n", id.c_str(), ignoreReason.c_str());
                mbgl::Log::Info(mbgl::Event::General, "* ignore " + id + " (" + ignoreReason + ")");
            }
        } else {
            // Only fail the bots on render errors, this is a CI limitation that
            // we need to succeed on metrics failed so the rebaseline bot can
            // run next in the pipeline and collect the new baselines. The
            // rebaseline bot will ultimately report the error and block the
            // patch from being merged.
            if (metadata.renderErrored || metadata.renderFailed) {
                returnCode = 1;
            }

            if (passed) {
                status = "passed";
                color = "green";
                stats.passedTests++;
                printf(ANSI_COLOR_GREEN "* passed %s" ANSI_COLOR_RESET "\n", id.c_str());
                mbgl::Log::Info(mbgl::Event::General, "* passed " + id);
            } else if (errored) {
                status = "errored";
                color = "red";
                stats.erroredTests++;
                returnCode = 2;
                printf(ANSI_COLOR_RED "* errored %s" ANSI_COLOR_RESET "\n", id.c_str());
                printf(ANSI_COLOR_RED "* error: %s" ANSI_COLOR_RESET "\n", metadata.errorMessage.c_str());
                mbgl::Log::Info(mbgl::Event::General, "* errored " + id);
                mbgl::Log::Info(mbgl::Event::General, "* error " + metadata.errorMessage);
            } else {
                status = "failed";
                color = "red";
                stats.failedTests++;
                returnCode = 3;
                printf(ANSI_COLOR_RED "* failed %s" ANSI_COLOR_RESET "\n", id.c_str());
                mbgl::Log::Info(mbgl::Event::General, "* failed " + id);
            }
        }

        metadatas.push_back(std::move(metadata));
        if (testStatus) {
            testStatus();
        }
    }

    const std::string manifestName = mbgl::filesystem::path(manifestPath).stem().generic_string();
    const std::string resultPath = manifest.getResultPath() + "/" + manifestName + ".html";
    std::string resultsHTML = createResultPage(stats, metadatas, shuffle, seed);
    mbgl::util::write_file(resultPath, resultsHTML);

    const uint32_t count = stats.erroredTests + stats.failedTests + stats.ignoreFailedTests + stats.ignorePassedTests +
                           stats.passedTests;

    if (stats.passedTests) {
        printf(ANSI_COLOR_GREEN "%u passed (%.1lf%%)" ANSI_COLOR_RESET "\n",
               stats.passedTests,
               100.0 * stats.passedTests / count);
        mbgl::Log::Info(mbgl::Event::General, std::to_string(stats.passedTests) + " passed tests");
    }
    if (stats.ignorePassedTests) {
        printf(ANSI_COLOR_YELLOW "%u passed but were ignored (%.1lf%%)" ANSI_COLOR_RESET "\n",
               stats.ignorePassedTests,
               100.0 * stats.ignorePassedTests / count);
        mbgl::Log::Info(mbgl::Event::General,
                        std::to_string(stats.ignorePassedTests) + " passed tests but were ignored");
    }
    if (stats.ignoreFailedTests) {
        printf(ANSI_COLOR_LIGHT_GRAY "%u ignored (%.1lf%%)" ANSI_COLOR_RESET "\n",
               stats.ignoreFailedTests,
               100.0 * stats.ignoreFailedTests / count);
        mbgl::Log::Info(mbgl::Event::General, std::to_string(stats.ignoreFailedTests) + " ignored tests");
    }
    if (stats.failedTests) {
        printf(ANSI_COLOR_RED "%u failed (%.1lf%%)" ANSI_COLOR_RESET "\n",
               stats.failedTests,
               100.0 * stats.failedTests / count);
        mbgl::Log::Info(mbgl::Event::General, std::to_string(stats.failedTests) + " failed tests");
    }
    if (stats.erroredTests) {
        printf(ANSI_COLOR_RED "%u errored (%.1lf%%)" ANSI_COLOR_RESET "\n",
               stats.erroredTests,
               100.0 * stats.erroredTests / count);
        mbgl::Log::Info(mbgl::Event::General, std::to_string(stats.erroredTests) + " errored tests");
    }

    printf("Results at: %s\n", mbgl::filesystem::canonical(resultPath).generic_string().c_str());

    return returnCode;
}

} // namespace mbgl
