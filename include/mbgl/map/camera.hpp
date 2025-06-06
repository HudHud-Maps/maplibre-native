#pragma once

#include <mbgl/util/chrono.hpp>
#include <mbgl/util/geo.hpp>
#include <mbgl/util/unitbezier.hpp>
#include <mbgl/util/vectors.hpp>

#include <optional>

#include <functional>

namespace mbgl {

/** Various options for describing the viewpoint of a map. All fields are
    optional.
    Anchor and center points are mutually exclusive, with preference for the
    center point when both are set.
    */
struct CameraOptions {
    CameraOptions& withCenter(const std::optional<LatLng>& o) {
        center = o;
        return *this;
    }
    CameraOptions& withPadding(const std::optional<EdgeInsets>& p) {
        padding = p;
        return *this;
    }
    CameraOptions& withAnchor(const std::optional<ScreenCoordinate>& o) {
        anchor = o;
        return *this;
    }
    CameraOptions& withZoom(const std::optional<double>& o) {
        zoom = o;
        return *this;
    }
    CameraOptions& withBearing(const std::optional<double>& o) {
        bearing = o;
        return *this;
    }
    CameraOptions& withPitch(const std::optional<double>& o) {
        pitch = o;
        return *this;
    }

    /** Coordinate at the center of the map. */
    std::optional<LatLng> center;

    /** Padding around the interior of the view that affects the frame of
        reference for `center`. */
    std::optional<EdgeInsets> padding;

    /** Point of reference for `zoom` and `angle`, assuming an origin at the
        top-left corner of the view. */
    std::optional<ScreenCoordinate> anchor;

    /** Zero-based zoom level. Constrained to the minimum and maximum zoom
        levels. */
    std::optional<double> zoom;

    /** Bearing, measured in degrees from true north. Wrapped to [0, 360). */
    std::optional<double> bearing;

    /** Pitch toward the horizon measured in degrees , with 0 deg resulting in a
        two-dimensional map. */
    std::optional<double> pitch;
};

constexpr bool operator==(const CameraOptions& a, const CameraOptions& b) {
    return a.center == b.center && a.padding == b.padding && a.anchor == b.anchor && a.zoom == b.zoom &&
           a.bearing == b.bearing && a.pitch == b.pitch;
}

constexpr bool operator!=(const CameraOptions& a, const CameraOptions& b) {
    return !(a == b);
}

struct PropertyAnimation;

/** Various options for describing a transition between viewpoints with
    animation. All fields are optional; the default values depend on how this
    struct is used. */
struct AnimationOptions {
    /** Time to animate to the viewpoint defined herein. */
    std::optional<Duration> duration;

    /** Average velocity of a flyTo() transition, measured in screenfuls per
        second, assuming a linear timing curve.

        A <i>screenful</i> is the visible span in pixels. It does not correspond
        to a fixed physical distance but rather varies by zoom level. */
    std::optional<double> velocity;

    /** Zero-based zoom level at the peak of the flyTo() transition’s flight
        path. */
    std::optional<double> minZoom;

    /** The easing timing curve of the transition. */
    std::optional<mbgl::util::UnitBezier> easing;

    /** A function that is called on each frame of the transition, just before a
        screen update, except on the last frame. The first parameter indicates
        the elapsed time as a percentage of the duration. */
    std::function<void(double)> transitionFrameFn;

    /** A function that is called once on the last frame of the transition, just
        before the corresponding screen update. */
    std::function<void()> transitionFinishFn;

    /** Creates an animation with no options specified. */
    AnimationOptions() = default;

    /** Creates an animation with the specified duration. */
    AnimationOptions(Duration d)
        : duration(d) {}
};

/** Various options for accessing physical properties of the underlying camera
   entity. A direct access to these properties allows more flexible and precise
   controlling of the camera while also being fully compatible and
   interchangeable with CameraOptions. All fields are optional. */
struct FreeCameraOptions {
    /** Position of the camera in slightly modified web mercator coordinates
        - The size of 1 unit is the width of the projected world instead of the
       "mercator meter". Coordinate [0, 0, 0] is the north-west corner and [1,
       1, 0] is the south-east corner.
        - Z coordinate is conformal and must respect minimum and maximum zoom
       values.
        - Zoom is automatically computed from the altitude (z)
    */
    std::optional<vec3> position = std::nullopt;

    /** Orientation of the camera represented as a unit quaternion [x, y, z, w].
        The default pose of the camera is such that the forward vector is
       looking up the -Z axis and the up vector is aligned with north
       orientation of the map: forward: [0, 0, -1] up:      [0, -1, 0] right [1,
       0, 0]

        Orientation can be set freely but certain constraints still apply
         - Orientation must be representable with only pitch and bearing.
         - Pitch has an upper limit */
    std::optional<vec4> orientation = std::nullopt;

    /** Helper function for setting the mercator position as Lat&Lng and altitude in meters */
    void setLocation(const LatLngAltitude& location) noexcept;

    /** Helper function for converting mercator position into Lat&Lng and
       altitude in meters. This function fails to return a value if `position`
       is invalid or is not set */
    std::optional<LatLngAltitude> getLocation() const noexcept;

    /** Helper function for setting orientation of the camera by defining a
       focus point on the map. Up vector is required in certain scenarios where
       bearing can't be deduced from the viewing direction */
    void lookAtPoint(const LatLng& location, const std::optional<vec3>& upVector = std::nullopt) noexcept;

    /** Helper function for setting the orientation of the camera as a pitch and
       a bearing. Both values are in degrees */
    void setPitchBearing(double pitch, double bearing) noexcept;
};

struct PropertyAnimation {
    TimePoint start;
    Duration duration;
    AnimationOptions animation;
    bool ran = false, finished = false, done = false;
    bool panning = false, scaling = false, rotating = false;

    PropertyAnimation(
        TimePoint start_, Duration duration_, AnimationOptions animation_, bool panning_, bool scaling_, bool rotating_)
        : start(start_),
          duration(duration_),
          animation(animation_),
          panning(panning_),
          scaling(scaling_),
          rotating(rotating_) {}

    double t(TimePoint now) {
        bool isAnimated = duration != Duration::zero();
        double t = isAnimated ? (std::chrono::duration<double>(now - start) / duration) : 1.0f;
        if (t >= 1.0) {
            return 1.0;
        }

        util::UnitBezier ease = animation.easing ? *animation.easing : util::DEFAULT_TRANSITION_EASE;
        return ease.solve(t, 0.001);
    }

    bool isAnimated() const { return duration != Duration::zero(); }
};

} // namespace mbgl
