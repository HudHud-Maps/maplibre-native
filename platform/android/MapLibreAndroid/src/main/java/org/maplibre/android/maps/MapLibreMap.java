package org.maplibre.android.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.UiThread;

import org.maplibre.android.gestures.AndroidGesturesManager;
import org.maplibre.android.gestures.MoveGestureDetector;
import org.maplibre.android.gestures.RotateGestureDetector;
import org.maplibre.android.gestures.ShoveGestureDetector;
import org.maplibre.android.gestures.StandardScaleGestureDetector;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.Geometry;
import org.maplibre.android.MapStrictMode;
import org.maplibre.android.annotations.Annotation;
import org.maplibre.android.annotations.BaseMarkerOptions;
import org.maplibre.android.annotations.Marker;
import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.annotations.Polygon;
import org.maplibre.android.annotations.PolygonOptions;
import org.maplibre.android.annotations.Polyline;
import org.maplibre.android.annotations.PolylineOptions;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdate;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.constants.MapLibreConstants;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.geometry.LatLngBounds;
import org.maplibre.android.location.LocationComponent;
import org.maplibre.android.location.LocationComponentActivationOptions;
import org.maplibre.android.log.Logger;
import org.maplibre.android.offline.OfflineRegionDefinition;
import org.maplibre.android.style.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * The general class to interact with in the Android MapLibre SDK. It exposes the entry point for all
 * methods related to the MapView. You cannot instantiate {@link MapLibreMap} object directly, rather,
 * you must obtain one from the getMapAsync() method on a MapFragment or MapView that you have
 * added to your application.
 * <p>
 * Note: Similar to a View object, a MapLibreMap should only be read and modified from the main thread.
 * </p>
 */
@UiThread
public final class MapLibreMap {

  private static final String TAG = "Mbgl-MapLibreMap";

  private final NativeMap nativeMapView;
  private final UiSettings uiSettings;
  private final Projection projection;
  private final Transform transform;
  private final CameraChangeDispatcher cameraChangeDispatcher;
  private final OnGesturesManagerInteractionListener onGesturesManagerInteractionListener;
  private final List<Style.OnStyleLoaded> awaitingStyleGetters = new ArrayList<>();
  private final List<OnDeveloperAnimationListener> developerAnimationStartedListeners;

  @Nullable
  private Style.OnStyleLoaded styleLoadedCallback;

  private LocationComponent locationComponent;
  private AnnotationManager annotationManager;

  @Nullable
  private MapLibreMap.OnFpsChangedListener onFpsChangedListener;

  @Nullable
  private Style style;

  private boolean debugActive;
  private boolean started;

  MapLibreMap(NativeMap map, Transform transform, UiSettings ui, Projection projection,
              OnGesturesManagerInteractionListener listener, CameraChangeDispatcher cameraChangeDispatcher,
              List<OnDeveloperAnimationListener> developerAnimationStartedListeners) {
    this.nativeMapView = map;
    this.uiSettings = ui;
    this.projection = projection;
    this.transform = transform;
    this.onGesturesManagerInteractionListener = listener;
    this.cameraChangeDispatcher = cameraChangeDispatcher;
    this.developerAnimationStartedListeners = developerAnimationStartedListeners;
  }

  /**
   * Trigger the mapview to repaint.
   */
  public void triggerRepaint() {
    nativeMapView.triggerRepaint();
  }

  /**
   * Query rendering statistics overlay status.
   */
  public boolean isRenderingStatsViewEnabled() {
    return nativeMapView.isRenderingStatsViewEnabled();
  }

  /**
   * Enable rendering statistics overlay with {@link RenderingStats} values.
   */
  public void enableRenderingStatsView(boolean value) {
    nativeMapView.enableRenderingStatsView(value);
  }

  public void toggleTransform() {
    nativeMapView.toggleTransform();
  }

  public void setSwapBehaviorFlush(boolean flush) {
    nativeMapView.setSwapBehaviorFlush(flush);
  }

  void initialise(@NonNull Context context, @NonNull MapLibreMapOptions options) {
    transform.initialise(this, options);
    uiSettings.initialise(context, options);

    // Map configuration
    setDebugActive(options.getDebugActive());
    setApiBaseUrl(options);
    setPrefetchesTiles(options);
  }

  /**
   * Get the Style of the map asynchronously.
   */
  public void getStyle(@NonNull Style.OnStyleLoaded onStyleLoaded) {
    if (style != null && style.isFullyLoaded()) {
      onStyleLoaded.onStyleLoaded(style);
    } else {
      awaitingStyleGetters.add(onStyleLoaded);
    }
  }

  /**
   * Get the Style of the map.
   * <p>
   * Returns null when style is being loaded.
   * </p>
   *
   * @return the style of the map
   */
  @Nullable
  public Style getStyle() {
    if (style == null || !style.isFullyLoaded()) {
      return null;
    } else {
      return style;
    }
  }

  /**
   * Called when the hosting Activity/Fragment onStart() method is called.
   */
  void onStart() {
    started = true;
    locationComponent.onStart();
  }

  /**
   * Called when the hosting Activity/Fragment onStop() method is called.
   */
  void onStop() {
    started = false;
    locationComponent.onStop();
  }

  /**
   * Called when the hosting Activity/Fragment is going to be destroyed and map state needs to be saved.
   *
   * @param outState the bundle to save the state to.
   */
  void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putParcelable(MapLibreConstants.STATE_CAMERA_POSITION, transform.getCameraPosition());
    outState.putBoolean(MapLibreConstants.STATE_DEBUG_ACTIVE, isDebugActive());
    uiSettings.onSaveInstanceState(outState);
  }

  /**
   * Called when the hosting Activity/Fragment is recreated and map state needs to be restored.
   *
   * @param savedInstanceState the bundle containing the saved state
   */
  void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    final CameraPosition cameraPosition = savedInstanceState.getParcelable(MapLibreConstants.STATE_CAMERA_POSITION);

    uiSettings.onRestoreInstanceState(savedInstanceState);

    if (cameraPosition != null) {
      moveCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder(cameraPosition).build())
      );
    }

    nativeMapView.setDebug(savedInstanceState.getBoolean(MapLibreConstants.STATE_DEBUG_ACTIVE));
  }

  /**
   * Called when the hosting Activity/Fragment onDestroy()/onDestroyView() method is called.
   */
  void onDestroy() {
    locationComponent.onDestroy();
    if (style != null) {
      style.clear();
    }
    cameraChangeDispatcher.onDestroy();
  }

  /**
   * Called before the OnMapReadyCallback is invoked.
   */
  void onPreMapReady() {
    transform.invalidateCameraPosition();
    annotationManager.reloadMarkers();
    annotationManager.adjustTopOffsetPixels(this);
  }

  /**
   * Called when the OnMapReadyCallback has finished executing.
   * <p>
   * Invalidation of the camera position is required to update the added components in
   * OnMapReadyCallback with the correct transformation.
   * </p>
   */
  void onPostMapReady() {
    transform.invalidateCameraPosition();
  }

  /**
   * Called when the map finished loading a style.
   */
  void onFinishLoadingStyle() {
    notifyStyleLoaded();
  }

  /**
   * Called when the map failed loading a style.
   */
  void onFailLoadingStyle() {
    styleLoadedCallback = null;
  }

  /**
   * Called when the region is changing or has changed.
   */
  void onUpdateRegionChange() {
    annotationManager.update();
  }

  /**
   * Called when the map frame is fully rendered.
   */
  void onUpdateFullyRendered() {
    CameraPosition cameraPosition = transform.invalidateCameraPosition();
    if (cameraPosition != null) {
      uiSettings.update(cameraPosition);
    }
  }

  /**
   * Experimental feature. Do not use.
   */
  long getNativeMapPtr() {
    return nativeMapView.getNativePtr();
  }

  // Style

  /**
   * Sets tile pre-fetching zoom delta from MapboxOptions.
   *
   * @param options the options object
   */
  private void setPrefetchesTiles(@NonNull MapLibreMapOptions options) {
    if (!options.getPrefetchesTiles()) {
      setPrefetchZoomDelta(0);
    } else {
      setPrefetchZoomDelta(options.getPrefetchZoomDelta());
    }
  }

  /**
   * Enable or disable tile pre-fetching. Pre-fetching makes sure that a low-resolution
   * tile is rendered as soon as possible at the expense of a little bandwidth.
   *
   * @param enable true to enable
   * @deprecated Use {@link #setPrefetchZoomDelta(int)} instead.
   */
  @Deprecated
  public void setPrefetchesTiles(boolean enable) {
    nativeMapView.setPrefetchTiles(enable);
  }

  /**
   * Check whether tile pre-fetching is enabled or not.
   *
   * @return true if enabled
   * @see MapLibreMap#setPrefetchesTiles(boolean)
   * @deprecated Use {@link #getPrefetchZoomDelta()} instead.
   */
  @Deprecated
  public boolean getPrefetchesTiles() {
    return nativeMapView.getPrefetchTiles();
  }

  /**
   * Set the tile pre-fetching zoom delta. Pre-fetching makes sure that a low-resolution
   * tile at the (current_zoom_level - delta) is rendered as soon as possible at the
   * expense of a little bandwidth.
   * Note: This operation will override the MapLibreMapOptions#setPrefetchesTiles(boolean)
   *       Setting zoom delta to 0 will disable pre-fetching.
   * Default zoom delta is 4.
   *
   * @param delta zoom delta
   */
  public void setPrefetchZoomDelta(@IntRange(from = 0) int delta) {
    nativeMapView.setPrefetchZoomDelta(delta);
  }

  /**
   * Check current pre-fetching zoom delta.
   *
   * @return current zoom delta.
   * @see MapLibreMap#setPrefetchZoomDelta(int)
   */
  @IntRange(from = 0)
  public int getPrefetchZoomDelta() {
    return nativeMapView.getPrefetchZoomDelta();
  }

  /**
   * Indicating whether the map may cache tiles for different zoom levels or not.
   *
   * @param enabled true causes the map view to consume more memory and have a smoother user
   * experience when zoom in/out. The default value of this property is `true`.
   */
  public void setTileCacheEnabled(boolean enabled) {
    nativeMapView.setTileCacheEnabled(enabled);
  }

  /**
   * Check whether tile cache is enabled or not.
   *
   * @return true if enabled
   * @see MapLibreMap#setTileCacheEnabled(boolean)
   */
  public boolean getTileCacheEnabled() {
    return nativeMapView.getTileCacheEnabled();
  }

  /**
   * Camera based tile level of detail controls
   *
   * @param radius minimum radius around the view point in unit of tiles in which the fine
   * grained zoom level tiles are always used when performing LOD
   * radius must be greater than 1 (At least 1 fine detailed tile is present)
   * A smaller radius value may improve performance at the cost of quality (tiles away from
   * camera use lower Zoom levels)
   */
  public void setTileLodMinRadius(@FloatRange(from = 1, fromInclusive = true) double radius) {
    nativeMapView.setTileLodMinRadius(radius);
  }

  /**
   * Camera based tile level of detail controls
   *
   * @return minimum radius around the view point in unit of tiles in which the fine grained
   * zoom level tiles are always used when performing LOD
   * @see MapLibreMap#setTileLodMinRadius(double)
   */
  public double getTileLodMinRadius() {
    return nativeMapView.getTileLodMinRadius();
  }

  /**
   * Camera based tile level of detail controls
   *
   * @param scale factor for the distance to the camera view point
   * A value larger than 1 increases the distance to the camera view point reducing LOD
   * Larger values may improve performance at the cost of quality (tiles away from camera
   * use lower Zoom levels)
   */
  public void setTileLodScale(@FloatRange(from = 0, fromInclusive = false) double scale) {
    nativeMapView.setTileLodScale(scale);
  }

  /**
   * Camera based tile level of detail controls
   *
   * @return scale factor for the distance to the camera view point
   * @see MapLibreMap#setTileLodScale(double)
   */
  public double getTileLodScale() {
    return nativeMapView.getTileLodScale();
  }

  /**
   * Camera based tile level of detail controls
   *
   * @param threshold pitch angle in radians above which LOD calculation is performed
   * A smaller radius value may improve performance at the cost of quality
   */
  public void setTileLodPitchThreshold(@FloatRange(from = 0, to = Math.PI) double threshold) {
    nativeMapView.setTileLodPitchThreshold(threshold);
  }

  /**
   * Camera based tile level of detail controls
   *
   * @return pitch angle threshold in radians above which LOD calculation is performed
   * @see MapLibreMap#setTileLodPitchThreshold(double)
   */
  public double getTileLodPitchThreshold() {
    return nativeMapView.getTileLodPitchThreshold();
  }

  /**
   * Camera based tile level of detail controls
   *
   * @param shift shift applied to the Zoom level during LOD calculation
   * A negative value shifts the Zoom level to a coarser level reducing quality but
   * improving performance
   * A positive value shifts the Zoom level to a finer level increasing details but
   * negatively affecting performance
   * A value of zero (default) does not apply any shift to the Zoom level
   * It is not recommended to change the default value unless performance is critical
   * and the loss of quality is acceptable. A value of -1 reduces the number of
   * displayed tiles by a factor of 4 on average
   * It is recommended to first configure the pixelRatio before adjusting
   * TileLodZoomShift. {@link MapLibreMapOptions#pixelRatio(float)}
   */
  public void setTileLodZoomShift(double shift) {
    nativeMapView.setTileLodZoomShift(shift);
  }

  /**
   * Camera based tile level of detail controls
   *
   * @return shift applied to the Zoom level during LOD calculation
   * @see MapLibreMap#setTileLodZoomShift(double)
   */
  public double getTileLodZoomShift() {
    return nativeMapView.getTileLodZoomShift();
  }

  //
  // MinZoom
  //

  /**
   * <p>
   * Sets the minimum zoom level the map can be displayed at.
   * </p>
   *
   * @param minZoom The new minimum zoom level.
   */
  public void setMinZoomPreference(
    @FloatRange(from = MapLibreConstants.MINIMUM_ZOOM, to = MapLibreConstants.MAXIMUM_ZOOM) double minZoom) {
    transform.setMinZoom(minZoom);
  }

  /**
   * <p>
   * Gets the minimum zoom level the map can be displayed at.
   * </p>
   *
   * @return The minimum zoom level.
   */
  public double getMinZoomLevel() {
    return transform.getMinZoom();
  }

  //
  // MaxZoom
  //

  /**
   * <p>
   * Sets the maximum zoom level the map can be displayed at.
   * </p>
   * <p>
   * The default maximum zoomn level is 22. The upper bound for this value is 25.5.
   * </p>
   *
   * @param maxZoom The new maximum zoom level.
   */
  public void setMaxZoomPreference(@FloatRange(from = MapLibreConstants.MINIMUM_ZOOM,
    to = MapLibreConstants.MAXIMUM_ZOOM) double maxZoom) {
    transform.setMaxZoom(maxZoom);
  }

  /**
   * <p>
   * Gets the maximum zoom level the map can be displayed at.
   * </p>
   *
   * @return The maximum zoom level.
   */
  public double getMaxZoomLevel() {
    return transform.getMaxZoom();
  }

  //
  // MinPitch
  //

  /**
   * <p>
   * Sets the minimum Pitch the map can be displayed at.
   * </p>
   *
   * <p>
   * The default and lower bound for minPitch Pitch is 0.
   * </p>
   * @param minPitch The new minimum Pitch.
   */
  public void setMinPitchPreference(
    @FloatRange(from = MapLibreConstants.MINIMUM_PITCH, to = MapLibreConstants.MAXIMUM_PITCH) double minPitch) {
    transform.setMinPitch(minPitch);
  }

  /**
   * <p>
   * Gets the minimum Pitch the map can be displayed at.
   * </p>
   *
   * @return The minimum Pitch.
   */
  public double getMinPitch() {
    return transform.getMinPitch();
  }

  //
  // MaxPitch
  //

  /**
   * <p>
   * Sets the maximum Pitch the map can be displayed at.
   * </p>
   * <p>
   * The default and upper bound for maximum Pitch is 60.
   * </p>
   *
   * @param maxPitch The new maximum Pitch.
   */
  public void setMaxPitchPreference(@FloatRange(from = MapLibreConstants.MINIMUM_PITCH,
    to = MapLibreConstants.MAXIMUM_PITCH) double maxPitch) {
    transform.setMaxPitch(maxPitch);
  }

  /**
   * <p>
   * Gets the maximum Pitch the map can be displayed at.
   * </p>
   *
   * @return The maximum Pitch.
   */
  public double getMaxPitch() {
    return transform.getMaxPitch();
  }

  //
  // UiSettings
  //

  /**
   * Gets the user interface settings for the map.
   *
   * @return the UiSettings associated with this map
   */
  @NonNull
  public UiSettings getUiSettings() {
    return uiSettings;
  }

  //
  // Projection
  //

  /**
   * Get the Projection object that you can use to convert between screen coordinates and latitude/longitude
   * coordinates.
   *
   * @return the Projection associated with this map
   */
  @NonNull
  public Projection getProjection() {
    return projection;
  }

  //
  // Camera API
  //

  /**
   * Cancels ongoing animations.
   * <p>
   * This invokes the {@link CancelableCallback} for ongoing camera updates.
   * </p>
   */
  public void cancelTransitions() {
    transform.cancelTransitions();
  }

  /**
   * Gets the current position of the camera.
   * The CameraPosition returned is a snapshot of the current position, and will not automatically update when the
   * camera moves.
   *
   * @return The current position of the Camera.
   */
  @NonNull
  public final CameraPosition getCameraPosition() {
    return transform.getCameraPosition();
  }

  /**
   * Repositions the camera according to the cameraPosition.
   * The move is instantaneous, and a subsequent getCameraPosition() will reflect the new position.
   * See CameraUpdateFactory for a set of updates.
   *
   * @param cameraPosition the camera position to set
   */
  public void setCameraPosition(@NonNull CameraPosition cameraPosition) {
    moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), true, null);
  }

  /**
   * Repositions the camera according to the instructions defined in the update.
   * The move is instantaneous, and a subsequent getCameraPosition() will reflect the new position.
   * See CameraUpdateFactory for a set of updates.
   *
   * @param update The change that should be applied to the camera.
   */
  public final void moveCamera(@NonNull CameraUpdate update) {
    moveCamera(update, true, null);
  }

  public final void moveCamera(@NonNull final CameraUpdate update,
                               @Nullable final MapLibreMap.CancelableCallback callback) {
    moveCamera(update, true, callback);
  }

  /**
   * Repositions the camera according to the instructions defined in the update.
   * The move is instantaneous, and a subsequent getCameraPosition() will reflect the new position.
   * See CameraUpdateFactory for a set of updates.
   *
   * @param update   The change that should be applied to the camera
   * @param callback the callback to be invoked when an animation finishes or is canceled
   */
  public final void moveCamera(@NonNull final CameraUpdate update,
                               @NonNull final boolean shouldCancelTransitions,
                               @Nullable final MapLibreMap.CancelableCallback callback) {
    notifyDeveloperAnimationListeners();
    transform.moveCamera(MapLibreMap.this, update, shouldCancelTransitions, callback);
  }

  /**
   * Gradually move the camera by the default duration, zoom will not be affected unless specified
   * within {@link CameraUpdate}. If {@link #getCameraPosition()} is called during the animation,
   * it will return the current location of the camera in flight.
   *
   * @param update The change that should be applied to the camera.
   * @see CameraUpdateFactory for a set of updates.
   */
  public final void easeCamera(@NonNull CameraUpdate update) {
    easeCamera(update, MapLibreConstants.ANIMATION_DURATION);
  }

  /**
   * Gradually move the camera by the default duration, zoom will not be affected unless specified
   * within {@link CameraUpdate}. If {@link #getCameraPosition()} is called during the animation,
   * it will return the current location of the camera in flight.
   *
   * @param update   The change that should be applied to the camera.
   * @param callback An optional callback to be notified from the main thread when the animation
   *                 stops. If the animation stops due to its natural completion, the callback
   *                 will be notified with onFinish(). If the animation stops due to interruption
   *                 by a later camera movement or a user gesture, onCancel() will be called.
   *                 Do not update or ease the camera from within onCancel().
   * @see CameraUpdateFactory for a set of updates.
   */
  public final void easeCamera(@NonNull CameraUpdate update, @Nullable final MapLibreMap.CancelableCallback callback) {
    easeCamera(update, MapLibreConstants.ANIMATION_DURATION, callback);
  }

  /**
   * Gradually move the camera by a specified duration in milliseconds, zoom will not be affected
   * unless specified within {@link CameraUpdate}. If {@link #getCameraPosition()} is called
   * during the animation, it will return the current location of the camera in flight.
   *
   * @param update     The change that should be applied to the camera.
   * @param durationMs The duration of the animation in milliseconds. This must be strictly
   *                   positive, otherwise an IllegalArgumentException will be thrown.
   * @see CameraUpdateFactory for a set of updates.
   */
  public final void easeCamera(@NonNull CameraUpdate update, int durationMs) {
    easeCamera(update, durationMs, null);
  }

  /**
   * Gradually move the camera by a specified duration in milliseconds, zoom will not be affected
   * unless specified within {@link CameraUpdate}. A callback can be used to be notified when
   * easing the camera stops. If {@link #getCameraPosition()} is called during the animation, it
   * will return the current location of the camera in flight.
   * <p>
   * Note that this will cancel location tracking mode if enabled.
   * </p>
   *
   * @param update     The change that should be applied to the camera.
   * @param durationMs The duration of the animation in milliseconds. This must be strictly
   *                   positive, otherwise an IllegalArgumentException will be thrown.
   * @param callback   An optional callback to be notified from the main thread when the animation
   *                   stops. If the animation stops due to its natural completion, the callback
   *                   will be notified with onFinish(). If the animation stops due to interruption
   *                   by a later camera movement or a user gesture, onCancel() will be called.
   *                   Do not update or ease the camera from within onCancel().
   * @see CameraUpdateFactory for a set of updates.
   */
  public final void easeCamera(@NonNull CameraUpdate update, int durationMs,
                               @Nullable final MapLibreMap.CancelableCallback callback) {
    easeCamera(update, durationMs, true, callback);
  }

  /**
   * Gradually move the camera by a specified duration in milliseconds, zoom will not be affected
   * unless specified within {@link CameraUpdate}. A callback can be used to be notified when
   * easing the camera stops. If {@link #getCameraPosition()} is called during the animation, it
   * will return the current location of the camera in flight.
   * <p>
   * Note that this will cancel location tracking mode if enabled.
   * </p>
   *
   * @param update             The change that should be applied to the camera.
   * @param durationMs         The duration of the animation in milliseconds. This must be strictly
   *                           positive, otherwise an IllegalArgumentException will be thrown.
   * @param easingInterpolator True for easing interpolator, false for linear.
   */
  public final void easeCamera(@NonNull CameraUpdate update, int durationMs, boolean easingInterpolator) {
    easeCamera(update, durationMs, easingInterpolator, true, null);
  }

  public final void easeCamera(@NonNull CameraUpdate update, int durationMs, boolean easingInterpolator,
                               @Nullable final MapLibreMap.CancelableCallback callback) {
    easeCamera(update, durationMs, easingInterpolator, true, callback);
  }

  /**
   * Gradually move the camera by a specified duration in milliseconds, zoom will not be affected
   * unless specified within {@link CameraUpdate}. A callback can be used to be notified when
   * easing the camera stops. If {@link #getCameraPosition()} is called during the animation, it
   * will return the current location of the camera in flight.
   *
   * @param update             The change that should be applied to the camera.
   * @param durationMs         The duration of the animation in milliseconds. This must be strictly
   *                           positive, otherwise an IllegalArgumentException will be thrown.
   * @param easingInterpolator True for easing interpolator, false for linear.
   * @param callback           An optional callback to be notified from the main thread when the animation
   *                           stops. If the animation stops due to its natural completion, the callback
   *                           will be notified with onFinish(). If the animation stops due to interruption
   *                           by a later camera movement or a user gesture, onCancel() will be called.
   *                           Do not update or ease the camera from within onCancel().
   */
  public final void easeCamera(@NonNull final CameraUpdate update,
                               final int durationMs,
                               final boolean easingInterpolator,
                               final boolean shouldCancelTransitions,
                               @Nullable final MapLibreMap.CancelableCallback callback) {
    if (durationMs <= 0) {
      throw new IllegalArgumentException("Null duration passed into easeCamera");
    }
    notifyDeveloperAnimationListeners();

    transform.easeCamera(MapLibreMap.this, update, durationMs, easingInterpolator, shouldCancelTransitions, callback);
  }

  /**
   * Animate the camera to a new location defined within {@link CameraUpdate} using a transition
   * animation that evokes powered flight. The animation will last the default amount of time.
   * During the animation, a call to {@link #getCameraPosition()} returns an intermediate location
   * of the camera in flight.
   *
   * @param update The change that should be applied to the camera.
   * @see CameraUpdateFactory for a set of updates.
   */
  public final void animateCamera(@NonNull CameraUpdate update) {
    animateCamera(update, MapLibreConstants.ANIMATION_DURATION, true, null);
  }

  /**
   * Animate the camera to a new location defined within {@link CameraUpdate} using a transition
   * animation that evokes powered flight. The animation will last the default amount of time. A
   * callback can be used to be notified when animating the camera stops. During the animation, a
   * call to {@link #getCameraPosition()} returns an intermediate location of the camera in flight.
   *
   * @param update   The change that should be applied to the camera.
   * @param callback The callback to invoke from the main thread when the animation stops. If the
   *                 animation completes normally, onFinish() is called; otherwise, onCancel() is
   *                 called. Do not update or animate the camera from within onCancel().
   * @see CameraUpdateFactory for a set of updates.
   */
  public final void animateCamera(@NonNull CameraUpdate update, @Nullable MapLibreMap.CancelableCallback callback) {
    animateCamera(update, MapLibreConstants.ANIMATION_DURATION, true, callback);
  }

  /**
   * Animate the camera to a new location defined within {@link CameraUpdate} using a transition
   * animation that evokes powered flight. The animation will last a specified amount of time
   * given in milliseconds. During the animation, a call to {@link #getCameraPosition()} returns
   * an intermediate location of the camera in flight.
   *
   * @param update     The change that should be applied to the camera.
   * @param durationMs The duration of the animation in milliseconds. This must be strictly
   *                   positive, otherwise an IllegalArgumentException will be thrown.
   * @see CameraUpdateFactory for a set of updates.
   */
  public final void animateCamera(@NonNull CameraUpdate update, int durationMs) {
    animateCamera(update, durationMs, true, null);
  }

  public final void animateCamera(@NonNull CameraUpdate update, int durationMs,
                                  @Nullable MapLibreMap.CancelableCallback callback) {
    animateCamera(update, durationMs, true, callback);
  }

  /**
   * Animate the camera to a new location defined within {@link CameraUpdate} using a transition
   * animation that evokes powered flight. The animation will last a specified amount of time
   * given in milliseconds. A callback can be used to be notified when animating the camera stops.
   * During the animation, a call to {@link #getCameraPosition()} returns an intermediate location
   * of the camera in flight.
   *
   * @param update     The change that should be applied to the camera.
   * @param durationMs The duration of the animation in milliseconds. This must be strictly
   *                   positive, otherwise an IllegalArgumentException will be thrown.
   * @param callback   An optional callback to be notified from the main thread when the animation
   *                   stops. If the animation stops due to its natural completion, the callback
   *                   will be notified with onFinish(). If the animation stops due to interruption
   *                   by a later camera movement or a user gesture, onCancel() will be called.
   *                   Do not update or animate the camera from within onCancel(). If a callback
   *                   isn't required, leave it as null.
   * @see CameraUpdateFactory for a set of updates.
   */
  public final void animateCamera(@NonNull final CameraUpdate update, final int durationMs,
                                  final boolean shouldCancelTransitions,
                                  @Nullable final MapLibreMap.CancelableCallback callback) {
    if (durationMs <= 0) {
      throw new IllegalArgumentException("Null duration passed into animateCamera");
    }
    notifyDeveloperAnimationListeners();
    transform.animateCamera(MapLibreMap.this, update, durationMs, shouldCancelTransitions, callback);
  }

  /**
   * Scrolls the camera over the map, shifting the center of view by the specified number of pixels in the x and y
   * directions.
   *
   * @param x Amount of pixels to scroll to in x direction
   * @param y Amount of pixels to scroll to in y direction
   */
  public void scrollBy(float x, float y) {
    scrollBy(x, y, 0);
  }

  /**
   * Scrolls the camera over the map, shifting the center of view by the specified number of pixels in the x and y
   * directions.
   *
   * @param x        Amount of pixels to scroll to in x direction
   * @param y        Amount of pixels to scroll to in y direction
   * @param duration Amount of time the scrolling should take
   */
  public void scrollBy(float x, float y, long duration) {
    notifyDeveloperAnimationListeners();
    nativeMapView.moveBy(x, y, duration);
  }

  /**
   * Returns the current zoom level.
   */
  public double getZoom() {
    return nativeMapView.getZoom();
  }

  /**
   * Zooms the camera to the specified level.
   * @param zoom              The zoom level to which the camera should move.
   * @param focalPoint        The point around which to zoom.
   * @param duration          The duration for the zoom animation
   */
  public void setZoom(double zoom, @NonNull PointF focalPoint, long duration ) {
    notifyDeveloperAnimationListeners();
    nativeMapView.setZoom(zoom, focalPoint, duration);
  }

  //
  //  Reset North
  //

  /**
   * Resets the map view to face north.
   */
  public void resetNorth() {
    notifyDeveloperAnimationListeners();
    transform.resetNorth();
  }

  /**
   * Transform the map bearing given a bearing, focal point coordinates, and a duration.
   *
   * @param bearing  The bearing of the Map to be transformed to
   * @param focalX   The x coordinate of the focal point
   * @param focalY   The y coordinate of the focal point
   * @param duration The duration of the transformation
   */
  public void setFocalBearing(double bearing, float focalX, float focalY, long duration) {
    notifyDeveloperAnimationListeners();
    transform.setBearing(bearing, focalX, focalY, duration);
  }

  /**
   * Returns the measured height of the Map.
   *
   * @return the height of the map
   */
  public float getHeight() {
    return projection.getHeight();
  }

  /**
   * Returns the measured width of the Map.
   *
   * @return the width of the map
   */
  public float getWidth() {
    return projection.getWidth();
  }

  //
  // Offline
  //

  /**
   * Loads a new style from the specified offline region definition and moves the map camera to that region.
   *
   * @param definition the offline region definition
   * @see OfflineRegionDefinition
   */
  public void setOfflineRegionDefinition(@NonNull OfflineRegionDefinition definition) {
    setOfflineRegionDefinition(definition, null);
  }

  /**
   * Loads a new style from the specified offline region definition and moves the map camera to that region.
   *
   * @param definition the offline region definition
   * @param callback   the callback to be invoked when the style has loaded
   * @see OfflineRegionDefinition
   */
  public void setOfflineRegionDefinition(@NonNull OfflineRegionDefinition definition,
                                         @Nullable Style.OnStyleLoaded callback) {
    double minZoom = definition.getMinZoom();
    double maxZoom = definition.getMaxZoom();
    CameraPosition cameraPosition = new CameraPosition.Builder()
      .target(definition.getBounds().getCenter())
      .zoom(minZoom)
      .build();
    moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    setMinZoomPreference(minZoom);
    setMaxZoomPreference(maxZoom);
    setStyle(new Style.Builder().fromUri(definition.getStyleURL()), callback);
  }

  //
  // Debug
  //

  /**
   * Returns whether the map debug information is currently shown.
   *
   * @return If true, map debug information is currently shown.
   */
  public boolean isDebugActive() {
    return debugActive;
  }

  /**
   * <p>
   * Changes whether the map debug information is shown.
   * </p>
   * The default value is false.
   *
   * @param debugActive If true, map debug information is shown.
   */
  public void setDebugActive(boolean debugActive) {
    this.debugActive = debugActive;
    nativeMapView.setDebug(debugActive);
  }

  /**
   * <p>
   * Cycles through the map debug options.
   * </p>
   * The value of isDebugActive reflects whether there are
   * any map debug options enabled or disabled.
   *
   * @see #isDebugActive()
   * @deprecated use {@link #setDebugActive(boolean)}
   */
  @Deprecated
  public void cycleDebugOptions() {
    this.debugActive = !nativeMapView.getDebug();
    nativeMapView.setDebug(debugActive);
  }

  /**
   * <p>
   * Get the list of action journal log files from oldest to newest.
   * </p>
   */
  public String[] getActionJournalLogFiles() {
    return nativeMapView.getActionJournalLogFiles();
  }

  /**
   * <p>
   * Get the action journal events from oldest to newest.
   * </p>
   * Each element contains a serialized json object with the event data.
   * Example
   * `{
   *   "name" : "onTileAction",
   *   "time" : "2025-04-17T13:13:13.974Z",
   *   "styleName" : "Streets",
   *   "styleURL" : "maptiler://maps/streets",
   *   "event" : {
   *     "action" : "RequestedFromNetwork",
   *     "tileX" : 0,
   *     "tileY" : 0,
   *     "tileZ" : 0,
   *     "overscaledZ" : 0,
   *     "sourceID" : "openmaptiles"
   *   }
   * }`
   */
  public String[] getActionJournalLog() {
    return nativeMapView.getActionJournalLog();
  }

  /**
   * <p>
   * Clear stored action journal events.
   * </p>
   */
  public void clearActionJournalLog() {
    nativeMapView.clearActionJournalLog();
  }

  //
  // API endpoint config
  //

  private void setApiBaseUrl(@NonNull MapLibreMapOptions options) {
    String apiBaseUrl = options.getApiBaseUrl();
    if (!TextUtils.isEmpty(apiBaseUrl)) {
      nativeMapView.setApiBaseUrl(apiBaseUrl);
    }
  }

  //
  // Styling
  //

  /**
   * Loads a new map style from the specified bundled style.
   * <p>
   * This method is asynchronous and will return before the style finishes loading.
   * If you wish to wait for the map to finish loading, listen to the {@link MapView.OnDidFinishLoadingStyleListener}
   * callback or use the {@link #setStyle(String, Style.OnStyleLoaded)} method instead.
   * </p>
   * If the style fails to load or an invalid style URL is set, the map view will become blank.
   * An error message will be logged in the Android logcat and {@link MapView.OnDidFailLoadingMapListener} callback
   * will be triggered.
   *
   * @param style The bundled style
   * @see Style
   */
  public void setStyle(String style) {
    this.setStyle(style, null);
  }

  /**
   * Loads a new map style from the specified bundled style.
   * <p>
   * If the style fails to load or an invalid style URL is set, the map view will become blank.
   * An error message will be logged in the Android logcat and {@link MapView.OnDidFailLoadingMapListener} callback
   * will be triggered.
   * </p>
   *
   * @param style    The bundled style
   * @param callback The callback to be invoked when the style has loaded
   * @see Style
   */
  public void setStyle(String style, final Style.OnStyleLoaded callback) {
    this.setStyle(new Style.Builder().fromUri(style), callback);
  }

  /**
   * Loads a new map style from the specified builder.
   * <p>
   * If the builder fails to load, the map view will become blank. An error message will be logged in the Android logcat
   * and {@link MapView.OnDidFailLoadingMapListener} callback will be triggered. If you wish to wait for the map to
   * finish loading, listen to the {@link MapView.OnDidFinishLoadingStyleListener} callback or use the
   * {@link #setStyle(String, Style.OnStyleLoaded)} instead.
   * </p>
   *
   * @param builder The style builder
   * @see Style
   */
  public void setStyle(Style.Builder builder) {
    this.setStyle(builder, null);
  }

  /**
   * Loads a new map style from the specified builder.
   * <p>
   * If the builder fails to load, the map view will become blank. An error message will be logged in the Android logcat
   * and {@link MapView.OnDidFailLoadingMapListener} callback will be triggered.
   * </p>
   *
   * @param builder  The style builder
   * @param callback The callback to be invoked when the style has loaded
   * @see Style
   */
  public void setStyle(Style.Builder builder, final Style.OnStyleLoaded callback) {
    styleLoadedCallback = callback;
    locationComponent.onStartLoadingMap();
    if (style != null) {
      style.clear();
    }

    style = builder.build(nativeMapView);
    if (!TextUtils.isEmpty(builder.getUri())) {
      nativeMapView.setStyleUri(builder.getUri());
    } else if (!TextUtils.isEmpty(builder.getJson())) {
      nativeMapView.setStyleJson(builder.getJson());
    } else {
      // user didn't provide a `from` component, load a blank style instead
      nativeMapView.setStyleJson(Style.EMPTY_JSON);
    }
  }

  void notifyStyleLoaded() {
    if (nativeMapView.isDestroyed()) {
      return;
    }

    if (style != null) {
      style.onDidFinishLoadingStyle();
      locationComponent.onFinishLoadingStyle();

      // notify the listener provided with the style setter
      if (styleLoadedCallback != null) {
        styleLoadedCallback.onStyleLoaded(style);
      }

      // notify style getters
      for (Style.OnStyleLoaded styleGetter : awaitingStyleGetters) {
        styleGetter.onStyleLoaded(style);
      }
    } else {
      MapStrictMode.strictModeViolation("No style to provide.");
    }
    styleLoadedCallback = null;
    awaitingStyleGetters.clear();
  }

  //
  // Annotations
  //

  /**
   * <p>
   * Adds a marker to this map.
   * </p>
   * The marker's icon is rendered on the map at the location {@code Marker.position}.
   * If {@code Marker.title} is defined, the map shows an info box with the marker's title and snippet.
   *
   * @param markerOptions A marker options object that defines how to render the marker
   * @return The {@code Marker} that was added to the map
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public Marker addMarker(@NonNull MarkerOptions markerOptions) {
    return annotationManager.addMarker(markerOptions, this);
  }

  /**
   * <p>
   * Adds a marker to this map.
   * </p>
   * The marker's icon is rendered on the map at the location {@code Marker.position}.
   * If {@code Marker.title} is defined, the map shows an info box with the marker's title and snippet.
   *
   * @param markerOptions A marker options object that defines how to render the marker
   * @return The {@code Marker} that was added to the map
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public Marker addMarker(@NonNull BaseMarkerOptions markerOptions) {
    return annotationManager.addMarker(markerOptions, this);
  }

  /**
   * <p>
   * Adds multiple markers to this map.
   * </p>
   * The marker's icon is rendered on the map at the location {@code Marker.position}.
   * If {@code Marker.title} is defined, the map shows an info box with the marker's title and snippet.
   *
   * @param markerOptionsList A list of marker options objects that defines how to render the markers
   * @return A list of the {@code Marker}s that were added to the map
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public List<Marker> addMarkers(@NonNull List<? extends
    BaseMarkerOptions> markerOptionsList) {
    return annotationManager.addMarkers(markerOptionsList, this);
  }

  /**
   * <p>
   * Updates a marker on this map. Does nothing if the marker isn't already added.
   * </p>
   *
   * @param updatedMarker An updated marker object
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void updateMarker(@NonNull Marker updatedMarker) {
    annotationManager.updateMarker(updatedMarker, this);
  }

  /**
   * Adds a polyline to this map.
   *
   * @param polylineOptions A polyline options object that defines how to render the polyline
   * @return The {@code Polyine} that was added to the map
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public Polyline addPolyline(@NonNull PolylineOptions polylineOptions) {
    return annotationManager.addPolyline(polylineOptions, this);
  }

  /**
   * Adds multiple polylines to this map.
   *
   * @param polylineOptionsList A list of polyline options objects that defines how to render the polylines.
   * @return A list of the {@code Polyline}s that were added to the map.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public List<Polyline> addPolylines(@NonNull List<PolylineOptions> polylineOptionsList) {
    return annotationManager.addPolylines(polylineOptionsList, this);
  }

  /**
   * Update a polyline on this map.
   *
   * @param polyline An updated polyline object.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void updatePolyline(@NonNull Polyline polyline) {
    annotationManager.updatePolyline(polyline);
  }

  /**
   * Adds a polygon to this map.
   *
   * @param polygonOptions A polygon options object that defines how to render the polygon.
   * @return The {@code Polygon} that was added to the map.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public Polygon addPolygon(@NonNull PolygonOptions polygonOptions) {
    return annotationManager.addPolygon(polygonOptions, this);
  }

  /**
   * Adds multiple polygons to this map.
   *
   * @param polygonOptionsList A list of polygon options objects that defines how to render the polygons
   * @return A list of the {@code Polygon}s that were added to the map
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public List<Polygon> addPolygons(@NonNull List<PolygonOptions> polygonOptionsList) {
    return annotationManager.addPolygons(polygonOptionsList, this);
  }

  /**
   * Update a polygon on this map.
   *
   * @param polygon An updated polygon object
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void updatePolygon(@NonNull Polygon polygon) {
    annotationManager.updatePolygon(polygon);
  }

  /**
   * <p>
   * Convenience method for removing a Marker from the map.
   * </p>
   * Calls removeAnnotation() internally.
   *
   * @param marker Marker to remove
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void removeMarker(@NonNull Marker marker) {
    annotationManager.removeAnnotation(marker);
  }

  /**
   * <p>
   * Convenience method for removing a Polyline from the map.
   * </p>
   * Calls removeAnnotation() internally.
   *
   * @param polyline Polyline to remove
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void removePolyline(@NonNull Polyline polyline) {
    annotationManager.removeAnnotation(polyline);
  }

  /**
   * <p>
   * Convenience method for removing a Polygon from the map.
   * </p>
   * Calls removeAnnotation() internally.
   *
   * @param polygon Polygon to remove
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void removePolygon(@NonNull Polygon polygon) {
    annotationManager.removeAnnotation(polygon);
  }

  /**
   * Removes an annotation from the map.
   *
   * @param annotation The annotation object to remove.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void removeAnnotation(@NonNull Annotation annotation) {
    annotationManager.removeAnnotation(annotation);
  }

  /**
   * Removes an annotation from the map
   *
   * @param id The identifier associated to the annotation to be removed
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void removeAnnotation(long id) {
    annotationManager.removeAnnotation(id);
  }

  /**
   * Removes multiple annotations from the map.
   *
   * @param annotationList A list of annotation objects to remove.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void removeAnnotations(@NonNull List<? extends Annotation> annotationList) {
    annotationManager.removeAnnotations(annotationList);
  }

  /**
   * Removes all annotations from the map.
   *
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void removeAnnotations() {
    annotationManager.removeAnnotations();
  }

  /**
   * Removes all markers, polylines, polygons, overlays, etc from the map.
   *
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void clear() {
    annotationManager.removeAnnotations();
  }

  /**
   * Return a annotation based on its id.
   *
   * @param id the id used to look up an annotation
   * @return An annotation with a matched id, null is returned if no match was found
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @Nullable
  public Annotation getAnnotation(long id) {
    return annotationManager.getAnnotation(id);
  }

  /**
   * Returns a list of all the annotations on the map.
   *
   * @return A list of all the annotation objects. The returned object is a copy so modifying this
   * list will not update the map
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public List<Annotation> getAnnotations() {
    return annotationManager.getAnnotations();
  }

  /**
   * Returns a list of all the markers on the map.
   *
   * @return A list of all the markers objects. The returned object is a copy so modifying this
   * list will not update the map.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public List<Marker> getMarkers() {
    return annotationManager.getMarkers();
  }

  /**
   * Returns a list of all the polygons on the map.
   *
   * @return A list of all the polygon objects. The returned object is a copy so modifying this
   * list will not update the map.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public List<Polygon> getPolygons() {
    return annotationManager.getPolygons();
  }

  /**
   * Returns a list of all the polylines on the map.
   *
   * @return A list of all the polylines objects. The returned object is a copy so modifying this
   * list will not update the map.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public List<Polyline> getPolylines() {
    return annotationManager.getPolylines();
  }

  /**
   * Sets a callback that's invoked when the user clicks on a marker.
   *
   * @param listener The callback that's invoked when the user clicks on a marker.
   *                 To unset the callback, use null.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void setOnMarkerClickListener(@Nullable OnMarkerClickListener listener) {
    annotationManager.setOnMarkerClickListener(listener);
  }

  /**
   * Sets a callback that's invoked when the user clicks on a polygon.
   *
   * @param listener The callback that's invoked when the user clicks on a polygon.
   *                 To unset the callback, use null.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void setOnPolygonClickListener(@Nullable OnPolygonClickListener listener) {
    annotationManager.setOnPolygonClickListener(listener);
  }

  /**
   * Sets a callback that's invoked when the user clicks on a polyline.
   *
   * @param listener The callback that's invoked when the user clicks on a polyline.
   *                 To unset the callback, use null.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void setOnPolylineClickListener(@Nullable OnPolylineClickListener listener) {
    annotationManager.setOnPolylineClickListener(listener);
  }

  /**
   * <p>
   * Selects a marker. The selected marker will have it's info window opened.
   * Any other open info windows will be closed unless isAllowConcurrentMultipleOpenInfoWindows()
   * is true.
   * </p>
   * Selecting an already selected marker will have no effect.
   *
   * @param marker The marker to select.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void selectMarker(@NonNull Marker marker) {
    if (marker == null) {
      Logger.w(TAG, "marker was null, so just returning");
      return;
    }
    annotationManager.selectMarker(marker);
  }

  /**
   * Deselects any currently selected marker. All markers will have it's info window closed.
   *
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void deselectMarkers() {
    annotationManager.deselectMarkers();
  }

  /**
   * Deselects a currently selected marker. The selected marker will have it's info window closed.
   *
   * @param marker the marker to deselect
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void deselectMarker(@NonNull Marker marker) {
    annotationManager.deselectMarker(marker);
  }

  /**
   * Gets the currently selected marker.
   *
   * @return The currently selected marker.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @NonNull
  public List<Marker> getSelectedMarkers() {
    return annotationManager.getSelectedMarkers();
  }

  //
  // InfoWindow
  //

  /**
   * <p>
   * Sets a custom renderer for the contents of info window.
   * </p>
   * When set your callback is invoked when an info window is about to be shown. By returning
   * a custom {@link View}, the default info window will be replaced.
   *
   * @param infoWindowAdapter The callback to be invoked when an info window will be shown.
   *                          To unset the callback, use null.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void setInfoWindowAdapter(@Nullable InfoWindowAdapter infoWindowAdapter) {
    annotationManager.getInfoWindowManager().setInfoWindowAdapter(infoWindowAdapter);
  }

  /**
   * Gets the callback to be invoked when an info window will be shown.
   *
   * @return The callback to be invoked when an info window will be shown.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  @Nullable
  public InfoWindowAdapter getInfoWindowAdapter() {
    return annotationManager.getInfoWindowManager().getInfoWindowAdapter();
  }

  /**
   * Changes whether the map allows concurrent multiple infowindows to be shown.
   *
   * @param allow If true, map allows concurrent multiple infowindows to be shown.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public void setAllowConcurrentMultipleOpenInfoWindows(boolean allow) {
    annotationManager.getInfoWindowManager().setAllowConcurrentMultipleOpenInfoWindows(allow);
  }

  /**
   * Returns whether the map allows concurrent multiple infowindows to be shown.
   *
   * @return If true, map allows concurrent multiple infowindows to be shown.
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public boolean isAllowConcurrentMultipleOpenInfoWindows() {
    return annotationManager.getInfoWindowManager().isAllowConcurrentMultipleOpenInfoWindows();
  }

  //
  // LatLngBounds
  //

  /**
   * Sets a LatLngBounds that constraints map transformations to this bounds.
   * <p>
   * Set to null to clear current bounds, newly set bounds will override previously set bounds.
   * </p>
   *
   * @param latLngBounds the bounds to constrain the map with
   */
  public void setLatLngBoundsForCameraTarget(@Nullable LatLngBounds latLngBounds) {
    nativeMapView.setLatLngBounds(latLngBounds);
  }


  /**
   * Get a camera position that fits a provided bounds and the current camera tilt and bearing.
   *
   * @param latLngBounds the bounds to set the map with
   * @return the camera position that fits the bounds
   */
  @Nullable
  public CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds) {
    // we use current camera tilt value to provide expected transformations as #11993
    return getCameraForLatLngBounds(latLngBounds, new int[] {0, 0, 0, 0});
  }


  /**
   * Get a camera position that fits a provided bounds and padding and the current camera tilt and bearing.
   *
   * @param latLngBounds the bounds to set the map with
   * @param padding      the padding to apply to the bounds (in left, top, right, bottom order)
   * @return the camera position that fits the bounds and padding
   */
  @Nullable
  public CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds,
                                                 @NonNull @Size(value = 4) int[] padding) {
    // we use current camera tilt/bearing value to provide expected transformations as #11993
    return getCameraForLatLngBounds(latLngBounds, padding, transform.getRawBearing(), transform.getTilt());
  }


  /**
   * Get a camera position that fits a provided bounds, bearing and tilt.
   *
   * @param latLngBounds the bounds to set the map with
   * @param bearing      the bearing to transform the camera position with
   * @param tilt         to transform the camera position with
   * @return the camera position that fits the bounds and given bearing and tilt
   */
  @Nullable
  public CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds,
                                                 @FloatRange(from = MapLibreConstants.MINIMUM_DIRECTION,
                                                   to = MapLibreConstants.MAXIMUM_DIRECTION) double bearing,
                                                 @FloatRange(from = MapLibreConstants.MINIMUM_TILT,
                                                   to = MapLibreConstants.MAXIMUM_TILT) double tilt) {
    return getCameraForLatLngBounds(latLngBounds, new int[] {0, 0, 0, 0}, bearing, tilt);
  }


  /**
   * Get a camera position that fits a provided bounds, padding, bearing and tilt.
   *
   * @param latLngBounds the bounds to set the map with
   * @param padding      the padding to apply to the bounds (in left, top, right, bottom order)
   * @param bearing      the bearing to transform the camera position with
   * @param tilt         to transform the camera position with
   * @return the camera position that fits the bounds, bearing and tilt
   */
  @Nullable
  public CameraPosition getCameraForLatLngBounds(@NonNull LatLngBounds latLngBounds,
                                                 @NonNull @Size(value = 4) int[] padding,
                                                 @FloatRange(from = MapLibreConstants.MINIMUM_DIRECTION,
                                                   to = MapLibreConstants.MAXIMUM_DIRECTION) double bearing,
                                                 @FloatRange(from = MapLibreConstants.MINIMUM_TILT,
                                                   to = MapLibreConstants.MAXIMUM_TILT) double tilt) {
    return nativeMapView.getCameraForLatLngBounds(latLngBounds, padding, bearing, tilt);
  }

  /**
   * Get a camera position that fits a provided shape.
   *
   * @param geometry the geometry to wraps the map with
   * @return the camera position that fits the geometry inside
   */
  @Nullable
  public CameraPosition getCameraForGeometry(@NonNull Geometry geometry) {
    // we use current camera tilt value to provide expected transformations as #11993
    return getCameraForGeometry(geometry, new int[] {0, 0, 0, 0});
  }

  /**
   * Get a camera position that fits a provided shape and padding.
   *
   * @param geometry the geometry to wraps the map with
   * @param padding  the padding to apply to the bounds
   * @return the camera position that fits the geometry inside and padding
   */
  @Nullable
  public CameraPosition getCameraForGeometry(@NonNull Geometry geometry,
                                             @NonNull @Size(value = 4) int[] padding) {
    // we use current camera tilt/bearing value to provide expected transformations as #11993
    return getCameraForGeometry(geometry, padding, transform.getBearing(), transform.getTilt());
  }

  /**
   * Get a camera position that fits a provided shape with a given bearing and tilt.
   *
   * @param geometry the geometry to wraps the map with
   * @param bearing  the bearing at which to compute the geometry's bounds
   * @param tilt     the tilt at which to compute the geometry's bounds
   * @return the camera position that the geometry inside with bearing and tilt
   */
  @Nullable
  public CameraPosition getCameraForGeometry(@NonNull Geometry geometry,
                                             @FloatRange(from = MapLibreConstants.MINIMUM_DIRECTION,
                                               to = MapLibreConstants.MAXIMUM_DIRECTION) double bearing,
                                             @FloatRange(from = MapLibreConstants.MINIMUM_TILT,
                                               to = MapLibreConstants.MAXIMUM_TILT) double tilt) {
    return getCameraForGeometry(geometry, new int[] {0, 0, 0, 0}, bearing, tilt);
  }

  /**
   * Get a camera position that fits a provided shape with a given padding, bearing and tilt.
   *
   * @param geometry the geometry to wraps the map with
   * @param padding  the padding to apply to the bounds
   * @param bearing  the bearing at which to compute the geometry's bounds
   * @param tilt     the tilt at which to compute the geometry's bounds
   * @return the camera position that fits the geometry inside with padding, bearing and tilt
   */
  @Nullable
  public CameraPosition getCameraForGeometry(@NonNull Geometry geometry,
                                             @NonNull @Size(value = 4) int[] padding,
                                             @FloatRange(from = MapLibreConstants.MINIMUM_DIRECTION,
                                               to = MapLibreConstants.MAXIMUM_DIRECTION) double bearing,
                                             @FloatRange(from = MapLibreConstants.MINIMUM_TILT,
                                               to = MapLibreConstants.MAXIMUM_TILT) double tilt) {
    return nativeMapView.getCameraForGeometry(geometry, padding, bearing, tilt);
  }

  //
  // Padding
  //

  /**
   * <p>
   * Sets the distance from the edges of the map view&#x27;s frame to the edges of the map
   * view&#x27s logical viewport.
   * </p>
   * <p>
   * When the value of this property is equal to {0,0,0,0}, viewport
   * properties such as &#x27;centerCoordinate&#x27; assume a viewport that matches the map
   * view&#x27;s frame. Otherwise, those properties are inset, excluding part of the
   * frame from the viewport. For instance, if the only the top edge is inset, the
   * map center is effectively shifted downward.
   * </p>
   * <p>
   * This method sets the padding "lazily".
   * This means that the <b>padding is going to be applied with the next camera transformation.</b>
   * To apply the padding immediately use {@link CameraPosition.Builder#padding(double, double, double, double)}
   * or {@link CameraUpdateFactory#paddingTo(double, double, double, double)}.
   * </p>
   *
   * @param left   The left margin in pixels.
   * @param top    The top margin in pixels.
   * @param right  The right margin in pixels.
   * @param bottom The bottom margin in pixels.
   * @deprecated Use {@link CameraPosition.Builder#padding(double, double, double, double)}
   * or {@link CameraUpdateFactory#paddingTo(double, double, double, double)} instead.
   */
  @Deprecated
  public void setPadding(int left, int top, int right, int bottom) {
    // TODO padding should be passed as doubles
    projection.setContentPadding(new int[] {left, top, right, bottom});
    uiSettings.invalidate();
  }

  /**
   * Returns the current configured content padding on map view. This might return the currently visible padding
   * or the padding cached but not yet applied by {@link #setPadding(int, int, int, int)}.
   *
   * @return An array with length 4 in the LTRB order.
   * @deprecated Use {@link CameraPosition#padding} instead.
   */
  @Deprecated
  @NonNull
  public int[] getPadding() {
    // TODO this should return double[] (semver major change)
    return projection.getContentPadding();
  }

  //
  // Map events
  //

  /**
   * Adds a callback that is invoked when camera movement has ended.
   *
   * @param listener the listener to notify
   */
  public void addOnCameraIdleListener(@NonNull OnCameraIdleListener listener) {
    cameraChangeDispatcher.addOnCameraIdleListener(listener);
  }

  /**
   * Removes a callback that is invoked when camera movement has ended.
   *
   * @param listener the listener to remove
   */
  public void removeOnCameraIdleListener(@NonNull OnCameraIdleListener listener) {
    cameraChangeDispatcher.removeOnCameraIdleListener(listener);
  }

  /**
   * Adds a callback that is invoked when camera movement was cancelled.
   *
   * @param listener the listener to notify
   */
  public void addOnCameraMoveCancelListener(@NonNull OnCameraMoveCanceledListener listener) {
    cameraChangeDispatcher.addOnCameraMoveCancelListener(listener);
  }

  /**
   * Removes a callback that is invoked when camera movement was cancelled.
   *
   * @param listener the listener to remove
   */
  public void removeOnCameraMoveCancelListener(@NonNull OnCameraMoveCanceledListener listener) {
    cameraChangeDispatcher.removeOnCameraMoveCancelListener(listener);
  }

  /**
   * Adds a callback that is invoked when camera movement has started.
   *
   * @param listener the listener to notify
   */
  public void addOnCameraMoveStartedListener(@NonNull OnCameraMoveStartedListener listener) {
    cameraChangeDispatcher.addOnCameraMoveStartedListener(listener);
  }

  /**
   * Removes a callback that is invoked when camera movement has started.
   *
   * @param listener the listener to remove
   */
  public void removeOnCameraMoveStartedListener(@NonNull OnCameraMoveStartedListener listener) {
    cameraChangeDispatcher.removeOnCameraMoveStartedListener(listener);
  }

  /**
   * Adds a callback that is invoked when camera position changes.
   *
   * @param listener the listener to notify
   */
  public void addOnCameraMoveListener(@NonNull OnCameraMoveListener listener) {
    cameraChangeDispatcher.addOnCameraMoveListener(listener);
  }

  /**
   * Removes a callback that is invoked when camera position changes.
   *
   * @param listener the listener to remove
   */
  public void removeOnCameraMoveListener(@NonNull OnCameraMoveListener listener) {
    cameraChangeDispatcher.removeOnCameraMoveListener(listener);
  }

  /**
   * Sets a callback that's invoked on every frame rendered to the map view.
   *
   * @param listener The callback that's invoked on every frame rendered to the map view.
   *                 To unset the callback, use null.
   */
  public void setOnFpsChangedListener(@Nullable OnFpsChangedListener listener) {
    onFpsChangedListener = listener;
    nativeMapView.setOnFpsChangedListener(listener);
  }

  // used by MapView
  @Nullable
  OnFpsChangedListener getOnFpsChangedListener() {
    return onFpsChangedListener;
  }

  /**
   * Adds a callback that's invoked when the map is flinged.
   *
   * @param listener The callback that's invoked when the map is flinged.
   */
  public void addOnFlingListener(@NonNull OnFlingListener listener) {
    onGesturesManagerInteractionListener.onAddFlingListener(listener);
  }

  /**
   * Removes a callback that's invoked when the map is flinged.
   *
   * @param listener The callback that's invoked when the map is flinged.
   */
  public void removeOnFlingListener(@NonNull OnFlingListener listener) {
    onGesturesManagerInteractionListener.onRemoveFlingListener(listener);
  }

  /**
   * Adds a callback that's invoked when the map is moved.
   *
   * @param listener The callback that's invoked when the map is moved.
   */
  public void addOnMoveListener(@NonNull OnMoveListener listener) {
    onGesturesManagerInteractionListener.onAddMoveListener(listener);
  }

  /**
   * Removes a callback that's invoked when the map is moved.
   *
   * @param listener The callback that's invoked when the map is moved.
   */
  public void removeOnMoveListener(@NonNull OnMoveListener listener) {
    onGesturesManagerInteractionListener.onRemoveMoveListener(listener);
  }

  /**
   * Adds a callback that's invoked when the map is rotated.
   *
   * @param listener The callback that's invoked when the map is rotated.
   */
  public void addOnRotateListener(@NonNull OnRotateListener listener) {
    onGesturesManagerInteractionListener.onAddRotateListener(listener);
  }

  /**
   * Removes a callback that's invoked when the map is rotated.
   *
   * @param listener The callback that's invoked when the map is rotated.
   */
  public void removeOnRotateListener(@NonNull OnRotateListener listener) {
    onGesturesManagerInteractionListener.onRemoveRotateListener(listener);
  }

  /**
   * Adds a callback that's invoked when the map is scaled.
   *
   * @param listener The callback that's invoked when the map is scaled.
   */
  public void addOnScaleListener(@NonNull OnScaleListener listener) {
    onGesturesManagerInteractionListener.onAddScaleListener(listener);
  }

  /**
   * Removes a callback that's invoked when the map is scaled.
   *
   * @param listener The callback that's invoked when the map is scaled.
   */
  public void removeOnScaleListener(@NonNull OnScaleListener listener) {
    onGesturesManagerInteractionListener.onRemoveScaleListener(listener);
  }

  /**
   * Adds a callback that's invoked when the map is tilted.
   *
   * @param listener The callback that's invoked when the map is tilted.
   */
  public void addOnShoveListener(@NonNull OnShoveListener listener) {
    onGesturesManagerInteractionListener.onAddShoveListener(listener);
  }

  /**
   * Remove a callback that's invoked when the map is tilted.
   *
   * @param listener The callback that's invoked when the map is tilted.
   */
  public void removeOnShoveListener(@NonNull OnShoveListener listener) {
    onGesturesManagerInteractionListener.onRemoveShoveListener(listener);
  }

  /**
   * Sets a custom {@link AndroidGesturesManager} to handle {@link android.view.MotionEvent}s
   * registered by the {@link MapView}.
   *
   * @param androidGesturesManager       Gestures manager that interprets gestures based on the motion events.
   * @param attachDefaultListeners       If true, pre-defined listeners will be attach
   *                                     to change map based on {@link AndroidGesturesManager} callbacks.
   * @param setDefaultMutuallyExclusives If true, pre-defined mutually exclusive gesture sets
   *                                     will be added to the passed gestures manager.
   * @see <a href="https://github.com/mapbox/mapbox-gestures-android">mapbox-gestures-android library</a>
   */
  public void setGesturesManager(@NonNull AndroidGesturesManager androidGesturesManager, boolean attachDefaultListeners,
                                 boolean setDefaultMutuallyExclusives) {
    onGesturesManagerInteractionListener.setGesturesManager(
      androidGesturesManager, attachDefaultListeners, setDefaultMutuallyExclusives);
  }

  /**
   * Get current {@link AndroidGesturesManager} that handles {@link android.view.MotionEvent}s
   * registered by the {@link MapView}
   *
   * @return Current gestures manager.
   */
  @NonNull
  public AndroidGesturesManager getGesturesManager() {
    return onGesturesManagerInteractionListener.getGesturesManager();
  }

  /**
   * Interrupts any ongoing gesture velocity animations.
   */
  public void cancelAllVelocityAnimations() {
    onGesturesManagerInteractionListener.cancelAllVelocityAnimations();
  }

  /**
   * Adds a callback that's invoked when the user clicks on the map view.
   *
   * @param listener The callback that's invoked when the user clicks on the map view.
   */
  public void addOnMapClickListener(@NonNull OnMapClickListener listener) {
    onGesturesManagerInteractionListener.onAddMapClickListener(listener);
  }

  /**
   * Removes a callback that's invoked when the user clicks on the map view.
   *
   * @param listener The callback that's invoked when the user clicks on the map view.
   */
  public void removeOnMapClickListener(@NonNull OnMapClickListener listener) {
    onGesturesManagerInteractionListener.onRemoveMapClickListener(listener);
  }

  /**
   * Adds a callback that's invoked when the user long clicks on the map view.
   *
   * @param listener The callback that's invoked when the user long clicks on the map view.
   */
  public void addOnMapLongClickListener(@NonNull OnMapLongClickListener listener) {
    onGesturesManagerInteractionListener.onAddMapLongClickListener(listener);
  }

  /**
   * Removes a callback that's invoked when the user long clicks on the map view.
   *
   * @param listener The callback that's invoked when the user long clicks on the map view.
   */
  public void removeOnMapLongClickListener(@NonNull OnMapLongClickListener listener) {
    onGesturesManagerInteractionListener.onRemoveMapLongClickListener(listener);
  }

  /**
   * Sets a callback that's invoked when the user clicks on an info window.
   *
   * @param listener The callback that's invoked when the user clicks on an info window.
   *                 To unset the callback, use null.
   */
  public void setOnInfoWindowClickListener(@Nullable OnInfoWindowClickListener listener) {
    annotationManager.getInfoWindowManager().setOnInfoWindowClickListener(listener);
  }

  /**
   * Return the InfoWindow click listener
   *
   * @return Current active InfoWindow Click Listener
   */
  @Nullable
  public OnInfoWindowClickListener getOnInfoWindowClickListener() {
    return annotationManager.getInfoWindowManager().getOnInfoWindowClickListener();
  }

  /**
   * Sets a callback that's invoked when a marker's info window is long pressed.
   *
   * @param listener The callback that's invoked when a marker's info window is long pressed. To unset the callback,
   *                 use null.
   */
  public void setOnInfoWindowLongClickListener(@Nullable OnInfoWindowLongClickListener
                                                 listener) {
    annotationManager.getInfoWindowManager().setOnInfoWindowLongClickListener(listener);
  }

  /**
   * Return the InfoWindow long click listener
   *
   * @return Current active InfoWindow long Click Listener
   */
  @Nullable
  public OnInfoWindowLongClickListener getOnInfoWindowLongClickListener() {
    return annotationManager.getInfoWindowManager().getOnInfoWindowLongClickListener();
  }

  /**
   * Set an callback to be invoked when an InfoWindow closes.
   *
   * @param listener callback invoked when an InfoWindow closes
   */
  public void setOnInfoWindowCloseListener(@Nullable OnInfoWindowCloseListener listener) {
    annotationManager.getInfoWindowManager().setOnInfoWindowCloseListener(listener);
  }

  /**
   * Return the InfoWindow close listener
   *
   * @return Current active InfoWindow Close Listener
   */
  @Nullable
  public OnInfoWindowCloseListener getOnInfoWindowCloseListener() {
    return annotationManager.getInfoWindowManager().getOnInfoWindowCloseListener();
  }

  //
  // Invalidate
  //

  /**
   * Takes a snapshot of the map.
   *
   * @param callback Callback method invoked when the snapshot is taken.
   */
  public void snapshot(@NonNull SnapshotReadyCallback callback) {
    if (!started) {
      return;
    }
    nativeMapView.addSnapshotCallback(callback);
  }

  /**
   * Queries the map for rendered features.
   * <p>
   * Returns an empty list if either the map or underlying render surface has been destroyed.
   * </p>
   *
   * @param coordinates the point to query
   * @param layerIds    optionally - only query these layers
   * @return the list of feature
   */
  @NonNull
  public List<Feature> queryRenderedFeatures(@NonNull PointF coordinates, @Nullable String... layerIds) {
    return nativeMapView.queryRenderedFeatures(coordinates, layerIds, null);
  }

  /**
   * Queries the map for rendered features
   * <p>
   * Returns an empty list if either the map or underlying render surface has been destroyed.
   * </p>
   *
   * @param coordinates the point to query
   * @param filter      filters the returned features with an expression
   * @param layerIds    optionally - only query these layers
   * @return the list of feature
   */
  @NonNull
  public List<Feature> queryRenderedFeatures(@NonNull PointF coordinates,
                                             @Nullable Expression filter,
                                             @Nullable String... layerIds) {
    return nativeMapView.queryRenderedFeatures(coordinates, layerIds, filter);
  }

  /**
   * Queries the map for rendered features
   * <p>
   * Returns an empty list if either the map or underlying render surface has been destroyed.
   * </p>
   *
   * @param coordinates the box to query
   * @param layerIds    optionally - only query these layers
   * @return the list of feature
   */
  @NonNull
  public List<Feature> queryRenderedFeatures(@NonNull RectF coordinates, @Nullable String... layerIds) {
    return nativeMapView.queryRenderedFeatures(coordinates, layerIds, null);
  }

  /**
   * Queries the map for rendered features
   * <p>
   * Returns an empty list if either the map or underlying render surface has been destroyed.
   * </p>
   *
   * @param coordinates the box to query
   * @param filter      filters the returned features with an expression
   * @param layerIds    optionally - only query these layers
   * @return the list of feature
   */
  @NonNull
  public List<Feature> queryRenderedFeatures(@NonNull RectF coordinates,
                                             @Nullable Expression filter,
                                             @Nullable String... layerIds) {
    return nativeMapView.queryRenderedFeatures(coordinates, layerIds, filter);
  }

  //
  // LocationComponent
  //

  void injectLocationComponent(LocationComponent locationComponent) {
    this.locationComponent = locationComponent;
  }

  void injectAnnotationManager(AnnotationManager annotationManager) {
    this.annotationManager = annotationManager.bind(this);
  }

  /**
   * Returns the {@link LocationComponent} that can be used to display user's location on the map.
   * <p>
   * Use {@link LocationComponent#activateLocationComponent(LocationComponentActivationOptions)} or
   * any overload to activate the component,
   * then, enable it with {@link LocationComponent#setLocationComponentEnabled(boolean)}.
   * <p>
   * You can customize the location icon and more with {@link org.maplibre.android.location.LocationComponentOptions}.
   *
   * @return the Location Component
   */
  @NonNull
  public LocationComponent getLocationComponent() {
    return locationComponent;
  }

  //
  // Interfaces
  //

  /**
   * Interface definition for a callback to be invoked when the map is flinged.
   *
   * @see MapLibreMap#addOnFlingListener(OnFlingListener)
   */
  public interface OnFlingListener {
    /**
     * Called when the map is flinged.
     */
    void onFling();
  }

  /**
   * Interface definition for a callback to be invoked when the map is moved.
   *
   * @see MapLibreMap#addOnMoveListener(OnMoveListener)
   */
  public interface OnMoveListener {
    void onMoveBegin(@NonNull MoveGestureDetector detector);

    void onMove(@NonNull MoveGestureDetector detector);

    void onMoveEnd(@NonNull MoveGestureDetector detector);
  }

  /**
   * Interface definition for a callback to be invoked when the map is rotated.
   *
   * @see MapLibreMap#addOnRotateListener(OnRotateListener)
   */
  public interface OnRotateListener {
    void onRotateBegin(@NonNull RotateGestureDetector detector);

    void onRotate(@NonNull RotateGestureDetector detector);

    void onRotateEnd(@NonNull RotateGestureDetector detector);
  }

  /**
   * Interface definition for a callback to be invoked when the map is scaled.
   *
   * @see MapLibreMap#addOnScaleListener(OnScaleListener)
   */
  public interface OnScaleListener {
    void onScaleBegin(@NonNull StandardScaleGestureDetector detector);

    void onScale(@NonNull StandardScaleGestureDetector detector);

    void onScaleEnd(@NonNull StandardScaleGestureDetector detector);
  }

  /**
   * Interface definition for a callback to be invoked when the map is tilted.
   *
   * @see MapLibreMap#addOnShoveListener(OnShoveListener)
   */
  public interface OnShoveListener {
    void onShoveBegin(@NonNull ShoveGestureDetector detector);

    void onShove(@NonNull ShoveGestureDetector detector);

    void onShoveEnd(@NonNull ShoveGestureDetector detector);
  }

  /**
   * Interface definition for a callback to be invoked for when the camera motion starts.
   */
  public interface OnCameraMoveStartedListener {
    int REASON_API_GESTURE = 1;
    int REASON_DEVELOPER_ANIMATION = 2;
    int REASON_API_ANIMATION = 3;

    /**
     * Called when the camera starts moving after it has been idle or when the reason for camera motion has changed.
     *
     * @param reason the reason for the camera change
     */
    void onCameraMoveStarted(int reason);
  }

  /**
   * Interface definition for a callback to be invoked for when the camera changes position.
   */
  public interface OnCameraMoveListener {
    /**
     * Called repeatedly as the camera continues to move after an onCameraMoveStarted call.
     * This may be called as often as once every frame and should not perform expensive operations.
     */
    void onCameraMove();
  }

  /**
   * Interface definition for a callback to be invoked for when the camera's motion has been stopped or when the camera
   * starts moving for a new reason.
   */
  public interface OnCameraMoveCanceledListener {
    /**
     * Called when the developer explicitly calls the cancelTransitions() method or if the reason for camera motion has
     * changed before the onCameraIdle had a chance to fire after the previous animation.
     * Do not update or animate the camera from within this method.
     */
    void onCameraMoveCanceled();
  }

  /**
   * Interface definition for a callback to be invoked for when camera movement has ended.
   */
  public interface OnCameraIdleListener {
    /**
     * Called when camera movement has ended.
     */
    void onCameraIdle();
  }

  /**
   * Interface definition for a callback to be invoked for when the compass is animating.
   */
  public interface OnCompassAnimationListener {
    /**
     * Called repeatedly as the compass continues to move after clicking on it.
     */
    void onCompassAnimation();

    /**
     * Called when compass animation has ended.
     */
    void onCompassAnimationFinished();
  }

  /**
   * Interface definition for a callback to be invoked when a frame is rendered to the map view.
   *
   * @see MapLibreMap#setOnFpsChangedListener(OnFpsChangedListener)
   */
  public interface OnFpsChangedListener {
    /**
     * Called for every frame rendered to the map view.
     *
     * @param fps The average number of frames rendered over the last second.
     */
    void onFpsChanged(double fps);
  }

  /**
   * Interface definition for a callback to be invoked when a user registers an listener that is
   * related to touch and click events.
   */
  interface OnGesturesManagerInteractionListener {

    void onAddMapClickListener(OnMapClickListener listener);

    void onRemoveMapClickListener(OnMapClickListener listener);

    void onAddMapLongClickListener(OnMapLongClickListener listener);

    void onRemoveMapLongClickListener(OnMapLongClickListener listener);

    void onAddFlingListener(OnFlingListener listener);

    void onRemoveFlingListener(OnFlingListener listener);

    void onAddMoveListener(OnMoveListener listener);

    void onRemoveMoveListener(OnMoveListener listener);

    void onAddRotateListener(OnRotateListener listener);

    void onRemoveRotateListener(OnRotateListener listener);

    void onAddScaleListener(OnScaleListener listener);

    void onRemoveScaleListener(OnScaleListener listener);

    void onAddShoveListener(OnShoveListener listener);

    void onRemoveShoveListener(OnShoveListener listener);

    AndroidGesturesManager getGesturesManager();

    void setGesturesManager(AndroidGesturesManager gesturesManager, boolean attachDefaultListeners,
                            boolean setDefaultMutuallyExclusives);

    void cancelAllVelocityAnimations();
  }

  /**
   * Interface definition for a callback to be invoked when the user clicks on the map view.
   *
   * @see MapLibreMap#addOnMapClickListener(OnMapClickListener)
   */
  public interface OnMapClickListener {
    /**
     * Called when the user clicks on the map view.
     *
     * @param point The projected map coordinate the user clicked on.
     * @return True if this click should be consumed and not passed further to other listeners registered afterwards,
     * false otherwise.
     */
    boolean onMapClick(@NonNull LatLng point);
  }

  /**
   * Interface definition for a callback to be invoked when the user long clicks on the map view.
   *
   * @see MapLibreMap#addOnMapLongClickListener(OnMapLongClickListener)
   */
  public interface OnMapLongClickListener {
    /**
     * Called when the user long clicks on the map view.
     *
     * @param point The projected map coordinate the user long clicked on.
     * @return True if this click should be consumed and not passed further to other listeners registered afterwards,
     * false otherwise.
     */
    boolean onMapLongClick(@NonNull LatLng point);
  }

  /**
   * Interface definition for a callback to be invoked when the user clicks on a marker.
   *
   * @see MapLibreMap#setOnMarkerClickListener(OnMarkerClickListener)
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public interface OnMarkerClickListener {
    /**
     * Called when the user clicks on a marker.
     *
     * @param marker The marker the user clicked on.
     * @return If true the listener has consumed the event and the info window will not be shown.
     */
    boolean onMarkerClick(@NonNull Marker marker);
  }

  /**
   * Interface definition for a callback to be invoked when the user clicks on a polygon.
   *
   * @see MapLibreMap#setOnPolygonClickListener(OnPolygonClickListener)
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public interface OnPolygonClickListener {
    /**
     * Called when the user clicks on a polygon.
     *
     * @param polygon The polygon the user clicked on.
     */
    void onPolygonClick(@NonNull Polygon polygon);
  }

  /**
   * Interface definition for a callback to be invoked when the user clicks on a polyline.
   *
   * @see MapLibreMap#setOnPolylineClickListener(OnPolylineClickListener)
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public interface OnPolylineClickListener {
    /**
     * Called when the user clicks on a polyline.
     *
     * @param polyline The polyline the user clicked on.
     */
    void onPolylineClick(@NonNull Polyline polyline);
  }

  /**
   * Interface definition for a callback to be invoked when the user clicks on an info window.
   *
   * @see MapLibreMap#setOnInfoWindowClickListener(OnInfoWindowClickListener)
   */
  public interface OnInfoWindowClickListener {
    /**
     * Called when the user clicks on an info window.
     *
     * @param marker The marker of the info window the user clicked on.
     * @return If true the listener has consumed the event and the info window will not be closed.
     */
    boolean onInfoWindowClick(@NonNull Marker marker);
  }

  /**
   * Interface definition for a callback to be invoked when the user long presses on a marker's info window.
   *
   * @see MapLibreMap#setOnInfoWindowClickListener(OnInfoWindowClickListener)
   */
  public interface OnInfoWindowLongClickListener {

    /**
     * Called when the user makes a long-press gesture on the marker's info window.
     *
     * @param marker The marker were the info window is attached to
     */
    void onInfoWindowLongClick(@NonNull Marker marker);
  }

  /**
   * Interface definition for a callback to be invoked when a marker's info window is closed.
   *
   * @see MapLibreMap#setOnInfoWindowCloseListener(OnInfoWindowCloseListener)
   */
  public interface OnInfoWindowCloseListener {

    /**
     * Called when the marker's info window is closed.
     *
     * @param marker The marker of the info window that was closed.
     */
    void onInfoWindowClose(@NonNull Marker marker);
  }

  /**
   * Interface definition for a callback to be invoked when an info window will be shown.
   *
   * @see MapLibreMap#setInfoWindowAdapter(InfoWindowAdapter)
   * @deprecated As of 7.0.0,
   * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
   * MapLibre Annotation Plugin</a> instead
   */
  @Deprecated
  public interface InfoWindowAdapter {
    /**
     * Called when an info window will be shown as a result of a marker click.
     *
     * @param marker The marker the user clicked on.
     * @return View to be shown as a info window. If null is returned the default
     * info window will be shown.
     */
    @Nullable
    View getInfoWindow(@NonNull Marker marker);
  }

  /**
   * Interface definition for a callback to be invoked when a task is complete or cancelled.
   */
  public interface CancelableCallback {
    /**
     * Invoked when a task is cancelled.
     */
    void onCancel();

    /**
     * Invoked when a task is complete.
     */
    void onFinish();
  }

  /**
   * Interface definition for a callback to be invoked when the snapshot has been taken.
   */
  public interface SnapshotReadyCallback {
    /**
     * Invoked when the snapshot has been taken.
     *
     * @param snapshot the snapshot bitmap
     */
    void onSnapshotReady(@NonNull Bitmap snapshot);
  }

  /**
   * Internal use.
   */
  public interface OnDeveloperAnimationListener {

    /**
     * Notifies listener when a developer invoked animation is about to start.
     */
    void onDeveloperAnimationStarted();
  }

  //
  // Used for instrumentation testing
  //
  @NonNull
  Transform getTransform() {
    return transform;
  }

  private void notifyDeveloperAnimationListeners() {
    for (OnDeveloperAnimationListener listener : developerAnimationStartedListeners) {
      listener.onDeveloperAnimationStarted();
    }
  }
}
