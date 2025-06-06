#pragma once

#include <mbgl/platform/time.hpp>

#include <chrono>
#include <limits>
#include <string>
#include <type_traits>

namespace mbgl {

using Clock = std::chrono::steady_clock;

using Seconds = std::chrono::seconds;
using Milliseconds = std::chrono::milliseconds;

using TimePoint = Clock::time_point;
using Duration = Clock::duration;

// Used to measure second-precision times, such as times gathered from HTTP responses.
using Timestamp = std::chrono::time_point<std::chrono::system_clock, Seconds>;

namespace util {

inline Timestamp now() {
    return platform::now();
}

// Returns the RFC1123 formatted date. E.g. "Tue, 04 Nov 2014 02:13:24 GMT"
std::string rfc1123(Timestamp);

// YYYY-mm-dd HH:MM:SS e.g. "2015-11-26 16:11:23"
std::string iso8601(Timestamp);

// YYYY-mm-ddTHH:MM:SS.MS e.g. "2015-11-26T16:11:23.324Z"
std::string iso8601(std::chrono::time_point<std::chrono::system_clock, Milliseconds>);

Timestamp parseTimestamp(const char *);

Timestamp parseTimestamp(int32_t timestamp);

// C++17 polyfill
#if defined(_MSC_VER) && !defined(__clang__)
template <class _Rep, class _Period, std::enable_if_t<std::numeric_limits<_Rep>::is_signed, int> = 0>
_NODISCARD constexpr std::chrono::duration<_Rep, _Period> abs(const std::chrono::duration<_Rep, _Period> _Dur) noexcept(
    std::is_arithmetic_v<_Rep>) /* strengthened */ {
    // create a duration with count() the absolute value of _Dur.count()
    return _Dur < std::chrono::duration<_Rep, _Period>::zero() ? std::chrono::duration<_Rep, _Period>::zero() - _Dur
                                                               : _Dur;
}
#else
template <class Rep, class Period>
constexpr std::chrono::duration<Rep, Period> abs(std::chrono::duration<Rep, Period> d)
    requires(std::chrono::duration<Rep, Period>::min() < std::chrono::duration<Rep, Period>::zero())
{
    return d >= d.zero() ? d : -d;
}
#endif

} // namespace util

} // namespace mbgl
