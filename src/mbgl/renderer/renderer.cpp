#include <mbgl/renderer/renderer.hpp>

#include <mbgl/annotation/annotation_manager.hpp>
#include <mbgl/gfx/backend_scope.hpp>
#include <mbgl/gfx/dynamic_texture_atlas.hpp>
#include <mbgl/gfx/renderer_backend.hpp>
#include <mbgl/layermanager/layer_manager.hpp>
#include <mbgl/renderer/renderer_impl.hpp>
#include <mbgl/renderer/render_static_data.hpp>
#include <mbgl/renderer/render_tree.hpp>
#include <mbgl/renderer/update_parameters.hpp>
#include <mbgl/util/instrumentation.hpp>

namespace mbgl {

Renderer::Renderer(gfx::RendererBackend& backend, float pixelRatio_, const std::optional<std::string>& localFontFamily_)
    : impl(std::make_unique<Impl>(backend, pixelRatio_, localFontFamily_)) {}

Renderer::~Renderer() {
    gfx::BackendScope guard{impl->backend};
    impl.reset();
}

void Renderer::markContextLost() {
    impl->orchestrator.markContextLost();
}

void Renderer::setObserver(RendererObserver* observer) {
    impl->setObserver(observer);
    impl->orchestrator.setObserver(observer);
}

void Renderer::render(const std::shared_ptr<UpdateParameters>& updateParameters) {
    MLN_TRACE_FUNC();
    assert(updateParameters);
    const bool styleChanged = impl->styleLoaded && !updateParameters->styleLoaded;
    impl->styleLoaded = updateParameters->styleLoaded;
    if (!impl->dynamicTextureAtlas || styleChanged) {
        auto& context = impl->backend.getContext();
        impl->dynamicTextureAtlas = std::make_unique<gfx::DynamicTextureAtlas>(context);
    }
    if (auto renderTree = impl->orchestrator.createRenderTree(updateParameters, impl->dynamicTextureAtlas)) {
        renderTree->prepare();
        impl->render(*renderTree, updateParameters);
    }
}

std::vector<Feature> Renderer::queryRenderedFeatures(const ScreenLineString& geometry,
                                                     const RenderedQueryOptions& options) const {
    return impl->orchestrator.queryRenderedFeatures(geometry, options);
}

std::vector<Feature> Renderer::queryRenderedFeatures(const ScreenCoordinate& point,
                                                     const RenderedQueryOptions& options) const {
    return impl->orchestrator.queryRenderedFeatures({point}, options);
}

std::vector<Feature> Renderer::queryRenderedFeatures(const ScreenBox& box, const RenderedQueryOptions& options) const {
    return impl->orchestrator.queryRenderedFeatures(
        {box.min, {box.max.x, box.min.y}, box.max, {box.min.x, box.max.y}, box.min}, options);
}

AnnotationIDs Renderer::queryPointAnnotations(const ScreenBox& box) const {
    if (!LayerManager::annotationsEnabled) {
        return {};
    }
    RenderedQueryOptions options;
    options.layerIDs = {{AnnotationManager::PointLayerID}};
    auto features = queryRenderedFeatures(box, options);
    return getAnnotationIDs(features);
}

AnnotationIDs Renderer::queryShapeAnnotations(const ScreenBox& box) const {
    if (!LayerManager::annotationsEnabled) {
        return {};
    }
    auto features = impl->orchestrator.queryShapeAnnotations(
        {box.min, {box.max.x, box.min.y}, box.max, {box.min.x, box.max.y}, box.min});
    return getAnnotationIDs(features);
}

AnnotationIDs Renderer::getAnnotationIDs(const std::vector<Feature>& features) const {
    if (!LayerManager::annotationsEnabled) {
        return {};
    }
    std::set<AnnotationID> set;
    for (auto& feature : features) {
        assert(feature.id.is<uint64_t>());
        assert(feature.id.get<uint64_t>() <= std::numeric_limits<AnnotationID>::max());
        set.insert(static_cast<AnnotationID>(feature.id.get<uint64_t>()));
    }
    AnnotationIDs ids;
    ids.reserve(set.size());
    std::move(set.begin(), set.end(), std::back_inserter(ids));
    return ids;
}

std::vector<Feature> Renderer::querySourceFeatures(const std::string& sourceID,
                                                   const SourceQueryOptions& options) const {
    return impl->orchestrator.querySourceFeatures(sourceID, options);
}

FeatureExtensionValue Renderer::queryFeatureExtensions(const std::string& sourceID,
                                                       const Feature& feature,
                                                       const std::string& extension,
                                                       const std::string& extensionField,
                                                       const std::optional<std::map<std::string, Value>>& args) const {
    return impl->orchestrator.queryFeatureExtensions(sourceID, feature, extension, extensionField, args);
}

void Renderer::setFeatureState(const std::string& sourceID,
                               const std::optional<std::string>& sourceLayerID,
                               const std::string& featureID,
                               const FeatureState& state) {
    impl->orchestrator.setFeatureState(sourceID, sourceLayerID, featureID, state);
}

void Renderer::getFeatureState(FeatureState& state,
                               const std::string& sourceID,
                               const std::optional<std::string>& sourceLayerID,
                               const std::string& featureID) const {
    impl->orchestrator.getFeatureState(state, sourceID, sourceLayerID, featureID);
}

void Renderer::removeFeatureState(const std::string& sourceID,
                                  const std::optional<std::string>& sourceLayerID,
                                  const std::optional<std::string>& featureID,
                                  const std::optional<std::string>& stateKey) {
    impl->orchestrator.removeFeatureState(sourceID, sourceLayerID, featureID, stateKey);
}

void Renderer::dumpDebugLogs() {
    impl->orchestrator.dumpDebugLogs();
}

void Renderer::collectPlacedSymbolData(bool enable) {
    impl->orchestrator.collectPlacedSymbolData(enable);
}

const std::vector<PlacedSymbolData>& Renderer::getPlacedSymbolsData() const {
    return impl->orchestrator.getPlacedSymbolsData();
}

void Renderer::setTileCacheEnabled(bool enable) {
    impl->orchestrator.setTileCacheEnabled(enable);
}

bool Renderer::getTileCacheEnabled() const {
    return impl->orchestrator.getTileCacheEnabled();
}

void Renderer::reduceMemoryUse() {
    gfx::BackendScope guard{impl->backend};
    impl->reduceMemoryUse();
    impl->orchestrator.reduceMemoryUse();
}

void Renderer::clearData() {
    impl->orchestrator.clearData();
}

#if MLN_RENDER_BACKEND_OPENGL
void Renderer::enableAndroidEmulatorGoldfishMitigation(bool enable) {
    impl->orchestrator.enableAndroidEmulatorGoldfishMitigation(enable);
}
#endif

} // namespace mbgl
