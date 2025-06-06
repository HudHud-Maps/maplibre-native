#pragma once

#include <mbgl/map/camera.hpp>
#include <mbgl/map/projection_mode.hpp>
#include <mbgl/map/map_observer.hpp>
#include <mbgl/map/mode.hpp>
#include <mbgl/map/transform_state.hpp>
#include <mbgl/util/chrono.hpp>
#include <mbgl/util/constants.hpp>
#include <mbgl/util/geo.hpp>
#include <mbgl/util/noncopyable.hpp>

#include <cstdint>
#include <cmath>
#include <functional>
#include <optional>

namespace mbgl {

class TransformObserver {
public:
    virtual ~TransformObserver() = default;

    static TransformObserver& nullObserver() {
        static TransformObserver observer;
        return observer;
    }

    virtual void onCameraWillChange(MapObserver::CameraChangeMode) {}
    virtual void onCameraIsChanging() {}
    virtual void onCameraDidChange(MapObserver::CameraChangeMode) {}
};

double normalizeAngle(double, double);

class Transform {
public:
    Transform(TransformObserver& = TransformObserver::nullObserver(),
              ConstrainMode = ConstrainMode::HeightOnly,
              ViewportMode = ViewportMode::Default);

    Transform(const TransformState& state_)
        : observer(TransformObserver::nullObserver()),
          state(state_) {}

    virtual ~Transform() = default;

    // Map view
    void resize(Size size);

    // Camera
    /** Returns the current camera options. */
    CameraOptions getCameraOptions(const std::optional<EdgeInsets>&) const;

    /** Instantaneously, synchronously applies the given camera options. */
    void jumpTo(const CameraOptions&);
    /** Asynchronously transitions all specified camera options linearly along
        an optional time curve. However, center coordinate is not transitioned
        linearly as, instead, ground speed is kept linear.*/
    virtual void easeTo(const CameraOptions&, const AnimationOptions& = {});
    /** Asynchronously zooms out, pans, and zooms back into the given camera
        along a great circle, as though the viewer is riding a supersonic
        jetcopter.
        Parameter linearZoomInterpolation: when true, there is no additional
        zooming out as zoom is linearly interpolated from current to given
        camera zoom. This is used for easeTo.*/
    virtual void flyTo(const CameraOptions&, const AnimationOptions& = {}, bool linearZoomInterpolation = false);

    // Position

    /** Pans the map by the given amount.
        @param offset The distance to pan the map by, measured in pixels from
            top to bottom and from left to right. */
    void moveBy(const ScreenCoordinate& offset, const AnimationOptions& = {});
    LatLng getLatLng(LatLng::WrapMode = LatLng::Wrapped) const;

    // Bounds

    void setLatLngBounds(LatLngBounds);
    void setMinZoom(double);
    void setMaxZoom(double);

    void setMinPitch(double);
    void setMaxPitch(double);

    // Zoom

    /** Returns the zoom level. */
    double getZoom() const;

    // Bearing

    void rotateBy(const ScreenCoordinate& first, const ScreenCoordinate& second, const AnimationOptions& = {});
    double getBearing() const;

    // Pitch

    double getPitch() const;

    // North Orientation
    void setNorthOrientation(NorthOrientation);
    NorthOrientation getNorthOrientation() const;

    // Constrain mode
    void setConstrainMode(ConstrainMode);
    ConstrainMode getConstrainMode() const;

    // Viewport mode
    void setViewportMode(ViewportMode);
    ViewportMode getViewportMode() const;

    // Projection mode
    void setProjectionMode(const ProjectionMode&);
    ProjectionMode getProjectionMode() const;

    // Transitions
    virtual bool inTransition() const;
    virtual void updateTransitions(const TimePoint& now);
    TimePoint getTransitionStart() const { return transitionStart; }
    Duration getTransitionDuration() const { return transitionDuration; }
    virtual void cancelTransitions();

    // Gesture
    void setGestureInProgress(bool);
    bool isGestureInProgress() const { return state.isGestureInProgress(); }

    // Transform state
    const TransformState& getState() const { return state; }
    bool isRotating() const { return state.isRotating(); }
    bool isScaling() const { return state.isScaling(); }
    bool isPanning() const { return state.isPanning(); }

    // Conversion and projection
    ScreenCoordinate latLngToScreenCoordinate(const LatLng&) const;
    LatLng screenCoordinateToLatLng(const ScreenCoordinate&, LatLng::WrapMode = LatLng::Wrapped) const;

    FreeCameraOptions getFreeCameraOptions() const;
    void setFreeCameraOptions(const FreeCameraOptions& options);

protected:
    TransformObserver& observer;
    TransformState state;

    virtual void startTransition(const CameraOptions&,
                                 const AnimationOptions&,
                                 const std::function<void(double)>&,
                                 const Duration&);

    // We don't want to show horizon: limit max pitch based on edge insets.
    double getMaxPitchForEdgeInsets(const EdgeInsets& insets) const;

    TimePoint transitionStart;
    Duration transitionDuration;
    std::function<bool(const TimePoint)> transitionFrameFn;
    std::function<void()> transitionFinishFn;
};

} // namespace mbgl
