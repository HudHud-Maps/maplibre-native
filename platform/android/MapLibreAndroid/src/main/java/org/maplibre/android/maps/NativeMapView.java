package org.maplibre.android.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.maplibre.geojson.Feature;
import org.maplibre.geojson.Geometry;
import org.maplibre.android.LibraryLoader;
import org.maplibre.android.MapStrictMode;
import org.maplibre.android.annotations.Icon;
import org.maplibre.android.annotations.Marker;
import org.maplibre.android.annotations.Polygon;
import org.maplibre.android.annotations.Polyline;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.exceptions.CalledFromWorkerThreadException;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.geometry.LatLngBounds;
import org.maplibre.android.geometry.ProjectedMeters;
import org.maplibre.android.log.Logger;
import org.maplibre.android.maps.renderer.MapRenderer;
import org.maplibre.android.storage.FileSource;
import org.maplibre.android.style.expressions.Expression;
import org.maplibre.android.style.layers.CannotAddLayerException;
import org.maplibre.android.style.layers.Layer;
import org.maplibre.android.style.layers.TransitionOptions;
import org.maplibre.android.style.light.Light;
import org.maplibre.android.style.sources.CannotAddSourceException;
import org.maplibre.android.style.sources.Source;
import org.maplibre.android.utils.BitmapUtils;
import org.maplibre.android.tile.TileOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Class that wraps the native methods for convenience
final class NativeMapView implements NativeMap {

  private static final String TAG = "Mbgl-NativeMapView";

  // Hold a reference to prevent it from being GC'd as long as it's used on the native side
  private final FileSource fileSource;

  // Used to schedule work on the MapRenderer Thread
  private final MapRenderer mapRenderer;

  // Used to validate if methods are called from the correct thread
  @NonNull
  private final Thread thread;

  // Used for view callbacks
  @Nullable
  private ViewCallback viewCallback;

  // Used for map change callbacks
  @Nullable
  private StateCallback stateCallback;

  // Device density
  private final float pixelRatio;

  // Flag to indicate destroy was called
  private boolean destroyed = false;

  // Cached to enable lazily set padding.
  // Whenever an animation is schedule, this value is cleared and the source of truth becomes the core transform state.
  private double[] edgeInsets;

  // Holds the pointer to JNI NativeMapView
  @Keep
  private long nativePtr = 0;

  // Listener invoked to return a bitmap of the map
  private MapLibreMap.SnapshotReadyCallback snapshotReadyCallback;

  static {
    LibraryLoader.load();
  }

  //
  // Constructors
  //

  public NativeMapView(@NonNull final Context context,
                       final ViewCallback viewCallback, final StateCallback stateCallback,
                       final MapRenderer mapRenderer) {
    this(context, new NativeMapOptions(context.getResources().getDisplayMetrics().density, false),
            viewCallback, stateCallback, mapRenderer);
  }

  public NativeMapView(@NonNull final Context context, final MapLibreMapOptions options,
                       final ViewCallback viewCallback, final StateCallback stateCallback,
                       final MapRenderer mapRenderer) {
    this(context, new NativeMapOptions(options), viewCallback, stateCallback, mapRenderer);
  }

  public NativeMapView(@NonNull final Context context, final NativeMapOptions nativeOptions,
                       final ViewCallback viewCallback, final StateCallback stateCallback,
                       final MapRenderer mapRenderer) {
    this.mapRenderer = mapRenderer;
    this.viewCallback = viewCallback;
    this.fileSource = FileSource.getInstance(context);
    this.pixelRatio = nativeOptions.pixelRatio();
    this.thread = Thread.currentThread();
    this.stateCallback = stateCallback;
    nativeInitialize(this, fileSource, mapRenderer, nativeOptions);
  }

  //
  // Methods
  //

  private boolean checkState(String callingMethod) {
    // validate if invocation has occurred on the main thread
    if (thread != Thread.currentThread()) {
      throw new CalledFromWorkerThreadException(
        String.format(
          "Map interactions should happen on the UI thread. Method invoked from wrong thread is %s.",
          callingMethod)
      );
    }

    // validate if map has already been destroyed
    if (destroyed && !TextUtils.isEmpty(callingMethod)) {
      String message = String.format(
        "You're calling `%s` after the `MapView` was destroyed, were you invoking it after `onDestroy()`?",
        callingMethod);
      Logger.e(TAG, message);

      MapStrictMode.strictModeViolation(message);
    }
    return destroyed;
  }

  @Override
  public void destroy() {
    destroyed = true;
    viewCallback = null;
    nativeDestroy();
  }

  @Override
  public void resizeView(int width, int height) {
    if (checkState("resizeView")) {
      return;
    }
    width = (int) Math.ceil(width / pixelRatio);
    height = (int) Math.ceil(height / pixelRatio);

    if (width < 0) {
      Logger.e(TAG, String.format("Device returned a negative width size, "
              + "setting value to 0 instead of %s", width)
      );
      width = 0;
    }

    if (height < 0) {
      Logger.e(TAG, String.format("Device returned a negative height size, "
              + "setting value to 0 instead of %s", height)
      );
      height = 0;
    }

    if (width > 65535) {
      // we have seen edge cases where devices return incorrect values #6111
      Logger.e(TAG, String.format("Device returned an out of range width size, "
        + "capping value at 65535 instead of %s", width)
      );
      width = 65535;
    }

    if (height > 65535) {
      // we have seen edge cases where devices return incorrect values #6111
      Logger.e(TAG, String.format("Device returned an out of range height size, "
        + "capping value at 65535 instead of %s", height)
      );
      height = 65535;
    }

    nativeResizeView(width, height);
  }

  @Override
  public void setStyleUri(String url) {
    if (checkState("setStyleUri")) {
      return;
    }
    nativeSetStyleUrl(url);
  }

  @Override
  @NonNull
  public String getStyleUri() {
    if (checkState("getStyleUri")) {
      return "";
    }
    return nativeGetStyleUrl();
  }

  @Override
  public void setStyleJson(String newStyleJson) {
    if (checkState("setStyleJson")) {
      return;
    }
    nativeSetStyleJson(newStyleJson);
  }

  @Override
  @NonNull
  public String getStyleJson() {
    if (checkState("getStyleJson")) {
      return "";
    }
    return nativeGetStyleJson();
  }

  @Override
  public void setLatLngBounds(LatLngBounds latLngBounds) {
    if (checkState("setLatLngBounds")) {
      return;
    }
    nativeSetLatLngBounds(latLngBounds);
  }

  @Override
  public void cancelTransitions() {
    if (checkState("cancelTransitions")) {
      return;
    }
    nativeCancelTransitions();
  }

  @Override
  public void setGestureInProgress(boolean inProgress) {
    if (checkState("setGestureInProgress")) {
      return;
    }
    nativeSetGestureInProgress(inProgress);
  }

  @Override
  public void moveBy(double dx, double dy, long duration) {
    if (checkState("moveBy")) {
      return;
    }

    try {
      nativeMoveBy(dx / pixelRatio, dy / pixelRatio, duration);
    } catch (java.lang.Error error) {
      // workaround for latitude must not be NaN issue
      // which is thrown when gl-native can't convert a screen coordinate to location
      Logger.d(TAG, "Error when executing NativeMapView#moveBy", error);
    }
  }

  @Override
  public void setLatLng(@NonNull LatLng latLng, long duration) {
    if (checkState("setLatLng")) {
      return;
    }
    nativeSetLatLng(latLng.getLatitude(), latLng.getLongitude(),
      getAnimationPaddingAndClearCachedInsets(null), duration);
  }

  @Override
  public LatLng getLatLng() {
    if (checkState("")) {
      return new LatLng();
    }
    return nativeGetLatLng();
  }

  @Override
  public CameraPosition getCameraForLatLngBounds(
          @NonNull LatLngBounds bounds,
          int[] padding,
          double bearing,
          double tilt
  ) {
    if (checkState("getCameraForLatLngBounds")) {
      return null;
    }
    // Note that we have to juggle things a bit to match the ordering of arguments
    // to match the NativeMapView C++ interface.
    return nativeGetCameraForLatLngBounds(
      bounds,
      padding[1] / pixelRatio,
      padding[0] / pixelRatio,
      padding[3] / pixelRatio,
      padding[2] / pixelRatio,
      bearing,
      tilt
    );
  }

  @Override
  public CameraPosition getCameraForGeometry(@NonNull Geometry geometry, int[] padding, double bearing, double tilt) {
    if (checkState("getCameraForGeometry")) {
      return null;
    }
    return nativeGetCameraForGeometry(
      geometry,
      padding[1] / pixelRatio,
      padding[0] / pixelRatio,
      padding[3] / pixelRatio,
      padding[2] / pixelRatio,
      bearing,
      tilt
    );
  }

  @Override
  public void resetPosition() {
    if (checkState("resetPosition")) {
      return;
    }
    nativeResetPosition();
  }

  @Override
  public double getPitch() {
    if (checkState("getPitch")) {
      return 0;
    }
    return nativeGetPitch();
  }

  @Override
  public void setPitch(double pitch, long duration) {
    if (checkState("setPitch")) {
      return;
    }
    nativeSetPitch(pitch, duration);
  }

  @Override
  public void setZoom(double zoom, @NonNull PointF focalPoint, long duration) {
    if (checkState("setZoom")) {
      return;
    }
    nativeSetZoom(zoom, focalPoint.x / pixelRatio, focalPoint.y / pixelRatio, duration);
  }

  @Override
  public double getZoom() {
    if (checkState("getZoom")) {
      return 0;
    }
    return nativeGetZoom();
  }

  @Override
  public void resetZoom() {
    if (checkState("resetZoom")) {
      return;
    }
    nativeResetZoom();
  }

  @Override
  public void setMinZoom(double zoom) {
    if (checkState("setMinZoom")) {
      return;
    }
    nativeSetMinZoom(zoom);
  }

  @Override
  public double getMinZoom() {
    if (checkState("getMinZoom")) {
      return 0;
    }
    return nativeGetMinZoom();
  }

  @Override
  public void setMaxZoom(double zoom) {
    if (checkState("setMaxZoom")) {
      return;
    }
    nativeSetMaxZoom(zoom);
  }

  @Override
  public double getMaxZoom() {
    if (checkState("getMaxZoom")) {
      return 0;
    }
    return nativeGetMaxZoom();
  }

  @Override
  public void setMinPitch(double pitch) {
    if (checkState("setMinPitch")) {
      return;
    }
    nativeSetMinPitch(pitch);
  }

  @Override
  public double getMinPitch() {
    if (checkState("getMinPitch")) {
      return 0;
    }
    return nativeGetMinPitch();
  }

  @Override
  public void setMaxPitch(double pitch) {
    if (checkState("setMaxPitch")) {
      return;
    }
    nativeSetMaxPitch(pitch);
  }

  @Override
  public double getMaxPitch() {
    if (checkState("getMaxPitch")) {
      return 0;
    }
    return nativeGetMaxPitch();
  }

  @Override
  public void rotateBy(double sx, double sy, double ex, double ey,
                       long duration) {
    if (checkState("rotateBy")) {
      return;
    }
    nativeRotateBy(sx / pixelRatio, sy / pixelRatio, ex, ey, duration);
  }

  @Override
  public void setContentPadding(double[] padding) {
    if (checkState("setContentPadding")) {
      return;
    }
    this.edgeInsets = padding;
  }

  @Override
  public double[] getContentPadding() {
    if (checkState("getContentPadding")) {
      return new double[] {0, 0, 0, 0};
    }
    // if cached insets are not applied yet, return them, otherwise, get the padding from the camera
    return edgeInsets != null ? edgeInsets : getCameraPosition().padding;
  }

  @Override
  public void setBearing(double degrees, long duration) {
    if (checkState("setBearing")) {
      return;
    }
    nativeSetBearing(degrees, duration);
  }

  @Override
  public void setBearing(double degrees, double fx, double fy, long duration) {
    if (checkState("setBearing")) {
      return;
    }
    nativeSetBearingXY(degrees, fx / pixelRatio, fy / pixelRatio, duration);
  }

  @Override
  public double getBearing() {
    if (checkState("getBearing")) {
      return 0;
    }
    return nativeGetBearing();
  }

  @Override
  public void resetNorth() {
    if (checkState("resetNorth")) {
      return;
    }
    nativeResetNorth();
  }

  @Override
  public long addMarker(Marker marker) {
    if (checkState("addMarker")) {
      return 0;
    }
    Marker[] markers = {marker};
    return nativeAddMarkers(markers)[0];
  }

  @Override
  @NonNull
  public long[] addMarkers(@NonNull List<Marker> markers) {
    if (checkState("addMarkers")) {
      return new long[] {};
    }
    return nativeAddMarkers(markers.toArray(new Marker[markers.size()]));
  }

  @Override
  public long addPolyline(Polyline polyline) {
    if (checkState("addPolyline")) {
      return 0;
    }
    Polyline[] polylines = {polyline};
    return nativeAddPolylines(polylines)[0];
  }

  @Override
  @NonNull
  public long[] addPolylines(@NonNull List<Polyline> polylines) {
    if (checkState("addPolylines")) {
      return new long[] {};
    }
    return nativeAddPolylines(polylines.toArray(new Polyline[polylines.size()]));
  }

  @Override
  public long addPolygon(Polygon polygon) {
    if (checkState("addPolygon")) {
      return 0;
    }
    Polygon[] polygons = {polygon};
    return nativeAddPolygons(polygons)[0];
  }

  @Override
  @NonNull
  public long[] addPolygons(@NonNull List<Polygon> polygons) {
    if (checkState("addPolygons")) {
      return new long[] {};
    }
    return nativeAddPolygons(polygons.toArray(new Polygon[polygons.size()]));
  }

  @Override
  public void updateMarker(@NonNull Marker marker) {
    if (checkState("updateMarker")) {
      return;
    }
    LatLng position = marker.getPosition();
    Icon icon = marker.getIcon();
    nativeUpdateMarker(marker.getId(), position.getLatitude(), position.getLongitude(), icon.getId());
  }

  @Override
  public void updatePolygon(@NonNull Polygon polygon) {
    if (checkState("updatePolygon")) {
      return;
    }
    nativeUpdatePolygon(polygon.getId(), polygon);
  }

  @Override
  public void updatePolyline(@NonNull Polyline polyline) {
    if (checkState("updatePolyline")) {
      return;
    }
    nativeUpdatePolyline(polyline.getId(), polyline);
  }

  @Override
  public void removeAnnotation(long id) {
    if (checkState("removeAnnotation")) {
      return;
    }
    long[] ids = {id};
    removeAnnotations(ids);
  }

  @Override
  public void removeAnnotations(long[] ids) {
    if (checkState("removeAnnotations")) {
      return;
    }
    nativeRemoveAnnotations(ids);
  }

  @Override
  @NonNull
  public long[] queryPointAnnotations(RectF rect) {
    if (checkState("queryPointAnnotations")) {
      return new long[] {};
    }
    return nativeQueryPointAnnotations(rect);
  }

  @Override
  @NonNull
  public long[] queryShapeAnnotations(RectF rectF) {
    if (checkState("queryShapeAnnotations")) {
      return new long[] {};
    }
    return nativeQueryShapeAnnotations(rectF);
  }

  @Override
  public void addAnnotationIcon(String symbol, int width, int height, float scale, byte[] pixels) {
    if (checkState("addAnnotationIcon")) {
      return;
    }
    nativeAddAnnotationIcon(symbol, width, height, scale, pixels);
  }

  @Override
  public void removeAnnotationIcon(String symbol) {
    if (checkState("removeAnnotationIcon")) {
      return;
    }
    nativeRemoveAnnotationIcon(symbol);
  }

  @Override
  public void setVisibleCoordinateBounds(LatLng[] coordinates, RectF padding, double direction, long duration) {
    if (checkState("setVisibleCoordinateBounds")) {
      return;
    }
    nativeSetVisibleCoordinateBounds(coordinates, padding, direction, duration);
  }

  @Override
  public void onLowMemory() {
    if (checkState("onLowMemory")) {
      return;
    }
    nativeOnLowMemory();
  }

  @Override
  public void setDebug(boolean debug) {
    if (checkState("setDebug")) {
      return;
    }
    nativeSetDebug(debug);
  }

  @Override
  public boolean getDebug() {
    if (checkState("getDebug")) {
      return false;
    }
    return nativeGetDebug();
  }

  @Override
  public String[] getActionJournalLogFiles() {
    if (checkState("getActionJournalLogFiles")) {
      return null;
    }
    return nativeGetActionJournalLogFiles();
  }

  @Override
  public String[] getActionJournalLog() {
    if (checkState("getActionJournalLog")) {
      return null;
    }
    return nativeGetActionJournalLog();
  }

  @Override
  public void clearActionJournalLog() {
    if (checkState("clearActionJournalLog")) {
      return;
    }
    nativeClearActionJournalLog();
  }

  @Override
  public boolean isFullyLoaded() {
    if (checkState("isFullyLoaded")) {
      return false;
    }
    return nativeIsFullyLoaded();
  }

  @Override
  public void setReachability(boolean status) {
    if (checkState("setReachability")) {
      return;
    }
    nativeSetReachability(status);
  }

  @Override
  public double getMetersPerPixelAtLatitude(double lat) {
    if (checkState("getMetersPerPixelAtLatitude")) {
      return 0;
    }
    return nativeGetMetersPerPixelAtLatitude(lat, getZoom());
  }

  @Override
  public ProjectedMeters projectedMetersForLatLng(@NonNull LatLng latLng) {
    if (checkState("projectedMetersForLatLng")) {
      return null;
    }
    return nativeProjectedMetersForLatLng(latLng.getLatitude(), latLng.getLongitude());
  }

  @Override
  public LatLng latLngForProjectedMeters(@NonNull ProjectedMeters projectedMeters) {
    if (checkState("latLngForProjectedMeters")) {
      return new LatLng();
    }
    return nativeLatLngForProjectedMeters(projectedMeters.getNorthing(),
      projectedMeters.getEasting());
  }

  @Override
  @NonNull
  public PointF pixelForLatLng(@NonNull LatLng latLng) {
    if (checkState("pixelForLatLng")) {
      return new PointF();
    }
    PointF pointF = nativePixelForLatLng(latLng.getLatitude(), latLng.getLongitude());
    pointF.set(pointF.x * pixelRatio, pointF.y * pixelRatio);
    return pointF;
  }

  @Override
  public void pixelsForLatLngs(@NonNull double[] input, @NonNull double[] output) {
    if (!checkState("pixelsForLatLngs")) {
      nativePixelsForLatLngs(input, output, pixelRatio);
    }
  }

  @Override
  public void getVisibleCoordinateBounds(@NonNull double[] output) {
    if (!checkState("getVisibleCoordinateBounds")) {
      nativeGetVisibleCoordinateBounds(output);
    }
  }

  @Override
  public LatLng latLngForPixel(@NonNull PointF pixel) {
    if (checkState("latLngForPixel")) {
      return new LatLng();
    }
    return nativeLatLngForPixel(pixel.x / pixelRatio, pixel.y / pixelRatio);
  }

  @Override
  public void latLngsForPixels(@NonNull double[] input, @NonNull double[] output) {
    if (!checkState("latLngsForPixels")) {
      nativeLatLngsForPixels(input, output, pixelRatio);
    }
  }


  @Override
  public double getTopOffsetPixelsForAnnotationSymbol(String symbolName) {
    if (checkState("getTopOffsetPixelsForAnnotationSymbol")) {
      return 0;
    }
    return nativeGetTopOffsetPixelsForAnnotationSymbol(symbolName);
  }

  @Override
  public void jumpTo(@NonNull LatLng center, double zoom, double pitch, double angle, double[] padding) {
    if (checkState("jumpTo")) {
      return;
    }
    nativeJumpTo(angle, center.getLatitude(), center.getLongitude(), pitch, zoom,
      getAnimationPaddingAndClearCachedInsets(padding));
  }

  @Override
  public void easeTo(@NonNull LatLng center, double zoom, double angle, double pitch, double[] padding, long duration,
                     boolean easingInterpolator) {
    if (checkState("easeTo")) {
      return;
    }
    nativeEaseTo(angle, center.getLatitude(), center.getLongitude(), duration, pitch, zoom,
      getAnimationPaddingAndClearCachedInsets(padding), easingInterpolator);
  }

  @Override
  public void flyTo(@NonNull LatLng center, double zoom, double angle, double pitch, double[] padding, long duration) {
    if (checkState("flyTo")) {
      return;
    }
    nativeFlyTo(angle, center.getLatitude(), center.getLongitude(), duration, pitch, zoom,
      getAnimationPaddingAndClearCachedInsets(padding));
  }

  @Override
  @NonNull
  public CameraPosition getCameraPosition() {
    if (checkState("getCameraValues")) {
      return new CameraPosition.Builder().build();
    }
    if (edgeInsets != null) {
      return new CameraPosition.Builder(nativeGetCameraPosition()).padding(edgeInsets).build();
    } else {
      return nativeGetCameraPosition();
    }
  }

  @Override
  public void setPrefetchTiles(boolean enable) {
    if (checkState("setPrefetchTiles")) {
      return;
    }
    nativeSetPrefetchTiles(enable);
  }

  @Override
  public boolean getPrefetchTiles() {
    if (checkState("getPrefetchTiles")) {
      return false;
    }
    return nativeGetPrefetchTiles();
  }

  @Override
  public void setPrefetchZoomDelta(@IntRange(from = 0) int delta) {
    if (checkState("nativeSetPrefetchZoomDelta")) {
      return;
    }
    nativeSetPrefetchZoomDelta(delta);
  }

  @Override
  @IntRange(from = 0)
  public int getPrefetchZoomDelta() {
    if (checkState("nativeGetPrefetchZoomDelta")) {
      return 0;
    }
    return nativeGetPrefetchZoomDelta();
  }

  @Override
  public void setTileCacheEnabled(boolean enabled) {
    if (checkState("setTileCacheEnabled")) {
      return;
    }
    nativeSetTileCacheEnabled(enabled);
  }

  @Override
  public boolean getTileCacheEnabled() {
    if (checkState("getTileCacheEnabled")) {
      return false;
    }
    return nativeGetTileCacheEnabled();
  }

  @Override
  public void setTileLodMinRadius(double radius) {
    if (checkState("setTileLodMinRadius")) {
      return;
    }
    nativeSetTileLodMinRadius(radius);
  }

  @Override
  public double getTileLodMinRadius() {
    if (checkState("getTileLodMinRadius")) {
      return 0;
    }
    return nativeGetTileLodMinRadius();
  }

  @Override
  public void setTileLodScale(double scale) {
    if (checkState("setTileLodScale")) {
      return;
    }
    nativeSetTileLodScale(scale);
  }

  @Override
  public double getTileLodScale() {
    if (checkState("getTileLodScale")) {
      return 0;
    }
    return nativeGetTileLodScale();
  }

  @Override
  public void setTileLodPitchThreshold(double threshold) {
    if (checkState("setTileLodPitchThreshold")) {
      return;
    }
    nativeSetTileLodPitchThreshold(threshold);
  }

  @Override
  public double getTileLodPitchThreshold() {
    if (checkState("getTileLodPitchThreshold")) {
      return 0;
    }
    return nativeGetTileLodPitchThreshold();
  }

  @Override
  public void setTileLodZoomShift(double shift) {
    if (checkState("setTileLodZoomShift")) {
      return;
    }
    nativeSetTileLodZoomShift(shift);
  }

  @Override
  public double getTileLodZoomShift() {
    if (checkState("getTileLodZoomShift")) {
      return 0;
    }
    return nativeGetTileLodZoomShift();
  }
  // Runtime style Api

  @Override
  public void setTransitionOptions(@NonNull TransitionOptions transitionOptions) {
    nativeSetTransitionOptions(transitionOptions);
  }

  @NonNull
  @Override
  public TransitionOptions getTransitionOptions() {
    return nativeGetTransitionOptions();
  }

  @Override
  @NonNull
  public List<Layer> getLayers() {
    if (checkState("getLayers")) {
      return new ArrayList<>();
    }
    return Arrays.asList(nativeGetLayers());
  }

  @Override
  public Layer getLayer(String layerId) {
    if (checkState("getLayer")) {
      return null;
    }
    return nativeGetLayer(layerId);
  }

  @Override
  public void addLayer(@NonNull Layer layer) {
    if (checkState("addLayer")) {
      return;
    }
    nativeAddLayer(layer.getNativePtr(), null);
  }

  @Override
  public void addLayerBelow(@NonNull Layer layer, @NonNull String below) {
    if (checkState("addLayerBelow")) {
      return;
    }
    nativeAddLayer(layer.getNativePtr(), below);
  }

  @Override
  public void addLayerAbove(@NonNull Layer layer, @NonNull String above) {
    if (checkState("addLayerAbove")) {
      return;
    }
    nativeAddLayerAbove(layer.getNativePtr(), above);
  }

  @Override
  public void addLayerAt(@NonNull Layer layer, @IntRange(from = 0) int index) {
    if (checkState("addLayerAt")) {
      return;
    }
    nativeAddLayerAt(layer.getNativePtr(), index);
  }

  @Override
  public boolean removeLayer(@NonNull String layerId) {
    if (checkState("removeLayer")) {
      return false;
    }

    Layer layer = getLayer(layerId);
    if (layer != null) {
      return removeLayer(layer);
    }
    return false;
  }


  @Override
  public boolean removeLayer(@NonNull Layer layer) {
    if (checkState("removeLayer")) {
      return false;
    }
    return nativeRemoveLayer(layer.getNativePtr());
  }

  @Override
  public boolean removeLayerAt(@IntRange(from = 0) int index) {
    if (checkState("removeLayerAt")) {
      return false;
    }
    return nativeRemoveLayerAt(index);
  }

  @Override
  @NonNull
  public List<Source> getSources() {
    if (checkState("getSources")) {
      return new ArrayList<>();
    }
    return Arrays.asList(nativeGetSources());
  }

  @Override
  public Source getSource(@NonNull String sourceId) {
    if (checkState("getSource")) {
      return null;
    }
    return nativeGetSource(sourceId);
  }

  @Override
  public void addSource(@NonNull Source source) {
    if (checkState("addSource")) {
      return;
    }
    nativeAddSource(source, source.getNativePtr());
  }

  @Override
  public boolean removeSource(@NonNull String sourceId) {
    if (checkState("removeSource")) {
      return false;
    }
    Source source = getSource(sourceId);
    if (source != null) {
      return removeSource(source);
    }
    return false;
  }

  @Override
  public boolean removeSource(@NonNull Source source) {
    if (checkState("removeSource")) {
      return false;
    }
    return nativeRemoveSource(source, source.getNativePtr());
  }

  @Override
  public void addImages(@NonNull Image[] images) {
    if (checkState("addImages")) {
      return;
    }
    nativeAddImages(images);
  }

  @Override
  public void removeImage(String name) {
    if (checkState("removeImage")) {
      return;
    }
    nativeRemoveImage(name);
  }

  @Override
  public Bitmap getImage(String name) {
    if (checkState("getImage")) {
      return null;
    }
    return nativeGetImage(name);
  }

  // Feature querying

  @Override
  @NonNull
  public List<Feature> queryRenderedFeatures(@NonNull PointF coordinates,
                                             @Nullable String[] layerIds,
                                             @Nullable Expression filter) {
    if (checkState("queryRenderedFeatures")) {
      return new ArrayList<>();
    }
    Feature[] features = nativeQueryRenderedFeaturesForPoint(coordinates.x / pixelRatio,
      coordinates.y / pixelRatio, layerIds, filter != null ? filter.toArray() : null);
    return features != null ? Arrays.asList(features) : new ArrayList<Feature>();
  }

  @Override
  @NonNull
  public List<Feature> queryRenderedFeatures(@NonNull RectF coordinates,
                                             @Nullable String[] layerIds,
                                             @Nullable Expression filter) {
    if (checkState("queryRenderedFeatures")) {
      return new ArrayList<>();
    }
    Feature[] features = nativeQueryRenderedFeaturesForBox(
      coordinates.left / pixelRatio,
      coordinates.top / pixelRatio,
      coordinates.right / pixelRatio,
      coordinates.bottom / pixelRatio,
      layerIds,
      filter != null ? filter.toArray() : null);
    return features != null ? Arrays.asList(features) : new ArrayList<Feature>();
  }

  @Override
  public void setApiBaseUrl(String baseUrl) {
    if (checkState("setApiBaseUrl")) {
      return;
    }
    fileSource.setApiBaseUrl(baseUrl);
  }

  @Override
  public Light getLight() {
    if (checkState("getLight")) {
      return null;
    }
    return nativeGetLight();
  }

  @Override
  public float getPixelRatio() {
    return pixelRatio;
  }

  @Override
  public void triggerRepaint() {
    nativeTriggerRepaint();
  }

  @Override
  public boolean isRenderingStatsViewEnabled() {
    return nativeIsRenderingStatsViewEnabled();
  }

  @Override
  public void enableRenderingStatsView(boolean value) {
    nativeEnableRenderingStatsView(value);
  }

  @Override
  public void toggleTransform() {
    nativeToggleTransform();
  }

  @Override
  public void setSwapBehaviorFlush(boolean flush) {
    mapRenderer.setSwapBehaviorFlush(flush);
  }

  @NonNull
  @Override
  public RectF getDensityDependantRectangle(final RectF rectangle) {
    return new RectF(
      rectangle.left / pixelRatio,
      rectangle.top / pixelRatio,
      rectangle.right / pixelRatio,
      rectangle.bottom / pixelRatio
    );
  }

  //
  // Callbacks
  //

  @Keep
  private void onCameraWillChange(boolean animated) {
    if (stateCallback != null) {
      stateCallback.onCameraWillChange(animated);
    }
  }

  @Keep
  private void onCameraIsChanging() {
    if (stateCallback != null) {
      stateCallback.onCameraIsChanging();
    }
  }

  @Keep
  private void onCameraDidChange(boolean animated) {
    if (stateCallback != null) {
      stateCallback.onCameraDidChange(animated);
    }
  }

  @Keep
  private void onWillStartLoadingMap() {
    if (stateCallback != null) {
      stateCallback.onWillStartLoadingMap();
    }
  }

  @Keep
  private void onDidFinishLoadingMap() {
    if (stateCallback != null) {
      stateCallback.onDidFinishLoadingMap();
    }
  }

  @Keep
  private void onDidFailLoadingMap(String error) {
    if (stateCallback != null) {
      stateCallback.onDidFailLoadingMap(error);
    }
  }

  @Keep
  private void onWillStartRenderingFrame() {
    if (stateCallback != null) {
      stateCallback.onWillStartRenderingFrame();
    }
  }

  @Keep
  private void onDidFinishRenderingFrame(boolean fully, RenderingStats stats) {
    if (stateCallback != null) {
      stateCallback.onDidFinishRenderingFrame(fully, stats);
    }
  }

  @Keep
  private void onWillStartRenderingMap() {
    if (stateCallback != null) {
      stateCallback.onWillStartRenderingMap();
    }
  }

  @Keep
  private void onDidFinishRenderingMap(boolean fully) {
    if (stateCallback != null) {
      stateCallback.onDidFinishRenderingMap(fully);
    }
  }

  @Keep
  private void onDidBecomeIdle() {
    if (stateCallback != null) {
      stateCallback.onDidBecomeIdle();
    }
  }

  @Keep
  private void onDidFinishLoadingStyle() {
    if (stateCallback != null) {
      stateCallback.onDidFinishLoadingStyle();
    }
  }

  @Keep
  private void onSourceChanged(String sourceId) {
    if (stateCallback != null) {
      stateCallback.onSourceChanged(sourceId);
    }
  }

  @Keep
  private void onStyleImageMissing(String imageId) {
    if (stateCallback != null) {
      stateCallback.onStyleImageMissing(imageId);
    }
  }

  @Keep
  private boolean onCanRemoveUnusedStyleImage(String imageId) {
    if (stateCallback != null) {
      return stateCallback.onCanRemoveUnusedStyleImage(imageId);
    }

    return true;
  }

  @Keep
  protected void onSnapshotReady(@Nullable Bitmap mapContent) {
    if (checkState("OnSnapshotReady")) {
      return;
    }

    try {
      if (snapshotReadyCallback != null && mapContent != null) {
        if (viewCallback == null) {
          snapshotReadyCallback.onSnapshotReady(mapContent);
        } else {
          Bitmap viewContent = viewCallback.getViewContent();
          if (viewContent != null) {
            snapshotReadyCallback.onSnapshotReady(
                    BitmapUtils.mergeBitmaps(mapContent, viewContent)
            );
          }
        }
      }
    } catch (Throwable err) {
      Logger.e(TAG, "Exception in onSnapshotReady", err);
      throw err;
    }
  }

  @Keep
  private void onPreCompileShader(int id, int type, String additionalDefines) {
    if (stateCallback != null) {
      stateCallback.onPreCompileShader(id, type, additionalDefines);
    }
  }

  @Keep
  private void onPostCompileShader(int id, int type, String additionalDefines) {
    if (stateCallback != null) {
      stateCallback.onPostCompileShader(id, type, additionalDefines);
    }
  }

  @Keep
  private void onShaderCompileFailed(int id, int type, String additionalDefines) {
    if (stateCallback != null) {
      stateCallback.onShaderCompileFailed(id, type, additionalDefines);
    }
  }

  @Keep
  private void onGlyphsLoaded(String[] stack, int rangeStart, int rangeEnd) {
    if (stateCallback != null) {
      stateCallback.onGlyphsLoaded(stack, rangeStart, rangeEnd);
    }
  }

  @Keep
  private void onGlyphsError(String[] stack, int rangeStart, int rangeEnd) {
    if (stateCallback != null) {
      stateCallback.onGlyphsError(stack, rangeStart, rangeEnd);
    }
  }

  @Keep
  private void onGlyphsRequested(String[] stack, int rangeStart, int rangeEnd) {
    if (stateCallback != null) {
      stateCallback.onGlyphsRequested(stack, rangeStart, rangeEnd);
    }
  }

  @Keep
  private void onTileAction(TileOperation op, int x, int y, int z, int wrap, int overscaledZ, String sourceID) {
    if (stateCallback != null) {
      stateCallback.onTileAction(op, x, y, z, wrap, overscaledZ, sourceID);
    }
  }

  @Keep
  private void onSpriteLoaded(String id, String url) {
    if (stateCallback != null) {
      stateCallback.onSpriteLoaded(id, url);
    }
  }

  @Keep
  private void onSpriteError(String id, String url) {
    if (stateCallback != null) {
      stateCallback.onSpriteError(id, url);
    }
  }

  @Keep
  private void onSpriteRequested(String id, String url) {
    if (stateCallback != null) {
      stateCallback.onSpriteRequested(id, url);
    }
  }

  //
  // JNI methods
  //

  @Keep
  private native void nativeInitialize(NativeMapView nativeMap,
                                       FileSource fileSource,
                                       MapRenderer mapRenderer,
                                       NativeMapOptions nativeOptions);

  @Keep
  private native void nativeDestroy();

  @Keep
  private native void nativeResizeView(int width, int height);

  @Keep
  private native void nativeSetStyleUrl(String url);

  @NonNull
  @Keep
  private native String nativeGetStyleUrl();

  @Keep
  private native void nativeSetStyleJson(String newStyleJson);

  @NonNull
  @Keep
  private native String nativeGetStyleJson();

  @Keep
  private native void nativeSetLatLngBounds(LatLngBounds latLngBounds);

  @Keep
  private native void nativeCancelTransitions();

  @Keep
  private native void nativeSetGestureInProgress(boolean inProgress);

  @Keep
  private native void nativeMoveBy(double dx, double dy, long duration);

  @Keep
  private native void nativeSetLatLng(double latitude, double longitude, double[] padding, long duration);

  @NonNull
  @Keep
  private native LatLng nativeGetLatLng();

  @NonNull
  @Keep
  private native CameraPosition nativeGetCameraForLatLngBounds(
    LatLngBounds latLngBounds, double top, double left, double bottom, double right, double bearing, double tilt);

  @NonNull
  @Keep
  private native CameraPosition nativeGetCameraForGeometry(
    Geometry geometry, double top, double left, double bottom, double right, double bearing, double tilt);

  @Keep
  private native void nativeResetPosition();

  @Keep
  private native double nativeGetPitch();

  @Keep
  private native void nativeSetPitch(double pitch, long duration);

  @Keep
  private native void nativeSetZoom(double zoom, double cx, double cy, long duration);

  @Keep
  private native double nativeGetZoom();

  @Keep
  private native void nativeResetZoom();

  @Keep
  private native void nativeSetMinZoom(double zoom);

  @Keep
  private native double nativeGetMinZoom();

  @Keep
  private native void nativeSetMaxZoom(double zoom);

  @Keep
  private native double nativeGetMaxZoom();

  @Keep
  private native void nativeSetMinPitch(double pitch);

  @Keep
  private native double nativeGetMinPitch();

  @Keep
  private native void nativeSetMaxPitch(double pitch);

  @Keep
  private native double nativeGetMaxPitch();

  @Keep
  private native void nativeRotateBy(double sx, double sy, double ex, double ey, long duration);

  @Keep
  private native void nativeSetBearing(double degrees, long duration);

  @Keep
  private native void nativeSetBearingXY(double degrees, double fx, double fy, long duration);

  @Keep
  private native double nativeGetBearing();

  @Keep
  private native void nativeResetNorth();

  @Keep
  private native void nativeUpdateMarker(long markerId, double lat, double lon, String iconId);

  @NonNull
  @Keep
  private native long[] nativeAddMarkers(Marker[] markers);

  @NonNull
  @Keep
  private native long[] nativeAddPolylines(Polyline[] polylines);

  @NonNull
  @Keep
  private native long[] nativeAddPolygons(Polygon[] polygons);

  @Keep
  private native void nativeRemoveAnnotations(long[] id);

  @NonNull
  @Keep
  private native long[] nativeQueryPointAnnotations(RectF rect);

  @NonNull
  @Keep
  private native long[] nativeQueryShapeAnnotations(RectF rect);

  @Keep
  private native void nativeAddAnnotationIcon(String symbol, int width, int height, float scale, byte[] pixels);

  @Keep
  private native void nativeRemoveAnnotationIcon(String symbol);

  @Keep
  private native void nativeSetVisibleCoordinateBounds(LatLng[] coordinates, RectF padding,
                                                       double direction, long duration);

  @Keep
  private native void nativeOnLowMemory();

  @Keep
  private native void nativeSetDebug(boolean debug);

  @Keep
  private native boolean nativeGetDebug();

  @Keep
  private native String[] nativeGetActionJournalLogFiles();

  @Keep
  private native String[] nativeGetActionJournalLog();

  @Keep
  private native void nativeClearActionJournalLog();

  @Keep
  private native boolean nativeIsFullyLoaded();

  @Keep
  private native void nativeSetReachability(boolean status);

  @Keep
  private native double nativeGetMetersPerPixelAtLatitude(double lat, double zoom);

  @NonNull
  @Keep
  private native ProjectedMeters nativeProjectedMetersForLatLng(double latitude, double longitude);

  @NonNull
  @Keep
  private native LatLng nativeLatLngForProjectedMeters(double northing, double easting);

  @NonNull
  @Keep
  private native PointF nativePixelForLatLng(double lat, double lon);

  @Keep
  private native void nativePixelsForLatLngs(double[] input, double[] output, float pixelRatio);

  @Keep
  private native void nativeGetVisibleCoordinateBounds(double[] output);

  @NonNull
  @Keep
  private native LatLng nativeLatLngForPixel(float x, float y);

  @Keep
  private native void nativeLatLngsForPixels(double[] input, double[] output, float pixelRatio);

  @Keep
  private native double nativeGetTopOffsetPixelsForAnnotationSymbol(String symbolName);

  @Keep
  private native void nativeJumpTo(double angle, double latitude, double longitude, double pitch, double zoom,
                                   double[] padding);

  @Keep
  private native void nativeEaseTo(double angle, double latitude, double longitude,
                                   long duration, double pitch, double zoom, double[] padding,
                                   boolean easingInterpolator);

  @Keep
  private native void nativeFlyTo(double angle, double latitude, double longitude,
                                  long duration, double pitch, double zoom, double[] padding);

  @NonNull
  @Keep
  private native CameraPosition nativeGetCameraPosition();

  @Keep
  private native void nativeSetTransitionOptions(TransitionOptions transitionOptions);

  @NonNull
  @Keep
  private native TransitionOptions nativeGetTransitionOptions();

  @Keep
  private native long nativeGetTransitionDuration();

  @Keep
  private native void nativeSetTransitionDuration(long duration);

  @Keep
  private native long nativeGetTransitionDelay();

  @Keep
  private native void nativeSetTransitionDelay(long delay);

  @NonNull
  @Keep
  private native Layer[] nativeGetLayers();

  @NonNull
  @Keep
  private native Layer nativeGetLayer(String layerId);

  @Keep
  private native void nativeAddLayer(long layerPtr, String before) throws CannotAddLayerException;

  @Keep
  private native void nativeAddLayerAbove(long layerPtr, String above) throws CannotAddLayerException;

  @Keep
  private native void nativeAddLayerAt(long layerPtr, int index) throws CannotAddLayerException;

  @Keep
  private native boolean nativeRemoveLayer(long layerId);

  @Keep
  private native boolean nativeRemoveLayerAt(int index);

  @NonNull
  @Keep
  private native Source[] nativeGetSources();

  @NonNull
  @Keep
  private native Source nativeGetSource(String sourceId);

  @Keep
  private native void nativeAddSource(Source source, long sourcePtr) throws CannotAddSourceException;

  @Keep
  private native boolean nativeRemoveSource(Source source, long sourcePtr);

  @Keep
  private native void nativeAddImage(String name, Bitmap bitmap, float pixelRatio, boolean sdf);

  @Keep
  private native void nativeAddImages(Image[] images);

  @Keep
  private native void nativeRemoveImage(String name);

  @NonNull
  @Keep
  private native Bitmap nativeGetImage(String name);

  @Keep
  private native void nativeUpdatePolygon(long polygonId, Polygon polygon);

  @Keep
  private native void nativeUpdatePolyline(long polylineId, Polyline polyline);

  @Keep
  private native void nativeTakeSnapshot();

  @NonNull
  @Keep
  private native Feature[] nativeQueryRenderedFeaturesForPoint(float x, float y,
                                                               String[] layerIds,
                                                               Object[] filter);

  @NonNull
  @Keep
  private native Feature[] nativeQueryRenderedFeaturesForBox(float left, float top,
                                                             float right, float bottom,
                                                             String[] layerIds,
                                                             Object[] filter);

  @NonNull
  @Keep
  private native Light nativeGetLight();

  @Keep
  private native void nativeSetPrefetchTiles(boolean enable);

  @Keep
  private native boolean nativeGetPrefetchTiles();

  @Keep
  private native void nativeSetPrefetchZoomDelta(int delta);

  @Keep
  private native void nativeSetTileCacheEnabled(boolean enabled);

  @Keep
  private native boolean nativeGetTileCacheEnabled();

  @Keep
  private native int nativeGetPrefetchZoomDelta();

  @Keep
  private native void nativeSetTileLodMinRadius(double radius);

  @Keep
  private native double nativeGetTileLodMinRadius();

  @Keep
  private native void nativeSetTileLodScale(double scale);

  @Keep
  private native double nativeGetTileLodScale();

  @Keep
  private native void nativeSetTileLodPitchThreshold(double threshold);

  @Keep
  private native double nativeGetTileLodPitchThreshold();

  @Keep
  private native void nativeSetTileLodZoomShift(double shift);

  @Keep
  private native double nativeGetTileLodZoomShift();

  @Override
  public long getNativePtr() {
    return nativePtr;
  }

  @Keep
  private native void nativeTriggerRepaint();

  @Keep
  private native boolean nativeIsRenderingStatsViewEnabled();

  @Keep
  private native void nativeEnableRenderingStatsView(boolean enabled);

  @Keep
  private native void nativeToggleTransform();

  //
  // Snapshot
  //

  @Override
  public void addSnapshotCallback(@NonNull MapLibreMap.SnapshotReadyCallback callback) {
    if (checkState("addSnapshotCallback")) {
      return;
    }
    snapshotReadyCallback = callback;
    nativeTakeSnapshot();
  }

  @Override
  public void setOnFpsChangedListener(@Nullable final MapLibreMap.OnFpsChangedListener listener) {
    final Handler handler = new Handler(Looper.getMainLooper());
    mapRenderer.queueEvent(new Runnable() {

      @Override
      public void run() {
        if (listener != null) {
          mapRenderer.setOnFpsChangedListener(new MapLibreMap.OnFpsChangedListener() {
            @Override
            public void onFpsChanged(final double fps) {
              handler.post(() -> listener.onFpsChanged(fps));
            }
          });
        } else {
          mapRenderer.setOnFpsChangedListener(null);
        }
      }
    });
  }

  @Override
  public boolean isDestroyed() {
    return destroyed;
  }

  private double[] getAnimationPaddingAndClearCachedInsets(double[] providedPadding) {
    if (providedPadding == null) {
      providedPadding = this.edgeInsets;
    }
    this.edgeInsets = null;
    return providedPadding == null ? null :
      new double[] {
        providedPadding[1] / pixelRatio,
        providedPadding[0] / pixelRatio,
        providedPadding[3] / pixelRatio,
        providedPadding[2] / pixelRatio
      };
  }

  public interface ViewCallback {
    @Nullable
    Bitmap getViewContent();
  }

  interface StyleCallback {
    void onWillStartLoadingMap();

    void onDidFinishLoadingStyle();
  }

  interface StateCallback extends StyleCallback {
    void onCameraWillChange(boolean animated);

    void onCameraIsChanging();

    void onCameraDidChange(boolean animated);

    void onDidFinishLoadingMap();

    void onDidFailLoadingMap(String error);

    void onWillStartRenderingFrame();

    void onDidFinishRenderingFrame(boolean fully, RenderingStats stats);

    void onWillStartRenderingMap();

    void onDidFinishRenderingMap(boolean fully);

    void onDidBecomeIdle();

    void onSourceChanged(String sourceId);

    void onStyleImageMissing(String imageId);

    boolean onCanRemoveUnusedStyleImage(String imageId);

    void onPreCompileShader(int id, int type, String additionalDefines);

    void onPostCompileShader(int id, int type, String additionalDefines);

    void onShaderCompileFailed(int id, int type, String additionalDefines);

    void onGlyphsLoaded(String[] stack, int rangeStart, int rangeEnd);

    void onGlyphsError(String[] stack, int rangeStart, int rangeEnd);

    void onGlyphsRequested(String[] stack, int rangeStart, int rangeEnd);

    void onTileAction(TileOperation op, int x, int y, int z, int wrap, int overscaledZ, String sourceID);

    void onSpriteLoaded(String id, String url);

    void onSpriteError(String id, String url);

    void onSpriteRequested(String id, String url);
  }
}
