# Changelog MapLibre Native for Android

## 11.12.0

### ✨ Features and improvements

- feat: add clusterMinPoints option Android and iOS ([#3601](https://github.com/maplibre/maplibre-native/pull/3601)).

## 11.11.0

### ✨ Features and improvements

- Improve the logic to let source peers be consistent with C++ peers ([#3561](https://github.com/maplibre/maplibre-native/pull/3561)).
- set default move gesture threshold to a small non-zero value ([#3573](https://github.com/maplibre/maplibre-native/pull/3573)).
- Bump version of MapLibre Gestures Android to 0.0.4 ([#3583](https://github.com/maplibre/maplibre-native/pull/3583)).
- Expose sync methods in GeoJsonSource  ([#3560](https://github.com/maplibre/maplibre-native/pull/3560)).

### 🐞 Bug fixes

- Prevent `Style.validateState()` exception on location state updates ([#3574](https://github.com/maplibre/maplibre-native/pull/3574)).
- Fix the symbol blink issue by only placing the symbol in current level ([#3534](https://github.com/maplibre/maplibre-native/pull/3534)).

## 11.10.5

### ✨ Features and improvements

- Release debug builds for android

## 11.10.4

### ✨ Features and improvements

- Concurrent camera animations

## 11.10.3

### 🐞 Bug fixes

- Revert fix [#3536](https://github.com/maplibre/maplibre-native/pull/3536) due to `getSource` crashes.

## 11.10.2

### 🐞 Bug fixes

- Fix segfault this-capture GeoJsonSource which may be deleted ([#3536](https://github.com/maplibre/maplibre-native/pull/3536)).
- Add `MapRenderer` dangling reference check ([#3541](https://github.com/maplibre/maplibre-native/pull/3541)).
- Fix incomplete feature state updates in GeometryTile and SourceFeatureState [Vector Tile Layer]. ([#3406](https://github.com/maplibre/maplibre-native/pull/3406)).

### ✨ Features and improvements

- Fix recycler view test ([#3537](https://github.com/maplibre/maplibre-native/pull/3537)).

## 11.10.1

### 🐞 Bug fixes

- Add missing proguard rules ([#3529](https://github.com/maplibre/maplibre-native/pull/3529)).

## 11.10.0

### ✨ Features and improvements

- Add action journal ([#3409](https://github.com/maplibre/maplibre-native/pull/3409)). Documentation: https://maplibre.org/maplibre-native/android/examples/observability/action-journal/
- Pattern layout performance improvement ([#3495](https://github.com/maplibre/maplibre-native/pull/3495)).
- Add Tile LOD controls ([#2958](https://github.com/maplibre/maplibre-native/pull/2958)).

### 🐞 Bug fixes

- Improve weak pointer use ([#3510](https://github.com/maplibre/maplibre-native/pull/3510)).
- Make sure AndroidRendererFrontend exists when accessing it ([#3522](https://github.com/maplibre/maplibre-native/pull/3522))
## 11.9.0

### ✨ Features and improvements

- Add dynamic texture atlas ([#3198](https://github.com/maplibre/maplibre-native/pull/3198)).
- Remove some of unused legacy uniforms ([#3481](https://github.com/maplibre/maplibre-native/pull/3481)).

### 🐞 Bug fixes

- Fix vulkan scaling issue ([#3489](https://github.com/maplibre/maplibre-native/pull/3489)).
- Fix swapchain out of bounds ([#3486](https://github.com/maplibre/maplibre-native/pull/3486)).
- Fix validation error reported by VulkanSDK 1.4.313.0 ([#3471](https://github.com/maplibre/maplibre-native/pull/3471)).P

## 11.9.0-pre0

Pre-release to test out the new [dynamic texture atlas](https://github.com/maplibre/maplibre-native/pull/3198). This should result in a memory reduction. Please [open an issue](https://github.com/maplibre/maplibre-native/issues) if you encounter any problems.

## 11.8.8

### ✨ Features and improvements

- Update NDK to 28.1.13356709 ([#3450](https://github.com/maplibre/maplibre-native/pull/3450)).
- Add support to range requests in AssetFileSource ([#3461](https://github.com/maplibre/maplibre-native/pull/3404)).
- Force PMTiles metadata to always have XYZ tile scheme ([#3403](https://github.com/maplibre/maplibre-native/pull/3403)).

### 🐞 Bug fixes

- Make sure renderThread is alive before calling requestExitAndWait() ([#3461](https://github.com/maplibre/maplibre-native/pull/3461)).

## 11.8.7

We now make releases with debug builds to make it easier to report issues with relevant logs.

They are available with a `-debug` postfix on Maven Central, for example `org.maplibre.gl:android-sdk-vulkan-debug`.

## 11.8.7

### ✨ Features and improvements

- Change Java Transfrom class from final to normal ([#3332](https://github.com/maplibre/maplibre-native/pull/3332)).

## 11.8.6

### ✨ Features and improvements

- Change Java Transfrom class from final to normal ([#3332](https://github.com/maplibre/maplibre-native/pull/3332)).

### 🐞 Bug fixes

- Fix rare crash LatLngAnimator ([#3352](https://github.com/maplibre/maplibre-native/pull/3352)).
- Sync surface destruction with main thread ([#3368](https://github.com/maplibre/maplibre-native/pull/3368)).
- Prevent exception SymbolLocationLayerRenderer with new style ([#3369](https://github.com/maplibre/maplibre-native/pull/3369)).
- Fix issue related to symbol icon scaling with offset

## 11.8.5

### 🐞 Bug fixes

- Add regression test for [#3323](https://github.com/maplibre/maplibre-native/pull/3323), bug in layer dependency tracking ([#3326](https://github.com/maplibre/maplibre-native/pull/3326))
- Fix `ErrorSurfaceLostKHR` exception ([#3337](https://github.com/maplibre/maplibre-native/pull/3337)).

## 11.8.4

### 🐞 Bug fixes

-  Add workaround for android emulator as crash on Android emulation is still presented ([#3310](https://github.com/maplibre/maplibre-native/pull/3310))

## 11.8.3

### 🐞 Bug fixes

- Revert "Eliminate copies in deferred cleanup" ([#3035](https://github.com/maplibre/maplibre-native/pull/3035)) which was causing a memory growth issue.

## 11.8.2

### ✨ Features and improvements

- Eliminate copies in deferred cleanup ([#3035](https://github.com/maplibre/maplibre-native/pull/3035)).
- (Custom Layer Support) Reset depth stencil state for render pass ([#3230](https://github.com/maplibre/maplibre-native/pull/3230)).

### 🐞 Bug fixes

- Fix texture view cleanup ([#3254](https://github.com/maplibre/maplibre-native/pull/3254)). Possible fix for [issue](https://github.com/maplibre/maplibre-native/issues/3241) reported by Lyft.

## 11.8.1

> [!NOTE]
> We are now releasing OpenGL ES and Vulkan variants of MapLibre Android. See the [11.7.0 release notes](https://github.com/maplibre/maplibre-native/releases/tag/android-v11.7.0) for details.

### ✨ Features and improvements

- Bind global uniform buffers at the end of the custom layer tweaker ([#3186](https://github.com/maplibre/maplibre-native/pull/3186)).

### 🐞 Bug fixes

- (Vulkan) Fix TextureMode crash ([#3144](https://github.com/maplibre/maplibre-native/pull/3144)).
- (Vulkan) Use timestamps for image descriptor updates ([#3152](https://github.com/maplibre/maplibre-native/pull/3152)).
- Change runtime error to the nullptr returning ([#3184](https://github.com/maplibre/maplibre-native/pull/3184)).

## 11.8.0

> [!NOTE]
> We are now releasing OpenGL ES and Vulkan variants of MapLibre Android. See the [11.7.0 release notes](https://github.com/maplibre/maplibre-native/releases/tag/android-v11.7.0) for details.

### ✨ Features and improvements

- Add PMTiles support ([#2882](https://github.com/maplibre/maplibre-native/pull/2882)).
- Consolidate UBOs ([#3089](https://github.com/maplibre/maplibre-native/pull/3089)).

### 🐞 Bug fixes

- (Vulkan) Fix in-flight frame update Vulkan ([#3122](https://github.com/maplibre/maplibre-native/pull/3122)). Fixes jittery labels and some similar issues.

We have a new feature in the C++ Core to constrain the screen (instead of the center of the camera) to some bounds ([#2475](https://github.com/maplibre/maplibre-native/pull/2475)). This functionality still has to be exposed to Android. If you are interested in implementing this, see [this issue](https://github.com/maplibre/maplibre-native/issues/3128).

## 11.7.1

> [!NOTE]
> We are now releasing OpenGL ES and Vulkan variants of MapLibre Android. See the [11.7.0 release notes](https://github.com/maplibre/maplibre-native/releases/tag/android-v11.7.0) for details.

### ✨ Features and improvements

- Batch up scheduling of deferred deletions ([#3030](https://github.com/maplibre/maplibre-native/pull/3030)).
- Specify Vulkan version needed in AndroidManifest.xml ([#3095](https://github.com/maplibre/maplibre-native/pull/3095)).

### 🐞 Bug fixes

- Remove `Pass3D` ([#3077](https://github.com/maplibre/maplibre-native/pull/3077)).
      Fixes issue where filters applied to fill extrusion layers are not rendered unless a manual zoom is applied to the map ([#3039](https://github.com/maplibre/maplibre-native/issues/3039)).

## 11.7.0

This release marks the official release of MapLibre Android with Vulkan support. [Vulkan](https://www.vulkan.org) is a modern graphics API which brings advantages such as improved performance, improved observability and better stability. Specifically, starting with this version we are releasing multiple versions of MapLibre Android:

- `org.maplibre.gl:android-sdk` (still OpenGL ES for now, might default to another rendering backend or might choose depending on device support in a future major release).
- `org.maplibre.gl:android-sdk-opengl` (OpenGL ES).
- `org.maplibre.gl:android-sdk-vulkan` (Vulkan).

Stability has proven to be excellent, but there are a few [known issues with Vulkan](https://github.com/maplibre/maplibre-native/issues?q=is%3Aissue%20state%3Aopen%20label%3AVulkan%20type%3ABug) that will be addressed in a future update.

Currently it is not possible to choose a backend at runtime. If you care about supporting devices that only support OpenGL ES and you want to use Vulkan, you will need to produce and ship [multiple APKs](https://developer.android.com/google/play/publishing/multiple-apks). Please see [this issue](https://github.com/maplibre/maplibre-native/issues/3079) if you are interested in choosing a rendering backend during initialization.

Thanks to everyone who helped test the pre-releases!

### ✨ Features and improvements

- Fix the issue that the empty polyline cannot be updated ([#3046](https://github.com/maplibre/maplibre-native/pull/3046)).
- feat: add `getZoom` and `setZoom` function support for Android Auto ([#3037](https://github.com/maplibre/maplibre-native/pull/3037)).
- Use MapLibre Android as attribution string across languages ([#3080](https://github.com/maplibre/maplibre-native/pull/3080)).
- Use CMake 3.24.0+ for Android ([#3065](https://github.com/maplibre/maplibre-native/pull/3065)).

### 🐞 Bug fixes

- Fix crash on unsupported attribute type conversion ([#3066](https://github.com/maplibre/maplibre-native/pull/3066)).
## 11.6.3

## Features and improvements

### Bug fixes

- LoD Clamping fixed to be based on view range and not source range to support over/under zooming
- Disable depth writing for the location indicator layer

## 11.6.2

### Features and Improvements

- Add LoD Support

## 11.6.1

### 🐞 Bug fixes

- Fix 16K alignment Android builds ([#2995](https://github.com/maplibre/maplibre-native/issues/2995)).

### ✨ Features and improvements

- Allow configuring a `Call.Factory` instead of a `OkHttpClient` ([https://github.com/maplibre/maplibre-native/pull/2987](#2987)). Since an `OkHttpClient` can be assigned to a `Call.Factory` this should not cause any issues.

### 🐞 Bug fixes

- Fix puck rendering behavior Android ([#2957](https://github.com/maplibre/maplibre-native/pull/2957)).

## 11.6.0

### ✨ Features and improvements

- Add support for `text-variable-anchor-offset` property ([#2921](https://github.com/maplibre/maplibre-native/pull/2921)).
- Change `parameters.currentLayer` to `index` instead of `layerIndex` ([#2956](https://github.com/maplibre/maplibre-native/pull/2956)).

### 🐞 Bug fixes

- Fix MapSnapshotter icon loading ([#2967](https://github.com/maplibre/maplibre-native/pull/2967)).

## 11.5.2

### ✨ Features and improvements

- Use Kotlin instead of Groovy for Android build configuration ([#2902](https://github.com/maplibre/maplibre-native/pull/2902)).
- Performance improvements ([#2892](https://github.com/maplibre/maplibre-native/pull/2892)).
- Remove workaround for emulator crashes, and apply proper fix: revert index buffers to dirty flag instead of timestamps ([#2927](https://github.com/maplibre/maplibre-native/pull/2927)).

### 🐞 Bug fixes

- Fix line atlas, which was causing an issue with `line-pattern`s ([#2908](https://github.com/maplibre/maplibre-native/pull/2908)).

## 11.5.1

### ✨ Features and improvements

- Add `PropertyFactory.iconPadding(Float)` overload for better backcompat ([#2880](https://github.com/maplibre/maplibre-native/pull/2880)).

### 🐞 Bug fixes

- Android renderThreadManager changed to non static ([#2872](https://github.com/maplibre/maplibre-native/pull/2872)).
- Make sure `Scheduler::GetCurrent()` cannot return a nullptr ([#2887](https://github.com/maplibre/maplibre-native/pull/2887)). This should fix a crash on startup when the library is not initialized on the main thread.

## 11.5.0

### ✨ Features and improvements

- Expose `RENDERMODE_CONTINUOUSLY` and `RENDERMODE_WHEN_DIRTY` ([#2801](https://github.com/maplibre/maplibre-native/pull/2801)). Toggling this can be helpful for development (e.g. when testing and optimizing performance).
- Compile for Android with support for 16 KB page sizes ([#2852](https://github.com/maplibre/maplibre-native/pull/2852)).
- Enable crash mitigation when running OpenGL in the Android emulator ([#2858](https://github.com/maplibre/maplibre-native/pull/2858)). Technically the crashes were caused by an issue with the Android Emulator, but we were able to find a workaround.
- Add array support for `icon-padding` ([#2845](https://github.com/maplibre/maplibre-native/pull/2845)).
  More information can be found in the [MapLibre Style Spec Documentation](https://maplibre.org/maplibre-style-spec/types/#padding). This is the first (code) contribution from [@random3940](https://github.com/random3940)! 🎉
- Use `thread_local` for thread local scheduler ([#2863](https://github.com/maplibre/maplibre-native/pull/2863)).

### 🐞 Bug fixes

- Move WeakPtrFactory in `map_renderer.hpp` ([#2861](https://github.com/maplibre/maplibre-native/pull/2861)).
- Fix updates hillshade geometry ([#2842](https://github.com/maplibre/maplibre-native/pull/2842)).

## 11.4.0

### ✨ Features and improvements

- Add `textFitWidth` and `textFitHeight` properties to sprites ([#2780](https://github.com/maplibre/maplibre-native/pull/2780)).
  More information can be found in the [MapLibre Style Spec](https://maplibre.org/maplibre-style-spec/sprite/#text-fit-properties).
- Move UBO updates from render layers to tweakers ([#2703](https://github.com/maplibre/maplibre-native/pull/2703)).
- Improve tile lifecycle determinism  ([#2819](https://github.com/maplibre/maplibre-native/pull/2819)).
- Make sure swapBehavior is NoFlush ([#2827](https://github.com/maplibre/maplibre-native/pull/2827)).

### 🐞 Bug fixes

- Ensure that all depth values are rendered before any color values ([#2811](https://github.com/maplibre/maplibre-native/pull/2811)).

## 11.3.0

### ✨ Features and improvements

- Added new map observer events: `onPreCompileShader`, `onPostCompileShader`, `onShaderCompileFailed`, `onGlyphsLoaded`, `onGlyphsError`, `onGlyphsRequested`, `onTileAction`, `onSpriteLoaded`, `onSpriteError`, `onSpriteRequested` ([#2694](https://github.com/maplibre/maplibre-native/pull/2694)).
- Correct and revise OpenGL texture pooling ([#2709](https://github.com/maplibre/maplibre-native/pull/2709)).
- Update Android dependencies ([#2794](https://github.com/maplibre/maplibre-native/pull/2794)).
- Make project compatible again with Java 11, based on feedback from @alasram ([#2799](https://github.com/maplibre/maplibre-native/pull/2799)).

### 🐞 Bug fixes

- Fix raster masking bug ([#2798](https://github.com/maplibre/maplibre-native/pull/2798)).

## 11.2.0

### ✨ Features and improvements

- Toggle tile cache final API ([#2723](https://github.com/maplibre/maplibre-native/pull/2723)).
    This is a new API on `MapLibreMap`: `setTileCacheEnabled()` and `getTileCacheEnabled()`. This tile cache is used to cache tiles on different zoom levels, disabling it will reduce memory usage.
- Add `getOfflineRegion` Kotlin API ([#2516](https://github.com/maplibre/maplibre-native/pull/2516)). This is the first (code) contribution from [@JRWilding](https://github.com/JRWilding)! 🎉
- Bump NDK version to 27.0.12077973, replace `ALooper_pollAll` with `ALooper_pollOnce` ([#2663](https://github.com/maplibre/maplibre-native/pull/2663)).
- Remove OkHttp3 ProGuard rules ([#2665](https://github.com/maplibre/maplibre-native/pull/2665)).
- Use C++20 ([#2659](https://github.com/maplibre/maplibre-native/pull/2659)).
- Reuse prefetched tiles to avoid empty screen ([#2668](https://github.com/maplibre/maplibre-native/pull/2668)).
- Update Android Dependencies, use [maplibre-gestures-android](https://github.com/maplibre/maplibre-gestures-android) ([#2714](https://github.com/maplibre/maplibre-native/pull/2714)).
- Update dependency gradle to v8.10 ([#2721](https://github.com/maplibre/maplibre-native/pull/2721)).
- Cleanup `mbgl/actor/mailbox*` implementation for repetition in ensuring valid weakScheduler exists before usage ([#2733](https://github.com/maplibre/maplibre-native/pull/2733)).
- Use latest MapLibre Style Spec ([#2756](https://github.com/maplibre/maplibre-native/pull/2756)).
  This PR adds two new APIs to `TransitionOptions` of `LocationIndicatorLayer`: `getBearingTransition()` and `setBearingTransition()`.
- Use timestamps for attribute updates ([#2629](https://github.com/maplibre/maplibre-native/pull/2629)).

### 🐞 Bug fixes

- Fix crash when feature contains invalid UTF-8 data ([#2693](https://github.com/maplibre/maplibre-native/pull/2693)).
- Fix accidental regression conditional layer evaluation ([#2705](https://github.com/maplibre/maplibre-native/pull/2705)).
- Ensure `ReentrantLock` is unlocked after being locked, and on same thread ([#2759](https://github.com/maplibre/maplibre-native/pull/2759)). This is the first (code) contributon from [@westnordost](https://github.com/westnordost)! 🎉
- Add guard blocks and checks to `SymbolInstance` ([#2744](https://github.com/maplibre/maplibre-native/pull/2744)).
  This fixes an exceedingly rare crash that a user is seeing in the field. The fix is not pretty, but we hope to better understand the problem in the future (see [this open issue](https://github.com/maplibre/maplibre-native/issues/2350)).

## 11.1.0

### ✨ Features and improvements

- Avoid logging error for onMove(0,0) on Android ([#2580](https://github.com/maplibre/maplibre-native/pull/2580)).
- Experimental API to toggle tile cache in map view ([#2590](https://github.com/maplibre/maplibre-native/pull/2590)). This can reduce memory usage at the cost of having to parse tile data again when the zoom level changes.
- Add TaggedScheduler, couple lifetime of tasks and orchestrator ([#2398](https://github.com/maplibre/maplibre-native/pull/2398)).

### 🐞 Bug fixes

- Fix null pointer dereference MapRenderer Android ([#2631](https://github.com/maplibre/maplibre-native/pull/2631)).
- Take locks before signaling thread condition variables ([#2636](https://github.com/maplibre/maplibre-native/pull/2636)).

## 11.0.2-pre0

### ✨ Features and improvements

- Add TaggedScheduler, couple lifetime of tasks and orchestrator ([#2398](https://github.com/maplibre/maplibre-native/pull/2398)).

## 11.0.1

### ✨ Features and improvements

- Updated included licenses third-party projects ([#2491](https://github.com/maplibre/maplibre-native/pull/2491)).
- Move all `WeakPtrFactory` members to the end of the containing class ([#2472](https://github.com/maplibre/maplibre-native/pull/2472)).

### 🐞 Bug fixes

- Fixes crash that happened when parsing some PBF files ([Issue](https://github.com/maplibre/maplibre-native/issues/795), [PR](https://github.com/maplibre/maplibre-native/pull/2460)).
- Shut down the mailbox on raster tiles as with GeometryTile to avoid processing messages after it's queued for destruction ([#2443](https://github.com/maplibre/maplibre-native/pull/2443)).
- Increase the deferred cleanup timeout ([#2455](https://github.com/maplibre/maplibre-native/pull/2455)).
- Fix crash caused `DefaultRefDeleter`, reported by Komoot ([#2487](https://github.com/maplibre/maplibre-native/pull/2487)).

## 11.0.0

The rendering internals of MapLibre Native have undergone major changes. We've had an extensive period of [pre-releases](https://github.com/maplibre/maplibre-native/issues/1608) leading up to this official release. While we've worked hard to minimize potential issues, it's possible that there may still be regressions. Therefore, it's important to conduct your own testing and report any encountered issues on GitHub.

This release utilizes **OpenGL ES 3.0**. As a result, some older devices are no longer supported. If you require compatibility with devices that do not support OpenGL ES 3.0, it's recommended to continue using the 10.x.x release series.

### ✨ Features and improvements

- Add support for custom `ModuleProvider` implementations (#[2231](https://github.com/maplibre/maplibre-native/pull/2231))
- Allow setting padding when camera is tracking (#[2165](https://github.com/maplibre/maplibre-native/pull/2165)).
- Update user agent from Mapbox to MapLibreNative.

- 💥 Breaking: Change package of all classes from `com.mapbox.mapboxsdk` to `org.maplibre.android` ([#1201](https://github.com/maplibre/maplibre-native/pull/1201)). This means you will need to fix your imports.

  > To migrate:
  > In your imports in each of your project files, replace `com.mapbox.mapboxsdk.` with `org.maplibre.android.*`.

- 💥 Breaking: Rename several classes to no longer contain the word "Mapbox". You will need to migrate by renaming references.

  > To migrate:
  > Each affected occurrence will be marked as an error during compilation. Replace each occurrence of "Mapbox" with "MapLibre" and let your IDE do the import.
  >
  > These are the most important classes that have been renamed:
  >
  > - `Mapbox` → `MapLibre`
  > - `MapboxMap` → `MapLibreMap`
  > - `MapboxConstants` → `MapLibreConstants`
  > - `MapboxMapOptions` → `MapLibreMapOptions`
  > - but also others, less frequently used ones, like `MapboxGLSurfaceView` → `MapLibreGLSurfaceView`.

### 🐞 Bug fixes

- Fix "... has unresolved theme attributes" error in BitMapUtils ([#1274](https://github.com/maplibre/maplibre-native/issues/1274)).

## 10.3.1

This release includes [debug symbols](https://github.com/maplibre/maplibre-native/releases?q=android&expanded=true) on GitHub.

## 10.3.0

### ✨ Features and improvements

* Add support for the [`slice` expression](https://maplibre.org/maplibre-style-spec/expressions/#slice) ([#1133](https://github.com/maplibre/maplibre-native/pull/1133))
* Add support for [index-of expression](https://maplibre.org/maplibre-style-spec/expressions/#index-of) ([#1113](https://github.com/maplibre/maplibre-native/pull/1113))
* Change to a more natural fling animation and allow setting `flingThreshold` and `flingAnimationBaseTime` in `UiSettings` ([#963](https://github.com/maplibre/maplibre-native/pull/963))
* Add setting padding when camera is tracking ([#2165](https://github.com/maplibre/maplibre-native/pull/2165)).

### 🐞 Bug fixes

* Fix regression in CameraUpdateFactory#zoomOut ([#1035](https://github.com/maplibre/maplibre-native/pull/1035))
* `AndroidLocationEngineImpl` made public to create custom `LocationEngineProvider`([#850](https://github.com/maplibre/maplibre-native/pull/850))

## 10.2.0

Revert changes of 10.1.0, which was a breaking release by accident.

This version is identical to 10.0.2.

## 10.1.0 - May 9, 2023

### ✨ Features and improvements

- Change to a more natural fling animation and allow setting `flingThreshold` and `flingAnimationBaseTime` in `UiSettings` ([#963](https://github.com/maplibre/maplibre-native/pull/963))
- Add support for the [`index-of` expression](https://maplibre.org/maplibre-style-spec/expressions/#index-of) ([#1113](https://github.com/maplibre/maplibre-native/pull/1113))

### 🐞 Bug fixes

- Fix regression in CameraUpdateFactory#zoomOut ([#1035](https://github.com/maplibre/maplibre-native/pull/1035))
- `AndroidLocationEngineImpl` made public to create custom `LocationEngineProvider`([#850](https://github.com/maplibre/maplibre-native/pull/850))

## 10.0.2 - February 23, 2023

### 🐞 Bug fixes

- `MapboxFusedLocationEngineImpl` constructor made public to create custom `LocationEngineProvider`([#850](https://github.com/maplibre/maplibre-native/pull/850))

## 10.0.1 - February 22, 2023

### 🐞 Bug fixes

- Fixed regression with `RasterSource` native code throwing an exception expecting bound after switching to `FloatArray` ([#830](https://github.com/maplibre/maplibre-native/pull/830)).
- `LocationEngineProxy` was made public so that the migration path suggested in the 10.0.0 changelog for those wanting to use GMS Location Services can actually be implemented ([#832](https://github.com/maplibre/maplibre-native/pull/830)).

## 10.0.0 - February 15, 2023

### ✨ Features and improvements

- 💥 Breaking: Changed resourcePrefix to `maplibre_` from `mapbox_` ([#647](https://github.com/maplibre/maplibre-native/pull/647)) and renamed resources accordingly. Note that this is a breaking change since the names of public resources were renamed as well. Replaced Mapbox logo with MapLibre logo.

  > To migrate:
  > If you use any of the public Android resources, you will get an error that they can not be found. Replace the prefix of each, e.g. `R.style.mapbox_LocationComponent` -> `R.style.maplibre_LocationComponent`.

- 💥 Breaking: several deprecated overloads of `LocationComponent.activateLocationComponent` were removed. Use `LocationComponentActivationOptions.Builder` instead.

  > To migrate, as an example:
  >
  > ```kotlin
  >  locationComponent.activateLocationComponent(context, style, false, locationEngineRequest, locationComponentOptions)
  > ```
  >
  > becomes
  >
  > ```kotlin
  > val options = LocationComponentActivationOptions.builder(context, style).useDefaultLocationEngine(false).locationEngineRequest(locationEngineRequest).locationComponentOptions(locationComponentOptions).build()
  > locationComponent.activateLocationComponent(options)
  > ```

- 💥 Breaking: the `LocationEngine` implemented with Google Location Services has been removed to make MapLibre Native for Android fully FLOSS ([#379](https://github.com/maplibre/maplibre-native/issues/379)).

  > To migrate:
  > Include the source code of the removed [`GoogleLocationEngineImpl`](https://github.com/maplibre/maplibre-native/blob/4a34caab7593f4f1b6d8c09c06a5e25d7c6cfc43/platform/android/MapLibreAndroid/src/main/java/com/mapbox/mapboxsdk/location/engine/GoogleLocationEngineImpl.java) in your source tree.
  >
  > Pass an instance of `LocationEngine` based on `GoogleLocationEngineImpl` to `LocationComponentActivationOptions.Builder.locationEngine` (this was done in a now removed [`LocationEngineProvider`](https://github.com/maplibre/maplibre-native/blob/68d58d6f6f453d5c6cc0fa92fcc6c6cfe0cf967f/platform/android/MapLibreAndroid/src/main/java/com/mapbox/mapboxsdk/location/engine/LocationEngineProvider.java#L59) class):
  >
  > ```kotlin
  > val locationEngine = LocationEngineProxy<Any>(GoogleLocationEngineImpl(context))
  > val options = LocationComponentActivationOptions.builder(context, style).locationEngine(locationEngine).build()
  > locationComponent.activateLocationComponent(options)
  > ```

- 💥 Breaking: The static `LocationEngineResult.extractResult` can no longer extract a `LocationEngineResult` from a Google Play intent.

  > To migrate, include and use the [previous implementation](https://github.com/maplibre/maplibre-native/blob/ea234edf67bb3aec75f077e15c1c30c99756b926/platform/android/MapLibreAndroid/src/main/java/com/mapbox/mapboxsdk/location/engine/LocationEngineResult.java#L97) in your source tree.

- Improve Kotlinification of LatLng ([#742](https://github.com/maplibre/maplibre-native/issues))
- Increment minSdkVersion from 14 to 21, as it covers 99.2%% of the newer devices since 2014 and lessens the backward compatibility burden ([#630](https://github.com/maplibre/maplibre-native/pull/630))

### 🐞 Bug fixes

- Catches NaN for onMove event ([621](https://github.com/maplibre/maplibre-native/pull/621))
- `BitmapUtils.mergeBitmap` was deprecated, `BitmapUtils.mergeBitmaps` is a new method that does not offset views rendered on top of snapshots ([#733](https://github.com/maplibre/maplibre-native/issues/733))
- Fixed a crash when native code was accessing the LatLngBounds class [#655](https://github.com/maplibre/maplibre-native/pull/)

### ⛵ Dependencies

- Revert "Revert "Gradle update"" - Update Gradle from v3 to v7 ([#619](https://github.com/maplibre/maplibre-native/pull/619))

## 9.6.0 - December 18, 2022

### ✨ Features and improvements

- Add missing header guards ([#543](https://github.com/maplibre/maplibre-native/pull/543))
- Removing unused versions sdk ([#515](https://github.com/maplibre/maplibre-native/pull/515))
- (tag: node-v5.0.1-pre.0) Upgrade libs and remove Jetifier ([#218](https://github.com/maplibre/maplibre-native/pull/218))
- Migrate examples in android TestApp to Kotlin ([#416](https://github.com/maplibre/maplibre-native/pull/416))
- Add ClientOptions to be able to pass around client name and version ([#365](https://github.com/maplibre/maplibre-native/pull/365))
- Use maplibre docs for more links ([#354](https://github.com/maplibre/maplibre-native/pull/354))
- Use the MapLibre style spec docs website ([#353](https://github.com/maplibre/maplibre-native/pull/353))
- Refresh iOS & Android build docs ([5f679b55b](https://github.com/maplibre/maplibre-native/commit/5f679b55b))
- Qt build improvements and documentation ([#277](https://github.com/maplibre/maplibre-native/pull/277))
- Replace Mapbox with MapLibre in README titles ([#297](https://github.com/maplibre/maplibre-native/pull/297))
- Rename MaptilerFileSource to MBTilesFileSource ([#198](https://github.com/maplibre/maplibre-native/pull/198))
- Changed missed MapBox reference to MapLibre ([#253](https://github.com/maplibre/maplibre-native/pull/253))
- Implement map projection functionality ([#254](https://github.com/maplibre/maplibre-native/pull/254))
- chore: rename master -> main in CI and scripts ([#246](https://github.com/maplibre/maplibre-native/pull/246))
- Feature - Bring back node support ([#217](https://github.com/maplibre/maplibre-native/pull/217))
- Remove obsolete CI configurations ([#219](https://github.com/maplibre/maplibre-native/pull/219))

### 🐞 Bug fixes

- Fixes potential NaN when calling `NativeMapView::nativeMoveBy` ([#501](https://github.com/maplibre/maplibre-native/pull/501))
- Fix android ci workflows ([#476](https://github.com/maplibre/maplibre-native/pull/476))
- Fix typo in geo.cpp ([#412](https://github.com/maplibre/maplibre-native/pull/412))
- Fix render tests ([#351](https://github.com/maplibre/maplibre-native/pull/351))
- fix shiftY calculation typo ([#285](https://github.com/maplibre/maplibre-native/pull/285))
- [msvc] Fix warnings, mainly casting to smaller types ([#270](https://github.com/maplibre/maplibre-native/pull/270))
- Fix street label appearance while animating near zoom level threshhold ([#267](https://github.com/maplibre/maplibre-native/pull/267))

### ⛵ Dependencies

- Bump semver from 7.3.7 to 7.3.8 in /platform/android ([#530](https://github.com/maplibre/maplibre-native/pull/530))
- Bump to JDK 11 in android CI and generate Gradle Wrapper ([#474](https://github.com/maplibre/maplibre-native/pull/474))
- Bump ejs from 3.1.7 to 3.1.8 in /platform/android ([#470](https://github.com/maplibre/maplibre-native/pull/470))
- Upgrade Gradle from 3.6.3 to 3.6.4 ([#456](https://github.com/maplibre/maplibre-native/pull/456))
- Bump semver from 5.7.1 to 7.3.7 in /platform/android ([#461](https://github.com/maplibre/maplibre-native/pull/461))
- Bump pixelmatch from 4.0.2 to 5.3.0 in /platform/android ([#460](https://github.com/maplibre/maplibre-native/pull/460))
- Bump esm from 3.1.0 to 3.2.25 in /platform/android ([#463](https://github.com/maplibre/maplibre-native/pull/463))
- Bump ejs from 2.7.4 to 3.1.7 in /platform/android ([#299](https://github.com/maplibre/maplibre-native/pull/299))
- Bump lodash from 4.17.19 to 4.17.21 in /platform/android ([#195](https://github.com/maplibre/maplibre-native/pull/195))

## 9.5.2 - December 02, 2021

### Bug Fixes

- raster layers stopped working in Android [#161](https://github.com/maplibre/maplibre-native/pull/161)
- remove Mapbox's in favor of MapLibre in attribution dialog [#160](https://github.com/maplibre/maplibre-native/pull/160)
- queryRenderedFeatures returns wrong results - issue #184, [#147](https://github.com/maplibre/maplibre-native/pull/174)

## 9.5.1 - September 06, 2021

### Bug Fixes

- Invalid tile url template for MapLibre style [#107](https://github.com/maplibre/maplibre-native/pull/107)
- Crash when inflating view on Android [#130](https://github.com/maplibre/maplibre-native/pull/130)

### Other

- Fix perf regression in cached tiles of tile pyramid [#129](https://github.com/maplibre/maplibre-native/pull/129)
- Upgrade to newest protozero [#148](https://github.com/maplibre/maplibre-native/pull/148)
- Change NativeMapView.resizeView to use 0 instead of throwing IllegalArgumentException [#151](https://github.com/maplibre/maplibre-native/pull/151)

## 9.5.0 - June 29, 2021

### Features

- Replacing hardcoded configuration with configurable API and removing Mapbox assets and dependencies [#90](https://github.com/maplibre/maplibre-native/pull/90)

### Other

- Migration from bintray [#77](https://github.com/maplibre/maplibre-native/pull/77)

## 9.4.2 - May 6, 2021

### Other

Switching from mapbox-java libraries to [maplibre fork](https://github.com/maplibre/maplibre-java)

## 9.4.0 - March 12, 2021

### Bug Fixes

- Fixed an issue where symbols flickered when zooming out. [#16](https://github.com/maplibre/maplibre-native/issues/16)
- Fixes crash caused by NullPointerException MapKeyListener [#464](https://github.com/mapbox/mapbox-gl-native-android/issues/464), cherry picked from [#466](https://github.com/mapbox/mapbox-gl-native-android/pull/466)
- Fixed an issue where GPS puck keeps previous value when enabled. [#462]https://github.com/mapbox/mapbox-gl-native-android/issues/462, cherry picked from [#470](https://github.com/mapbox/mapbox-gl-native-android/pull/470)
- Not changing location camera mode while disabled. [#24](https://github.com/maplibre/maplibre-native/issues/24)
- Symbol layer flickering after zooming out. [#16](https://github.com/maplibre/maplibre-native/issues/16)
- Avoid throwing null pointer exception on slow initialization [#22](https://github.com/maplibre/maplibre-native/issues/22)

### Other

- mapbox-gl-js submodule has been replaced with maplibre-gl-js
- Removed Telemetry [#7](https://github.com/maplibre/maplibre-native/pull/7)

## 9.3.0 - January 6, 2021

### Features

- Added the mbtiles file source for rendering vector tiles from file stored locally on the device.

## 9.2.1 - May 15, 2020

### Improvements and bug fixes

- Added compiler and linker options to reduce binary size for release build. ([#382](https://github.com/mapbox/mapbox-gl-native-android/pull/382))
- Fixed a crash when `Layer.getFilter()` is used with the `distance` expression. ([#380](https://github.com/mapbox/mapbox-gl-native-android/pull/380))
- Reset AndroidRendererBackend properly to prevent a crash during rotation on an Android 4.2.2 x86 tablet. ([#381](https://github.com/mapbox/mapbox-gl-native-android/pull/381)（h/t [ystsoi](https://github.com/ystsoi))

## 9.2.0 - May 7, 2020

### Features

- Added the `distance` expression for calculating the shortest distance between an evaluated feature and a given GeoJSON object. ([#339](https://github.com/mapbox/mapbox-gl-native-android/pull/339))
- The [`SymbolLayer.iconTextFit`](https://docs.mapbox.com/android/api/map-sdk/9.2.0-beta.1/com/mapbox/mapboxsdk/style/layers/PropertyFactory.html#iconTextFit-com.mapbox.mapboxsdk.style.expressions.Expression-) property now respects the stretch metadata of any nine-part stretchable image passed into the `style.addImage` method. You can define the stretchable area of an image by configuring the `stretchX`, `stretchY`, `content` options on `Style.addImage` to append the requisite [metadata](https://github.com/mapbox/mapbox-gl-js/issues/8917). ([#314](https://github.com/mapbox/mapbox-gl-native-android/pull/314))
- Added methods to set and get the sort key of features in a `CircleLayer` at runtime. Features with a higher sort key will appear above features with a lower sort key. ([#322](https://github.com/mapbox/mapbox-gl-native-android/pull/322))
- Introduced `Source.setVolatile` to disable ambient caching of tiles from a source. Use this option on transient sources, such as traffic tilesets, if not showing tiles from a source is preferable to showing outdated tiles should an update request fail. ([#355](https://github.com/mapbox/mapbox-gl-native-android/pull/355))
- Introduced `Source.minimumTileUpdateInterval` to override the expiration time set via HTTP headers with a longer expiration time on the client. ([#355](https://github.com/mapbox/mapbox-gl-native-android/pull/355))
- Added `Source.maxOverscaleFactor` for defining how much a parent tile can be overscaled during rendering. This setting is only needed if a custom source on your map appears blurry or distorted when viewed at new zoom levels. ([#299](https://github.com/mapbox/mapbox-gl-native-android/pull/299))

### User location indicator

- Introduced an activation option to render the `LocationComponent` with a dedicated OpenGL layer through `LocationComponentActivationOptions#useSpecializedLocationLayer`, instead of the combined symbol and circle layer implementation used by default. ([#319](https://github.com/mapbox/mapbox-gl-native-android/pull/319))
- Introduced an option to add an animated pulsing circle around the `LocationComponent` through `LocationComponentOptions#pulseEnabled` and methods to customize it. ([#172](https://github.com/mapbox/mapbox-gl-native-android/pull/172))
- Introduced an option to set a screen area through `LocationComponentOptions#trackingMultiFingerProtectedMoveArea` where executed gestures will avoid breaking camera tracking when `LocationComponentOptions#trackingGesturesManagement` is enabled. ([#356](https://github.com/mapbox/mapbox-gl-native-android/pull/356))

### Improvements and bug fixes

- Improved the default compass engine's heading calculation by accounting for device orientation, ensuring accelerometer and magnetic field sensor events are retained, and pushing updates to the heading even if the sensor is unreliable. ([#338](https://github.com/mapbox/mapbox-gl-native-android/pull/338))（h/t [ystsoi](https://github.com/ystsoi))
- Correctly call the `onFling` listener only when the velocity of a gesture meets the threshold to animate the camera. ([#356](https://github.com/mapbox/mapbox-gl-native-android/pull/356))
- Enabled adding style images on-demand on `MapSnapshotter`. ([#317](https://github.com/mapbox/mapbox-gl-native-android/pull/317))
- Fixed a crash when changing the user interface in `MapSnapshotter.ErrorHandler`. ([#347](https://github.com/mapbox/mapbox-gl-native-android/pull/347))
- Fixed a crash when calling the `Style.removeImage` method with the name of a nonexistent image. ([#16391](https://github.com/mapbox/mapbox-gl-native/pull/16391))
- Fixed a crash when encountering an invalid polyline. ([#16409](https://github.com/mapbox/mapbox-gl-native/pull/16409))
- Certain logging statements no longer run on the main thread. ([#16325](https://github.com/mapbox/mapbox-gl-native/pull/16325))

### Other changes

- Rollback to `System.loadLibrary` to fix SoLoader fails to load the native library on x86 devices. ([#368](https://github.com/mapbox/mapbox-gl-native-android/pull/368))
- Worked around crashes caused by `JobIntentService` on devices >= API 26 by using a network request to report errors instead. ([#472](https://github.com/mapbox/mapbox-events-android/pull/472)

## 9.1.0 - April 7, 2020

### Features

- Added `within` expression for testing whether an evaluated feature lies within a given GeoJSON object. `within` accepts `Feature`, `FeatureCollection`, `Polygon`, and `MultiPolygon` as valid GeoJSON objects. ([#198](https://github.com/mapbox/mapbox-gl-native-android/pull/198))
- Added `in` expression for testing whether an item exists in an array or a substring exists in a string. ([#171](https://github.com/mapbox/mapbox-gl-native-android/pull/171))
- Introduced APIs and callbacks for customizing the style used in a `MapSnapshotter` snapshotter with runtime styling methods. ([#268](https://www.github.com/mapbox/mapbox-gl-native-android/pull/268), [#292](https://www.github.com/mapbox/mapbox-gl-native-android/pull/292))
- Added methods to set and get the sort key of features in a `FillLayer` and `LineLayer` at runtime. Features with a higher sort key will appear above features with a lower sort key. ([#209](https://github.com/mapbox/mapbox-gl-native-android/pull/209))
- Introduced `MapboxMap.setMaxPitchPreference` and `MapboxMap.setMinPitchPreference` to constrain a map's pitch. ([#199](https://github.com/mapbox/mapbox-gl-native-android/pull/199))
- Introduced ability to configure the zoom level(s) that tiles are prefetched from by `Source`. This overrides the tile prefetch setting defined by the `Map` instance. ([#184](https://github.com/mapbox/mapbox-gl-native-android/pull/184))

### Performance improvements

- Increased stability of label placement when the map is tilted. ([#16287](https://github.com/mapbox/mapbox-gl-native/pull/16287))
- Improved the performance of loading a style that has many style images. ([#16187](https://github.com/mapbox/mapbox-gl-native/pull/16187))
- Fixed a memory leak during zooming when `MapboxMapOptions.debugActive` is enabled. ([#15179](https://github.com/mapbox/mapbox-gl-native/issues/15179), fixed by [#15395](https://github.com/mapbox/mapbox-gl-native/pull/15395))

### Bug fixes

- Downloaded offline packs no longer reduce the storage space available for ambient caching of tiles and other resources. ([#15622](https://github.com/mapbox/mapbox-gl-native/pull/15622))
- Fixed conversion of a `Map` object into a GeoJSON object. ([#16243](https://github.com/mapbox/mapbox-gl-native/pull/16243))
- Fixed issues that would cause an offline region to stop downloading before completion. ([#16230](https://github.com/mapbox/mapbox-gl-native/pull/16230), [#16240](https://github.com/mapbox/mapbox-gl-native/pull/16240))
- Fixed an issue where a `LineDasharray` value of `[1, 0]` resulted in hairline gaps. ([#16202](https://github.com/mapbox/mapbox-gl-native/pull/16202))
- Fixed Proguard configuration to prevent obfuscation of telemetry event classes. ([#306](https://github.com/mapbox/mapbox-gl-native-android/pull/306))
- Fixed a crash that might occur during EGL window surface creation. ([#278](https://github.com/mapbox/mapbox-gl-native-android/pull/278))
- Fixed a bug that would cause local CJK glyphs to be rendered incorrectly when generated on a device with the high-contrast accessibility setting enabled. ([#289](https://github.com/mapbox/mapbox-gl-native-android/pull/289))

### Other changes

- Updated mapbox-events-android to v5.0.0. ([#294](https://github.com/mapbox/mapbox-gl-native-android/pull/294))

## 9.0.1 - March 11, 2020

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v9.0.0...android-v9.0.1) since [Mapbox Maps SDK for Android v9.0.0](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v9.0.0):

### Bug fixes

- Fix Proguard configuration to prevent obfuscation of telemetry event classes. [#195](https://github.com/mapbox/mapbox-gl-native-android/pull/195)

## 8.6.3 - March 5, 2020

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.6.2...android-v8.6.3) since [Mapbox Maps SDK for Android v8.6.2](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.6.2):

### Bug fixes

- Fix Proguard configuration to prevent obfuscation of telemetry event classes. [#195](https://github.com/mapbox/mapbox-gl-native-android/pull/195)

## 9.0.0 - February 13, 2020

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v9.0.0-beta.1...android-v9.0.0) since [Mapbox Maps SDK for Android v9.0.0-beta.1](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v9.0.0-beta.1):

### Features

- Introduce lazy loading of view components on MapView [#141](https://github.com/mapbox/mapbox-gl-native-android/pull/141)
- Update Mapbox dependencies to AndroidX [#144](https://github.com/mapbox/mapbox-gl-native-android/pull/144) [#155](https://github.com/mapbox/mapbox-gl-native-android/pull/155)

### Bug fixes

- Fixed a bug that would cause `symbol-sort-key` not equally sort icon and text [#16023](https://github.com/mapbox/mapbox-gl-native/pull/16023)
- Revert a commit that appended sku token to offline download requests [#173](https://github.com/mapbox/mapbox-gl-native-android/pull/173)

## 9.0.0-beta.1 - January 24, 2020

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v9.0.0-alpha.1...android-v9.0.0-beta.1) since [Mapbox Maps SDK for Android v9.0.0-alpha.1](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v9.0.0-alpha.1):

### Features

- Introduce a gesture setting to disable horizontal scrolling [#156](https://github.com/mapbox/mapbox-gl-native-android/pull/156)
- Introduce pixel ratio as public API on MapView [#146](https://github.com/mapbox/mapbox-gl-native-android/pull/146)
- Introduce getVisibleCoordinateBounds method that allows for object reuse [#109](https://github.com/mapbox/mapbox-gl-native-android/pull/109)
- Update Mapbox dependencies to AndroidX [#136](https://github.com/mapbox/mapbox-gl-native-android/pull/136)

## 8.6.2 - January 22, 2020

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.6.1...android-v8.6.2) since [Mapbox Maps SDK for Android v8.6.1](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.6.1):

### Bug fixes

- Append sku token to offline download requests [#148](https://github.com/mapbox/mapbox-gl-native-android/pull/148)
- Write user id synchronously to avoid null entries as part of the URL [#145](https://github.com/mapbox/mapbox-gl-native-android/pull/145)

## 8.6.1 - January 9, 2020

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.6.0...android-v8.6.1) since [Mapbox Maps SDK for Android v8.6.0](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.6.0):

### Bug fixes

- Fixed native crash when using a GeoJsonSource when destructing the underlying GeoJSONVTData [#16106](https://github.com/mapbox/mapbox-gl-native/pull/16106).

## 9.0.0-alpha.1 - January 8, 2020

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.6.0...android-v9.0.0-alpha.1) since [Mapbox Maps SDK for Android v8.6.0](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.6.0):

### Features

- Update codebase to androidX. [#129](https://github.com/mapbox/mapbox-gl-native-android/pull/129)

### Bug fixes

- Deprecated `MapboxMap#cycleDebugOptions` and fixed an `UnsatisfiedLinkError` when accessed. [#104](https://github.com/mapbox/mapbox-gl-native-android/pull/104)
- Fixed a null pointer issue on the public static getters of `Mapbox.java`, in case the user hasn't called `Mapbox.getInstance()`. [#128](https://github.com/mapbox/mapbox-gl-native-android/pull/128)

## 8.6.0 - December 20, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.6.0-beta.1...android-v8.6.0) since [Mapbox Maps SDK for Android v8.6.0-beta.1](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.6.0-beta.1):

### Bug fixes

- Revert of a commit that resulted in a native crash when cycle debug options was invoked, issue was only impacting v8.6.0-beta.1 [#16074](https://github.com/mapbox/mapbox-gl-native/pull/16074)
- Fixed a crash caused by an unintentional minification of the `LocalGlyphRasterizer`. The full class definition is now kept when minification is used by end developer [#102](https://github.com/mapbox/mapbox-gl-native-android/pull/102)
- Fixed intermittent offline download failures by clearing the batch buffer if a download is interrupted. [#16074](https://github.com/mapbox/mapbox-gl-native/pull/16074)
- Fixed error for excessively overscaled lines [#16045](https://github.com/mapbox/mapbox-gl-native/pull/16045)
- Fixed signed to unsigned implicit conversion errors in symbol placement. [#16008](https://github.com/mapbox/mapbox-gl-native/pull/16008)
- Fixed error that would result when 0.0 was passed into `text-size` [#16080](https://github.com/mapbox/mapbox-gl-native/pull/16080)

## 8.6.0-beta.1 - December 6, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.6.0-alpha.2...android-v8.6.0-beta.1) since [Mapbox Maps SDK for Android v8.6.0-alpha.2](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.6.0-alpha.2):

### Features

- Introduce OfflineManager.runPackDatabaseAutomatically(boolean) and remove the redundant OfflineRegion.deleteAndSkipPackDatabase() method [#78](https://github.com/mapbox/mapbox-gl-native-android/pull/78)
- Expose getter for the view used for rendering OpenGL content on [#87](https://github.com/mapbox/mapbox-gl-native-android/pull/87)
- Bulk conversion of geographic coordinates to screen coordinate using reusable double arrays. [#91](https://github.com/mapbox/mapbox-gl-native-android/pull/91)

### Performance improvements

- Make network requests for expired resources lower priority than requests for new resources. ([#15950](https://github.com/mapbox/mapbox-gl-native/pull/15950))

### Bug fixes

- Fixed a runtime crash of Proguard optimized builds on < API 21 devices by including the missing SoLoader flag. [#71](https://github.com/mapbox/mapbox-gl-native-android/pull/71)
- Fixed a bug where location camera mode transition callback would not get notified if the requested mode was already set. [#80](https://github.com/mapbox/mapbox-gl-native-android/pull/80)
- Fixed a bug where location tilt/zoom while tracking callback would not get notified if the request was ignored. [#83](https://github.com/mapbox/mapbox-gl-native-android/pull/83)
- Use -Oz to reduce binary size [#15984](https://github.com/mapbox/mapbox-gl-native/pull/15984)
- Use icf=all compile flag to reduce binary size [#15987](https://github.com/mapbox/mapbox-gl-native/pull/15987)

## 8.6.0-alpha.2 - November 22, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.6.0-alpha.1...android-v8.6.0-alpha.2) since [Mapbox Maps SDK for Android v8.6.0-alpha.1](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.6.0-alpha.1):

### Bug fixes

- Synchronise LibaryLoader#loadLibrary to avoid race conditions resulting in UnsatisfiedLinkError [#58](https://github.com/mapbox/mapbox-gl-native-android/pull/58)

## 8.5.2 - December 10, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.5.1...android-v8.5.2) since [Mapbox Maps SDK for Android v8.5.1](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.5.1):

### Features

- Enabling decoupling vacuum operation from other operations run on the offline database file (delete offline region, clear ambient cache) [#99](https://github.com/mapbox/mapbox-gl-native-android/pull/99)

## 8.5.1 - November 20, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.5.0...android-v8.5.1) since [Mapbox Maps SDK for Android v8.5.0](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.5.0):

### Bugs

- Fixed Geo JSON source flickering on style transition [#15907](https://github.com/mapbox/mapbox-gl-native/pull/15907)
- Fixed flickering caused by unnecessary removing and re-adding of the render sources when the order of their corresponding style objects was changed in the updated style [#15941](https://github.com/mapbox/mapbox-gl-native/pull/15941)

## 8.6.0-alpha.1 - November 14, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.5.0...android-v8.6.0-alpha.1) since [Mapbox Maps SDK for Android v8.5.0](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.5.0):

### Features

- Added support for [image expression](https://docs.mapbox.com/mapbox-gl-js/style-spec/#expressions-types-image) in core library. Runtime APIs for image expression will be implemented separately. [#15877](https://github.com/mapbox/mapbox-gl-native/pull/15877)
- Introduce `OfflineManager#packDatabase` and `OfflineRegion#deleteAndSkipPackDatabase` API in order to decouple offline storage vacuum and delete region operations and thus to gain performance benefits e.g. when several regions should be deleted in a row [#15899](https://github.com/mapbox/mapbox-gl-native/pull/15899)

### Bugs

- Fixed Geo JSON source flickering on style transition [#15907](https://github.com/mapbox/mapbox-gl-native/pull/15907)

### Performance improvements

- Convert GeoJSON features to tiles for the loaded source description in a background thread and thus unblock the UI thread [#15885](https://github.com/mapbox/mapbox-gl-native/pull/15885)

## 8.5.0 - November 8, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.5.0-beta.1...android-v8.5.0) since [Mapbox Maps SDK for Android v8.5.0-beta.1](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.5.0-beta.1):

### Bug fixes

- Fixed the rendering bug caused by redundant pending requests for already requested images [#15864](https://github.com/mapbox/mapbox-gl-native/pull/15864)
- Use individual textures for line dash patterns [#15862](https://github.com/mapbox/mapbox-gl-native/pull/15862)

## 8.5.0-beta.1 - November 1, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.5.0-alpha.4...android-v8.5.0-beta.1) since [Mapbox Maps SDK for Android v8.5.0-alpha.4](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.5.0-alpha.4):

## 8.5.0-alpha.4 - October 24, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native-android/compare/android-v8.5.0-alpha.3...android-v8.5.0-alpha.4) since [Mapbox Maps SDK for Android v8.5.0-alpha.3](https://github.com/mapbox/mapbox-gl-native-android/releases/tag/android-v8.5.0-alpha.3):

### Bug fixes

- Add isLayerReady check in LocationComponent before updating compass listener. [#19](https://github.com/mapbox/mapbox-gl-native-android/pull/19)
- Update soLoader to version 0.8.0. [#23](https://github.com/mapbox/mapbox-gl-native-android/pull/23)
- Enable incremental vacuum for Offline DB [#15837](https://github.com/mapbox/mapbox-gl-native/pull/15837)
- Revert coalescing requests to the client for missing images [#15852](https://github.com/mapbox/mapbox-gl-native/pull/15852)
- Avoid edges for labels that use text-variable-anchors [#15854](https://github.com/mapbox/mapbox-gl-native/pull/15854)

## 8.5.0-alpha.3 - October 18, 2019

### Bug fixes

- Fix runtime exceptions that occurred when a manually built camera object without padding was serialized [#15788](https://github.com/mapbox/mapbox-gl-native/pull/15788)
- Fix icon-text-fit to take in account the additional 1px texture padding when scaling [#15634](https://github.com/mapbox/mapbox-gl-native/pull/15634)
- Fix occasional crashes during downloading thousands of offline region tiles [#15818](https://github.com/mapbox/mapbox-gl-native/pull/15818)

### Project

- Moved project location from `mapbox-gl-native` to `mapbox-gl-native-android`

## 8.5.0-alpha.2 - October 10, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.5.0-alpha.1...android-v8.5.0-alpha.2) since [Mapbox Maps SDK for Android v8.5.0-alpha.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.5.0-alpha.1):

### Features

- Expose pre-fetching zoom delta, allows to granular control the delta on which we prefetch tiles [#15769](https://github.com/mapbox/mapbox-gl-native/pull/15769)

### Performance improvements

- Improved rendering performance for the styles with multiple sources [#15756](https://github.com/mapbox/mapbox-gl-native/pull/15756)

### Bug fixes

- Fixed runtime exceptions that occurred when a manually built camera object without padding was serialized. [#15788](https://github.com/mapbox/mapbox-gl-native/pull/15788)
- Keep Mapbox when obfuscating code with proguard [#15762](https://github.com/mapbox/mapbox-gl-native/pull/15762)

## 8.5.0-alpha.1 - October 3, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.4.0...android-v8.5.0-alpha.1) since [Mapbox Maps SDK for Android v8.4.0](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.4.0):

### Bug fixes

- Suppress network requests for expired tiles update, if these tiles are invisible. [#15741](https://github.com/mapbox/mapbox-gl-native/pull/15741)
- Fixed opacity interpolation for composition expressions [#15738](https://github.com/mapbox/mapbox-gl-native/pull/15738)
- Fixed an issue where `Projection#getMetersPerPixelAtLatitude` returned a value incorrectly divided by the pixel ratio. This also fixes an issue where `LocationComponent` accuracy circle's radius was artificially increased. [#15742](https://github.com/mapbox/mapbox-gl-native/pull/15742)
- Coalesce requests to the client for the same missing image [#15778](https://github.com/mapbox/mapbox-gl-native/pull/15778)

## 8.4.0 - September 25, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.4.0-beta.1...android-v8.4.0) since [Mapbox Maps SDK for Android v8.4.0-beta.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.4.0-beta.1):

### Bug fixes

- Fixing a crash when `CameraPosition` padding is set when using `MapSnapshotter`. [#15699](https://github.com/mapbox/mapbox-gl-native/pull/15699)

### Other Changes

- Bumped Java SDK dependency to stable 4.9.0 [#15700](https://github.com/mapbox/mapbox-gl-native/pull/15700)
- Migrate map feedback to GL JS–powered feedback form [#15623](https://github.com/mapbox/mapbox-gl-native/pull/15623)

## 8.4.0-beta.1 - September 19, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.4.0-alpha.2...android-v8.4.0-beta.1) since [Mapbox Maps SDK for Android v8.4.0-alpha.2](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.4.0-alpha.2):

### Bug fixes

- Fixed an issue that `maxzoom` in style `Sources` option was ignored when URL resource is provided. It may cause problems such as extra tiles downloading at higher zoom level than `maxzoom`, or problems that wrong setting of `overscaledZ` in `OverscaledTileID` that will be passed to `SymbolLayout`, leading wrong rendering appearance. [#15581](https://github.com/mapbox/mapbox-gl-native/pull/15581)
- Fixed constant repainting for the sources with invisible layers, caused by `RenderSource::hasFadingTiles()` returning `true` all the time. [#15600](https://github.com/mapbox/mapbox-gl-native/pull/15600)
- Fixed an issue that caused the state of CompassView not up to date when `UiSettings.setCompassEnabled()` is set to true. [#15606](https://github.com/mapbox/mapbox-gl-native/pull/15606)
- Ignore location tracking mode based animations when the camera is transitioning. [#15641](https://github.com/mapbox/mapbox-gl-native/pull/15641)
- Fixed MapSnapshotter so that `MapSnapshotter.withApiBaseUri` works again. [#15642](https://github.com/mapbox/mapbox-gl-native/pull/15642)
- Fixed an assertion hit caused by possibility of adding a layer to an incompatible source. [#15644](https://github.com/mapbox/mapbox-gl-native/pull/15644)

## 8.3.2 - September 19, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.3.1...android-v8.3.2) since [Mapbox Maps SDK for Android v8.3.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.3.1):

### Bug fixes

- Fixed an issue of integer overflow when converting `tileCoordinates` to `LatLon`, which caused issues such as `queryRenderedFeatures` and `querySourceFeatures` returning incorrect coordinates at zoom levels 20 and higher. [#15560](https://github.com/mapbox/mapbox-gl-native/pull/15560)

## 8.3.1 - September 18, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.3.0...android-v8.3.1) since [Mapbox Maps SDK for Android v8.3.0](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.3.0):

### Bug fixes

- Updated earcut.hpp submodule file [#15660](https://github.com/mapbox/mapbox-gl-native/pull/15660)
- Fixed a wrong variable assignment of `MapSnapshotter.Options.withApiBaseUri`. [#15642](https://github.com/mapbox/mapbox-gl-native/pull/15642)

## 8.4.0-alpha.2 - September 11, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.4.0-alpha.1...android-v8.4.0-alpha.2) since [Mapbox Maps SDK for Android v8.4.0-alpha.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.4.0-alpha.1):

### Performance improvements

- Newly loaded labels appear faster on the screen. [#15308](https://github.com/mapbox/mapbox-gl-native/pull/15308)

### Bug fixes

- Fixed an issue of integer overflow when converting `tileCoordinates` to `LatLon`, which caused issues such as `queryRenderedFeatures` and `querySourceFeatures` returning incorrect coordinates at zoom levels 20 and higher. [#15560](https://github.com/mapbox/mapbox-gl-native/pull/15560)

### Other Changes

- Bumped Java SDK dependency to 4.9.0-alpha.1 [#15570](https://github.com/mapbox/mapbox-gl-native/pull/15570)

## 8.4.0-alpha.1 - September 4, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.3.0...android-v8.4.0-alpha.1) since [Mapbox Maps SDK for Android v8.3.0](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.3.0):

### Features

- Introduce `clusterProperties` option for aggregated cluster properties. [#15425](https://github.com/mapbox/mapbox-gl-native/pull/15425)
- Expose the `CameraPosition#padding` field and associated utility camera position builders. This gives a choice to set a persisting map padding immediately during a transition instead of setting it lazily `MapboxMap#setPadding`, which required scheduling additional transition to be applied. This also deprecates `MapboxMap#setPadding` as there should be no need for a lazy padding setter. [#15444](https://github.com/mapbox/mapbox-gl-native/pull/15444)
- Add number-format expression that allows to format a number to a string, with configurations as minimal/maximal fraction and locale/currency. [#15424](https://github.com/mapbox/mapbox-gl-native/pull/15424)
- Enable using of `text-offset` option together with `text-variable-anchor` (if `text-radial-offset` option is not provided). If used with `text-variable-anchor`, input values will be taken as absolute values. Offsets along the x- and y-axis will be applied automatically based on the anchor position. [#15542](https://github.com/mapbox/mapbox-gl-native/pull/15542)

### Bug fixes

- Fixed a rendering issue of `collisionBox` when `text-translate` or `icon-translate` is enabled. [#15467](https://github.com/mapbox/mapbox-gl-native/pull/15467)
- Fixed unnecessary layers blink when the same components are re-added after the style reload [#15513](https://github.com/mapbox/mapbox-gl-native/pull/15513)
- Fixed an issue in JNI marshaling code that converted a float to long that produced invalid expressions [#15557](https://github.com/mapbox/mapbox-gl-native/pull/15557)
- Call callback onFinish() immediately when trying to move camera to current position [#15558](https://github.com/mapbox/mapbox-gl-native/pull/15558)

### Performance improvements

- Mark used offline region resources in batches. [#15521](https://github.com/mapbox/mapbox-gl-native/pull/15521)

### Other Changes

- JNI binding refactor [#15462](https://github.com/mapbox/mapbox-gl-native/pull/15462)
- Javadoc changes to clarify OfflineManager methods [#15519](https://github.com/mapbox/mapbox-gl-native/pull/15519)

## 8.3.0 - August 28, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.3.0-beta.1...android-v8.3.0) since [Mapbox Maps SDK for Android v8.3.0-beta.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.3.0-beta.1):

This release changes how offline tile requests are billed — they are now billed on a pay-as-you-go basis and all developers are able raise the offline tile limit for their users. Offline requests were previously exempt from monthly active user (MAU) billing and increasing the offline per-user tile limit to more than 6,000 tiles required the purchase of an enterprise license. By upgrading to this release, you are opting into the changes outlined in [this blog post](https://blog.mapbox.com/offline-maps-for-all-bb0fc51827be) and [#15380](https://github.com/mapbox/mapbox-gl-native/pull/15380).

### Features

- Allow ability to pass a string array resource into `localIdeographFontFamily` for enabling/disabling the feature and specifying fallback fonts. [#15488](https://github.com/mapbox/mapbox-gl-native/pull/15488)

### Bug fixes

- Fixed a rendering issue caused by all icons being treated as SDFs if an SDF and non-SDF icon were in the same layer. [#15456](https://github.com/mapbox/mapbox-gl-native/pull/15456)
- Fixed an issue where changing location's render mode when the`LocationComponent` is disable wouldn't invalidate the foreground icon when it's back enabled. [#15507](https://github.com/mapbox/mapbox-gl-native/pull/15507)

## 8.2.2 - August 23, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.2.1...android-v8.2.2) since [Mapbox Maps SDK for Android v8.2.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.2.1):

### Bug fixes

- Fixed a possible crash that could be caused by invoking the wrong layer implementation casting function. [#15398](https://github.com/mapbox/mapbox-gl-native/pull/15398)
- Fixed use of objects after moving, potentially causing crashes. [#15408](https://github.com/mapbox/mapbox-gl-native/pull/1540)
- Fixed a wrong calculation of visibility in high pitch scenarios which might've led to tiles in the viewport not being rendered. [#15461](https://github.com/mapbox/mapbox-gl-native/pull/15461)

### Other Changes

- Do not invalidate the camera immediately when map padding is set. Reverts a change introduced in the `v8.2.0`. [#15437](https://github.com/mapbox/mapbox-gl-native/pull/15437)

## 8.3.0-beta.1 - August 22, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.3.0-alpha.3...android-v8.3.0-beta.1) since [Mapbox Maps SDK for Android v8.3.0-alpha.3](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.3.0-alpha.3):

### Bug fixes

- Fixed a bug where glyphs generated through the LocalGlyphRasterizer interface were changing fonts during some zoom changes. [#15407](https://github.com/mapbox/mapbox-gl-native/pull/15407)
- Fixed use of objects after moving, potentially causing crashes. [#15408](https://github.com/mapbox/mapbox-gl-native/pull/15408)
- Fixed a possible crash that could be caused by invoking the wrong layer implementation casting function [#15398](https://github.com/mapbox/mapbox-gl-native/pull/15398).
- Font lookup on pre lollipop devices failed, provide default font list instead [#15410](https://github.com/mapbox/mapbox-gl-native/pull/15410).
- Fixed rendering and collision detection issues with using `text-variable-anchor` and `icon-text-fit` properties on the same layer [#15367](https://github.com/mapbox/mapbox-gl-native/pull/15367).
- Fixed a bug where quick-scale was registered during a move gesture that followed a double-tap. Bumped gestures library to `v0.5.1`. [#15427](https://github.com/mapbox/mapbox-gl-native/pull/15427)
- Fixed symbol overlap when zooming out quickly. [#15416](https://github.com/mapbox/mapbox-gl-native/pull/15416)

### Other Changes

- Do not invalidate the camera immediately when map padding is set. Reverts a change introduced in the `v8.2.0`. [#15437](https://github.com/mapbox/mapbox-gl-native/pull/15437)

### Docs

- Add javadoc to OnStyleImageMissingListener indicating the required synchronous addition images, document workaround [#15418](https://github.com/mapbox/mapbox-gl-native/pull/15418)

## 8.3.0-alpha.3 - August 15, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.3.0-alpha.2...android-v8.3.0-alpha.3) since [Mapbox Maps SDK for Android v8.3.0-alpha.2](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.3.0-alpha.2):

### Gestures handling

- Bumped gestures library version to v0.5.0 which replaces the compat gesture detector with a custom scale gesture detector implementation. This is a softly breaking change because the underlying scale gesture detector reference has been removed. This brings a set of improvements to gestures without removing any functionality from the previous implementation. [#15136](https://github.com/mapbox/mapbox-gl-native/pull/15136)
- Improved scale and rotate gestures by refining sensitivity thresholds and animation velocity. [#15136](https://github.com/mapbox/mapbox-gl-native/pull/15136)
- Improved quick-zoom gestures by making zoom changes linear. Quick-zoom scale changes are now executed based on y-axis delta changes. [#15386](https://github.com/mapbox/mapbox-gl-native/pull/15386)

### Features

- Introduced the text-writing-mode layout property for symbol layers. This property allows labels to be placed horizontally or vertically. The property accepts an array of enumeration values from a ( horizontal | vertical ) set. [#14932](https://github.com/mapbox/mapbox-gl-native/pull/14932)
- Added support for fallback fonts in `localIdeographFontFamily`. [#15255](https://github.com/mapbox/mapbox-gl-native/pull/15255)

### Bug fixes

- Fixed an issue where it was possible to set the map’s content insets then tilt the map enough to see the horizon, causing performance issues [#15195](https://github.com/mapbox/mapbox-gl-native/pull/15195)
- Allow loading of a map without a style URI or style JSON [#15293](https://github.com/mapbox/mapbox-gl-native/pull/15293)
- Fixed an issue where animated camera transitions zoomed in or out too dramatically [#15281](https://github.com/mapbox/mapbox-gl-native/pull/15281)
- Enable variable label placement when `text-allow-overlap` property is set to true [#15354](https://github.com/mapbox/mapbox-gl-native/pull/15354)

## 8.3.0-alpha.2 - August 7, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.3.0-alpha.1...android-v8.3.0-alpha.2) since [Mapbox Maps SDK for Android v8.3.0-alpha.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.3.0-alpha.1):

### Bug fixes

- Load a style without an URI or JSON [#15293](https://github.com/mapbox/mapbox-gl-native/pull/15293)
- Do not try to wake up the RunLoop if a wake is already pending. Fixes offline downloads that could freeze after resuming. [#15330](https://github.com/mapbox/mapbox-gl-native/pull/15330)

## 8.0.2 - July 31, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.0.1...android-v8.0.2) since [Mapbox Maps SDK for Android v8.0.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.0.1):

### Bug fixes

- Fixed rendering layers after fill-extrusion regression caused by optimization of fill-extrusion rendering [#15065](https://github.com/mapbox/mapbox-gl-native/pull/15065)
- Fixed a map update bug caused by the render tiles and the render passes getting unsynchronized [#15092](https://github.com/mapbox/mapbox-gl-native/pull/15092)
- Fixed a custom geometry source bug caused by using the outdated tiles after style update [#15112](https://github.com/mapbox/mapbox-gl-native/pull/15112)
- Ensure location shadow's gradient radius is greater than 0 [#15099](https://github.com/mapbox/mapbox-gl-native/pull/15099)

## 7.4.1 - July 31, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v7.4.0...android-v7.4.1) since [Mapbox Maps SDK for Android v7.4.0](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v7.4.0):

### Bug fixes

- Fixed a map update bug caused by the render tiles and the render passes getting unsynchronized [#15092](https://github.com/mapbox/mapbox-gl-native/pull/15092)
- Ensure location shadow's gradient radius is greater than 0 [#15099](https://github.com/mapbox/mapbox-gl-native/pull/15099)
- Fixed a custom geometry source bug caused by using the outdated tiles after style update [#15112](https://github.com/mapbox/mapbox-gl-native/pull/15112)

## 8.3.0-alpha.1 - July 31, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.2.0...android-v8.3.0-alpha.1) since [Mapbox Maps SDK for Android v8.2.0](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.2.0):

### Features

- Do not include CJK ideographs in the offline packs by default. This decreases overall offline download size but changes appearance by a default set local glyph generation [#14269](https://github.com/mapbox/mapbox-gl-native/pull/14269)
- Update target SDK to 28, update support library and fix gradle warnings [#15135](https://github.com/mapbox/mapbox-gl-native/pull/15135)
- Introduce VertexVector::extend() and use it in placement code [#15194](https://github.com/mapbox/mapbox-gl-native/pull/15194)

### Bug fixes

- Save location animation timestamp only when fed. Fixes an issue where external camera updates impacted location animation duration[#15265](https://github.com/mapbox/mapbox-gl-native/pull/15265)
- Fixed flickering on style change for the same tile set [#15127](https://github.com/mapbox/mapbox-gl-native/pull/15127)
- Fix location render/camera modes being reinitialized even when the new mode is the same [#15266](https://github.com/mapbox/mapbox-gl-native/pull/15266)
- Ensure default local ideographs font family is not overwrote. Fix a bug that prevented local CJK glyphs generation. The local generation is now enabled by default and the font family used for generation is set to "sans-serif. [#15253](https://github.com/mapbox/mapbox-gl-native/pull/15253)
- Fix int overflow issue in GridIndex [#15245](https://github.com/mapbox/mapbox-gl-native/pull/15245)
- Align gesture animation reason for onCameraMoveStarted [#15218](https://github.com/mapbox/mapbox-gl-native/pull/15218)
- Remove layers first when clearing the style, fixes unnecessary log dumps [#15191](https://github.com/mapbox/mapbox-gl-native/pull/15191)
- Remove unused field from icon buffer [#15189](https://github.com/mapbox/mapbox-gl-native/pull/15189)

### Performance improvements

- Release quad data after vertex buffers are created [#15189](https://github.com/mapbox/mapbox-gl-native/pull/15189)
- Decrease size of SymbolBucket [#15178](https://github.com/mapbox/mapbox-gl-native/pull/15178)
- Avoid geometry collections copying [#15201](https://github.com/mapbox/mapbox-gl-native/pull/15201)

## 8.2.1 - July 31, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.2.0...android-v8.2.1) since [Mapbox Maps SDK for Android v8.2.0](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.2.0):

### Bug fixes

- Save location animation timestamp only when fed [#15265](https://github.com/mapbox/mapbox-gl-native/pull/15265)

## 8.1.1 - July 26, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.1.0...android-v8.1.1) since [Mapbox Maps SDK for Android v8.1.0](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.1.0):

### Bug fixes

- Fixed rendering layers after fill-extrusion regression caused by optimization of fill-extrusion rendering [#15065](https://github.com/mapbox/mapbox-gl-native/pull/15065)
- Fixed a map update bug caused by the render tiles and the render passes getting unsynchronized [#15092](https://github.com/mapbox/mapbox-gl-native/pull/15092)
- Fixed collision with content insets [#15130](https://github.com/mapbox/mapbox-gl-native/pull/15130)
- Fixed a custom geometry source bug caused by using the outdated tiles after style update [#15112](https://github.com/mapbox/mapbox-gl-native/pull/15112)
- Allow map panning after quick zoom is disabled but a phantom gesture is executed [#15093](https://github.com/mapbox/mapbox-gl-native/pull/15093)
- Ensure location shadow's gradient radius is greater than 0 [#15099](https://github.com/mapbox/mapbox-gl-native/pull/15099)
- Ensure that move detector is enabled if double-tap is interrupted [#15103](https://github.com/mapbox/mapbox-gl-native/pull/15103)

## 8.2.0 - July 24, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.2.0-beta.1...android-v8.2.0) since [Mapbox Maps SDK for Android v8.2.0-beta.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.2.0-beta.1):

### Bug fixes

- Fix rendering of layers that are on top of fill-extrusion layers [#15065](https://github.com/mapbox/mapbox-gl-native/pull/15065)

## 8.2.0-beta.1 - July 18, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.2.0-alpha.3...android-v8.2.0-beta.1) since [Mapbox Maps SDK for Android v8.2.0-alpha.3](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.2.0-alpha.3):

### Bug fixes

- Fix `CustomGeometrySource` bug caused by usage of outdated tiles after a style update [#15112](https://github.com/mapbox/mapbox-gl-native/pull/15112)
- Fix bug where previous anchors set in `text-variable-anchor` were still preferred after anchor options were reset [#15129](https://github.com/mapbox/mapbox-gl-native/pull/15129)
- Fix issue where annotations could not be selected due to a regression in content insets [#15130](https://github.com/mapbox/mapbox-gl-native/pull/15130)

## 8.2.0-alpha.3 - July 11, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.2.0-alpha.2...android-v8.2.0-alpha.3) since [Mapbox Maps SDK for Android v8.2.0-alpha.2](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.2.0-alpha.2):

### Bug fixes

- Fix map update bug caused by render tiles and render passes becoming unsynchronized [#15092](https://github.com/mapbox/mapbox-gl-native/pull/15092)
- Fix fill-extrusion layers always rendered above other layers [#15065](https://github.com/mapbox/mapbox-gl-native/pull/15065)
- Fix a bug where map panning would be blocked after a quick zoom gesture was attempted when quick zoom is set to be ignored by `UiSettings` preferences [#15093](https://github.com/mapbox/mapbox-gl-native/pull/15093)
- Fix a bug where map panning would be blocked if double-tap is interrupted [#15103](https://github.com/mapbox/mapbox-gl-native/pull/15103)

### Minor features and improvements

- Improve performance of queryRenderedFeatures by removing unnecessary iterations over vectors and unneeded layers [#14930](https://github.com/mapbox/mapbox-gl-native/pull/14930)
- Decrease the minimum pixel distance between the current camera position and the target required to interpolate during camera animation [#15073](https://github.com/mapbox/mapbox-gl-native/pull/15073)
- Introduce `MapboxMapOptions` facility creation method [#15069](https://github.com/mapbox/mapbox-gl-native/pull/15069)
- Throw configuration exception when accessing Mapbox services with an invalid token [#15081](https://github.com/mapbox/mapbox-gl-native/pull/15081)
- Allow setting a null access token in the runtime [#15081](https://github.com/mapbox/mapbox-gl-native/pull/15081)
- Ensure location shadow's gradient radius is greater than 0 [#15099](https://github.com/mapbox/mapbox-gl-native/pull/15099)

### Other Changes

- Update okhttp to latest stable version that support pre lollipop (`3.12.3`) [#15087](https://github.com/mapbox/mapbox-gl-native/pull/15087)
- Fix up "match" expressions docs with stop arguments [#15102](https://github.com/mapbox/mapbox-gl-native/pull/15102)

## 8.2.0-alpha.2 - July 3, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.2.0-alpha.1...android-v8.2.0-alpha.2) since [Mapbox Maps SDK for Android v8.2.0-alpha.1](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.2.0-alpha.1):

### Major features

- Cache management API [#14962](https://github.com/mapbox/mapbox-gl-native/pull/14962)

### Bug fixes

- Fix style change transition regression caused by delayed setting of the updated layer properties [#15016](https://github.com/mapbox/mapbox-gl-native/pull/15016)

### Minor features and improvements

- Include Hiragana and Katakana glyph ranges to LocalGlyphRasterizer [#15009](https://github.com/mapbox/mapbox-gl-native/pull/15009)

### Other Changes

- Update CI image with NDK r20 [#15005](https://github.com/mapbox/mapbox-gl-native/pull/15005)

## 8.2.0-alpha.1 - June 26, 2019

[Changes](https://github.com/mapbox/mapbox-gl-native/compare/android-v8.1.0...android-v8.2.0-alpha.1) since [Mapbox Maps SDK for Android v8.1.0](https://github.com/mapbox/mapbox-gl-native/releases/tag/android-v8.1.0):

### Bug fixes

- Fix `queryRenderedFeatues` bug caused by incorrect sort feature index calculation [#14884](https://github.com/mapbox/mapbox-gl-native/pull/14884)
- Fix performance regression by switching back to a more compact line attributes layout [#14851](https://github.com/mapbox/mapbox-gl-native/pull/14851)
- Fix a bug that restricted camera viewport after camera bounds were reset [#14882](https://github.com/mapbox/mapbox-gl-native/pull/14882)
- Fix a bug that ignored floating point values during core to platform color conversion [#14954](https://github.com/mapbox/mapbox-gl-native/pull/14954)
- Implemented asymmetric center of perspective: fixed an issue that caused the focal point to be always based on the view's horizontal center when setting [MapboxMap setPadding](https://docs.mapbox.com/android/api/map-sdk/8.0.0/com/mapbox/mapboxsdk/maps/MapboxMap.html#setPadding-int-int-int-int-). [#14664](https://github.com/mapbox/mapbox-gl-native/pull/14664)

### Minor features and improvements

- Allow null updates to GeoJsonSource [#14898](https://github.com/mapbox/mapbox-gl-native/pull/14898)

### Other Changes

- Add resource usage to HttpRequest, add offline query flag to HttpRequest [#14837](https://github.com/mapbox/mapbox-gl-native/pull/14837)

## 8.1.0 - June 20, 2019

### Bugs

- Accept floating numbers during core->platform color conversion [#14954](https://github.com/mapbox/mapbox-gl-native/pull/14954)
- Ensure to re-enable move when quickzoom finishes [#14965](https://github.com/mapbox/mapbox-gl-native/pull/14965)

### Features

- Limit concurrent request for Android 4.4 and below to 10 [#14971](https://github.com/mapbox/mapbox-gl-native/pull/14971)

_Note about #14971:_ This pr makes an adjustment as a precautionary measure towards a rare bug that was discovered via instrumented unit tests on Android 4.4 (aka KitKat). This bug isn’t tied to a code change in the Mapbox Maps SDK for Android. Rather, the root cause is on the Android OS (and how it interacts with OkHttp).

The Maps SDK doesn’t directly communicate with this system library that the bug originates from, but goes through an external dependency that handles the http communication for the Maps SDK. Capturing from the upstream repository that the code path to this system library isn’t thread safe in Android 4.4 and can result in the native crash above. The only way to fix this issue is by upgrading the end-user device to Android 5.0 or higher which isn’t possible if the device manufacturer hasn’t released an update for it.

Please see https://github.com/square/okhttp/issues/2679 for more information.

## 8.1.0-beta.1 - June 13, 2019

### Bugs

- Fix compilation warning with updated clang [#14865](https://github.com/mapbox/mapbox-gl-native/pull/14865)
- Fix reset behavior for setLatLngBoundsForCameraTarget [#14882](https://github.com/mapbox/mapbox-gl-native/pull/14882)

### Features

- Add resource usage to HttpRequest, add offline query param [#14837](https://github.com/mapbox/mapbox-gl-native/pull/14837)

## 8.0.1 - June 11, 2019

### Bugs

- Reset the native renderer only when the GL thread exits [#14841](https://github.com/mapbox/mapbox-gl-native/pull/14841)
- Change library loader from Relinker to SoLoader [#14890](https://github.com/mapbox/mapbox-gl-native/pull/14890)

## 8.1.0-alpha.2 - June 6, 2019

### Bugs

- Copy features array before passing them to core [#14804](https://github.com/mapbox/mapbox-gl-native/pull/14804)
- Improve test stability [#14853](https://github.com/mapbox/mapbox-gl-native/pull/14853)

### Features

- Rename URL API to URI [#14836](https://github.com/mapbox/mapbox-gl-native/pull/14836)

### Build

- Bumping Android telemetry to 4.5.1 [#14874](https://github.com/mapbox/mapbox-gl-native/pull/14874)

### Docs

- Add external links to javadoc generation, fix warnings [#14792](https://github.com/mapbox/mapbox-gl-native/pull/14792)

## 8.1.0-alpha.1 - June 3, 2019

### Bugs

- Return color string values in the [0-255] range [#14233](https://github.com/mapbox/mapbox-gl-native/pull/14233)
- Resume file source to complete resources cache path change [#14546](https://github.com/mapbox/mapbox-gl-native/pull/14546)
- Remove binary shader support [#14707](https://github.com/mapbox/mapbox-gl-native/pull/14707)
- Deprecate MapboxMapOptions empty constructor [#14748](https://github.com/mapbox/mapbox-gl-native/pull/14748)

### Features

- Break location camera tracking when a developer invoked animation starts [#14745](https://github.com/mapbox/mapbox-gl-native/pull/14745)
- Use URI instead of URL to enable asset loading [#14750](https://github.com/mapbox/mapbox-gl-native/pull/14750)
- Option to change the zoom rate. Decrease the default zoom rate by 35% [#14774](https://github.com/mapbox/mapbox-gl-native/pull/14774)
- Provide API to control eviction of cached images [#14610](https://github.com/mapbox/mapbox-gl-native/pull/14610)
- Android specific thread pool that keeps threads attached to JVM [#14450](https://github.com/mapbox/mapbox-gl-native/pull/14450)
- Asymmetric viewport when edge insets are specified [#14664](https://github.com/mapbox/mapbox-gl-native/pull/14664)

### Build

- Update maven publishing script to enable aar packaging [#14778](https://github.com/mapbox/mapbox-gl-native/pull/14778)
- Do not try to package duplicate files in the publication [#14822](https://github.com/mapbox/mapbox-gl-native/pull/14822)

### Docs

- Document that elevation(0) turns off the location shadow [#14723](https://github.com/mapbox/mapbox-gl-native/pull/14723)

## 8.0.0 - May 22, 2019

- This release improves the method for counting monthly active users. By upgrading to this release, you are opting into the changes outlined [in this blog post](https://www.mapbox.com/52219). [#14404](https://github.com/mapbox/mapbox-gl-native/pull/14404)

## 6.8.2 - May 17, 2019

### Bugs

- Avoid binary shader load race condition with using multiple maps, disable binary shaders [#14298](https://github.com/mapbox/mapbox-gl-native/pull/14298)

## 7.5.0-beta.1 - May 17, 2019

### Features

- Option to provide custom values for location camera transition [#14606](https://github.com/mapbox/mapbox-gl-native/pull/14606)

### Bugs

- Expose trackingGesturesManagement attribute [#14659](https://github.com/mapbox/mapbox-gl-native/pull/14659)
- Harden onLowMemory call, use atomic boolean for thread safe management of the render surface [#14622](https://github.com/mapbox/mapbox-gl-native/pull/14622)
- Normalize previous rotation values of the location animator [#14626](https://github.com/mapbox/mapbox-gl-native/pull/14626)

### Performance

- Remove telemetry gesture event handling code [#14627](https://github.com/mapbox/mapbox-gl-native/pull/14627)

### Localisation

- Pull in Polish translation and updates to Czech and Hungarian [#14679](https://github.com/mapbox/mapbox-gl-native/pull/14679)

### Docs

- Add javadoc to highlight incompatible onIdle event with snapshot api [#14621](https://github.com/mapbox/mapbox-gl-native/pull/14621)

## 7.4.0 - May 15, 2019

### Features

- Revert SKU token logic [#14594](https://github.com/mapbox/mapbox-gl-native/pull/14594)

### Bugs

- Keep strong references to the FileSource/OfflineManager operations callbacks [#14601](https://github.com/mapbox/mapbox-gl-native/pull/14601)
- Failed sprite requests do not block tiles rendering [#14605](https://github.com/mapbox/mapbox-gl-native/pull/14605)
- Remove missing image log [#14600](https://github.com/mapbox/mapbox-gl-native/pull/14600)

## 7.5.0-alpha.2 - May 8, 2019

### Bugs

- Keep shared_ptr to thread pool in CustomGeometrySource [#14562](https://github.com/mapbox/mapbox-gl-native/pull/14562)
- Changed placement order of symbols to match the viewport-y order when the `PropertyValue#symbolZOrder` is set to `SYMBOL_Z_ORDER_VIEWPORT_Y`, improving collision detection and rendering of symbol layers when icons are allowed to overlap but not text. [#14486](https://github.com/mapbox/mapbox-gl-native/pull/14486)

## 7.5.0-alpha.1 - May 2, 2019

### Bugs

- Use same OfflineRegion#isComplete logic as core [#14500](https://github.com/mapbox/mapbox-gl-native/pull/14500)
- Add toString, hashcode and equals to OfflineRegionError [#14499](https://github.com/mapbox/mapbox-gl-native/pull/14499)

### Features

- Add "layer-above" option to the LocationComponent [#14497](https://github.com/mapbox/mapbox-gl-native/pull/14497)

### Build

- Update mapbox java sdk to v4.7.0 [#14480](https://github.com/mapbox/mapbox-gl-native/pull/14480)
- Update gestures library to v0.4.2 [#14524](https://github.com/mapbox/mapbox-gl-native/pull/14524)

### Docs

- Clear up LocationComponent's z-index positioning docs [#14497](https://github.com/mapbox/mapbox-gl-native/pull/14497)

## 7.4.0-beta.3 - May 2, 2019

### Features

- Less strict rules for adding render layers to sources [#14540](https://github.com/mapbox/mapbox-gl-native/pull/14540)
- Store type of renderable segment when sorting symbols by key [#14517](https://github.com/mapbox/mapbox-gl-native/pull/14517)

### Build

- Bump gestures library to v0.4.2 [#14524](https://github.com/mapbox/mapbox-gl-native/pull/14524)

## 7.4.0-beta.2 - April 23, 2019

### Features

- Enable/Disable SKU token handling [#14476](https://github.com/mapbox/mapbox-gl-native/pull/14476)

## 7.4.0-beta.1 - April 19, 2019

### Bugs

- Order render items properly across several sources [#14408](https://github.com/mapbox/mapbox-gl-native/pull/14408)
- Ignore equal offline region's download state updates [#14399](https://github.com/mapbox/mapbox-gl-native/pull/14399)
- Activate the ConnectivityReceiver when starting the download [#14397](https://github.com/mapbox/mapbox-gl-native/pull/14397)

### Features

- Port symbol-sort-key symbol layout property [#14386](https://github.com/mapbox/mapbox-gl-native/pull/14386)
- Add style image missing binding integration [#14320](https://github.com/mapbox/mapbox-gl-native/pull/14320)
- Allow snapshot generation without overlain view content [#14413](https://github.com/mapbox/mapbox-gl-native/pull/14413)
- Introduce AccountsManager to support SKU tokens in API requests [#14404](https://github.com/mapbox/mapbox-gl-native/pull/14404)
- Add MapEventFactory [#14309](https://github.com/mapbox/mapbox-gl-native/pull/14309)
- Only perform thread checking in debug builds [#14293](https://github.com/mapbox/mapbox-gl-native/pull/14293)

### Build

- Bump gestures library to v0.4.1 [#14435](https://github.com/mapbox/mapbox-gl-native/pull/14435)
- Update mapbox java sdk to v4.6.0 [#14424](https://github.com/mapbox/mapbox-gl-native/pull/14424)
- Bump telemetry to 4.4.1 [#14309](https://github.com/mapbox/mapbox-gl-native/pull/14309)
- Program against NativeMap [#14412](https://github.com/mapbox/mapbox-gl-native/pull/14412)

## 7.3.2 - April 17, 2019

## Bugs

- Unify has surface state handling, allow querying after map resume [#14426](https://github.com/mapbox/mapbox-gl-native/pull/14426)

## 7.3.1 - April 12, 2019

## Bugs

- Harden renderer invocation when Activity is resumed [#14395](https://github.com/mapbox/mapbox-gl-native/pull/14395)
- Check for Mapbox#INSTANCE when initializing the MapView [#14366](https://github.com/mapbox/mapbox-gl-native/pull/14366)
- Disable binary programs shaders [#14298](https://github.com/mapbox/mapbox-gl-native/pull/14298)

## 7.4.0-alpha.2 - April 10, 2019

### Bugs

- Check for Mapbox#INSTANCE when initializing the MapView [#14366](https://github.com/mapbox/mapbox-gl-native/pull/14366)
- Clear previous style loaded listener when setting a new style[#14338](https://github.com/mapbox/mapbox-gl-native/pull/14338)

### Features

- Allow resetting a custom OkHttp client to the default impl [#14349](https://github.com/mapbox/mapbox-gl-native/pull/14349)
- Expose LocationComponentConstants class [#14333](https://github.com/mapbox/mapbox-gl-native/pull/14333)

### Build

- Fix build with mismatching glShaderSource signature[#14339](https://github.com/mapbox/mapbox-gl-native/pull/14339)

## 7.4.0-alpha.1 - April 3, 2019

### Bugs

- Clean up location permissions annotation [#14311](https://github.com/mapbox/mapbox-gl-native/pull/14311)
- Clear camera callbacks' message queue when the map is destroyed [#14292](https://github.com/mapbox/mapbox-gl-native/pull/14292)
- Use a valid gestures focal point when resetting a manager [#14284](https://github.com/mapbox/mapbox-gl-native/pull/14284)
- Disable move gesture detector foreseeing the quickzoom [#14268](https://github.com/mapbox/mapbox-gl-native/pull/14268)
- Remove request render from MapboxMap#onStart [#14245](https://github.com/mapbox/mapbox-gl-native/pull/14245)
- Use TurfMeasurement#distance in LatLng#distanceTo [#14220](https://github.com/mapbox/mapbox-gl-native/pull/14220)

### Features

- Add pixel dimension annotation to public UiSettings methods [#14281](https://github.com/mapbox/mapbox-gl-native/pull/14281)
- Add #toString override for formatted sections [#14247](https://github.com/mapbox/mapbox-gl-native/pull/14247)
- Harden fetching camera for bounds when padding is excessive [#14221](https://github.com/mapbox/mapbox-gl-native/pull/14221)
- Traverse expression tree when checking for property overrides [#14259](https://github.com/mapbox/mapbox-gl-native/pull/14259)
- Variable label placement [#14184](https://github.com/mapbox/mapbox-gl-native/pull/14184)

### Build

- Disable binary programs until we fix [#14294]((https://github.com/mapbox/mapbox-gl-native/pull/14298)
- Disable leak canary during instrumentation tests [#14296](https://github.com/mapbox/mapbox-gl-native/pull/14296)
- Remove Android v7 support library [#14265](https://github.com/mapbox/mapbox-gl-native/pull/14265)

## 7.3.0 - March 28, 2019

### Bugs

- Fix MapView reuse issues [#14127](https://github.com/mapbox/mapbox-gl-native/pull/14127)
- Don't call OnSurfaceCreated from the main thread [#14244](https://github.com/mapbox/mapbox-gl-native/pull/14244)

## 7.3.0-beta.1 - March 20, 2019

### Features

- Expose "text-color" option for formatted sections [#14128](https://github.com/mapbox/mapbox-gl-native/pull/14128)
- Expose LocationComponent's layer IDs [#14155](https://github.com/mapbox/mapbox-gl-native/pull/14155)

### Bugs

- Cache location layer IDs in a set instead of a list [#14141](https://github.com/mapbox/mapbox-gl-native/pull/14141)
- Clear the style object when the map is destroyed [#14171](https://github.com/mapbox/mapbox-gl-native/pull/14171)
- Cache source/layer only when successfully added [#14171](https://github.com/mapbox/mapbox-gl-native/pull/14171)

### Build

- Bump telemetry version to 4.3.0 [#14140](https://github.com/mapbox/mapbox-gl-native/pull/14140)

## 7.3.0-alpha.2 - March 13, 2019

### Features

- Option to change path of the resources cache db [#13947](https://github.com/mapbox/mapbox-gl-native/pull/13947)
- Throw an exception if the component is accessed before the activation [#14068](https://github.com/mapbox/mapbox-gl-native/pull/14068)
- Add Galician localization [#14095](https://github.com/mapbox/mapbox-gl-native/pull/14095)

### Bugs

- Set a blank style when an empty builder is provided [#14085](https://github.com/mapbox/mapbox-gl-native/pull/14085)
- Fix quick-zoom + double-tap gestures combo regression [#14084](https://github.com/mapbox/mapbox-gl-native/pull/14084)
- Explicitly declare MapboxIssueRegistry's lint version [#14086](https://github.com/mapbox/mapbox-gl-native/pull/14086)

### Build

- Publish SNAPSHOTs to oss.jfrog.org [#14067](https://github.com/mapbox/mapbox-gl-native/pull/14067)

## 7.3.0-alpha.1 - March 7, 2019

### Features

- Make ReLinker default library loader [#13974](https://github.com/mapbox/mapbox-gl-native/pull/13974)
- Add plural style builder methods [#14012](https://github.com/mapbox/mapbox-gl-native/pull/14012)
- Adding areAllGesturesEnabled() boolean retreival method to UiSettings class [#14022](https://github.com/mapbox/mapbox-gl-native/pull/14022)
- Adding builder pattern for LocationComponent activation [#13941](https://github.com/mapbox/mapbox-gl-native/pull/13941)
- Get vector drawables using AppCompatResources and set them programmatically [#14058](https://github.com/mapbox/mapbox-gl-native/pull/14058)

### Bugs

- LatLngBounds: Fix constrain when crossing antimeridian [#13958](https://github.com/mapbox/mapbox-gl-native/pull/13958)
- Verify no native method is called when saving state [#13980](https://github.com/mapbox/mapbox-gl-native/pull/13980)
- Clear fragment's onMapReady callbacks as part of OnDestroy, not OnDestroyView [#14035](https://github.com/mapbox/mapbox-gl-native/pull/14035)
- Fix dialog fragment setup, add regression test [#14036](https://github.com/mapbox/mapbox-gl-native/pull/14036)
- Remove unused dem memory [#13550](https://github.com/mapbox/mapbox-gl-native/pull/13550)

### Build

- Adding Bintray Gradle plugin for artifact publishing [#13999](https://github.com/mapbox/mapbox-gl-native/pull/13999)

## 7.2.0 - February 27, 2019

### Bugs

- fix surface creation after app backgrounding for surfaceview[#13976](https://github.com/mapbox/mapbox-gl-native/pull/13976)

## 7.2.0-beta.1 - February 21, 2019

### Bugs

- Keep location component's layers hidden when new style with the "layer-below" option is applied[#13936](https://github.com/mapbox/mapbox-gl-native/pull/13936)

### Features

- Set localIdeographFontFamily default to sans-serif[#13925](https://github.com/mapbox/mapbox-gl-native/pull/13925)

## 7.2.0-alpha.2 - February 14, 2019

### Bugs

- Invalidate the camera position when gesture finishes[#13920](https://github.com/mapbox/mapbox-gl-native/pull/13920)
- Hook into surface holder to cleanup renderer on the right thread before the surface is destroyed [#13926](https://github.com/mapbox/mapbox-gl-native/pull/13926)
- Fix deadlock in http request on hitting tile limit[#13858](https://github.com/mapbox/mapbox-gl-native/pull/13858)

## 7.2.0-alpha.1 - February 7, 2019

### Build

- Update downstream circle-ci job name [#13872](https://github.com/mapbox/mapbox-gl-native/pull/13872)
- Prioritize running benchmarks when building master [#13801](https://github.com/mapbox/mapbox-gl-native/pull/13801)
- Revert modules vendorization and keep submodules only [#13806](https://github.com/mapbox/mapbox-gl-native/pull/13806)
- Run vendor submodule check [#13816](https://github.com/mapbox/mapbox-gl-native/pull/13816)
- Fetch tags during nitpick, bump gestures library to v0.4.0 [#13847](https://github.com/mapbox/mapbox-gl-native/pull/13847)

### Bugs

- Test app fixes 7.1.0 QA [#13773](https://github.com/mapbox/mapbox-gl-native/pull/13773)
- Remove deprecated javacompile, resolve AS 3.3.0 warning on deprecation [#13777](https://github.com/mapbox/mapbox-gl-native/pull/13777)
- Update project readme with buck source file ignore configuration [#13788](https://github.com/mapbox/mapbox-gl-native/pull/13788)
- Fix LocationEngine's annotation [#13792](https://github.com/mapbox/mapbox-gl-native/pull/13792)
- Fix core-only layer creation [#13798](https://github.com/mapbox/mapbox-gl-native/pull/13798)
- Remove faulty cast to uint64_t, explict casting for cluster API [#13888](https://github.com/mapbox/mapbox-gl-native/pull/13888)

### Features

- Separate quick zoom gesture option [#13791](https://github.com/mapbox/mapbox-gl-native/pull/13791)
- Location animators memory and CPU optimizations [#13678](https://github.com/mapbox/mapbox-gl-native/pull/13678)
- Added performance event [#13795](https://github.com/mapbox/mapbox-gl-native/pull/13795)
- Adding new variation of LocationComponent#activateLocationComponent [#13829](https://github.com/mapbox/mapbox-gl-native/pull/13829)
- Expose symbol placement transition duration option [#13868](https://github.com/mapbox/mapbox-gl-native/pull/13868)
- Add experimental pointer [#13889](https://github.com/mapbox/mapbox-gl-native/pull/13889)

## 7.1.2 - February 7, 2019

### Bugs

- Fix layer not rendering correctly when property value's negative [#13888](https://github.com/mapbox/mapbox-gl-native/pull/13888)

## 7.1.1 - February 1, 2019

### Bugs

- Fix a crash caused by the missing `Timber` dependency [#13847](https://github.com/mapbox/mapbox-gl-native/pull/13847)

## 7.1.0 - January 30, 2019

### Build

- Revert Android vendorization, add submodule pinning [#13815](https://github.com/mapbox/mapbox-gl-native/pull/13815)

## 7.1.0-beta.1 - January 23, 2019

### Bugs

- Ignore unused C++ arguments for all build environments [#13774](https://github.com/mapbox/mapbox-gl-native/pull/13774)
- Import gesture detector dimens using the correct package structure [#13750](https://github.com/mapbox/mapbox-gl-native/pull/13750)
- Restore location layer visibility with new "layer-below" [#13771](https://github.com/mapbox/mapbox-gl-native/pull/13771)
- Rename required TransitionOptions field [#13769](https://github.com/mapbox/mapbox-gl-native/pull/13769)

### Build

- Ignore unused C++ arguments for all build environments [#13774](https://github.com/mapbox/mapbox-gl-native/pull/13774)
- Update to NDK 19, migrate STL experimental/optional [#13049](https://github.com/mapbox/mapbox-gl-native/pull/13049)

## 7.1.0-alpha.3 - January 21, 2019

### Features

- Add library loading to peer objects [#13758](https://github.com/mapbox/mapbox-gl-native/pull/13758)
- Remove tile sorting from the clip and mask algorithms [#13756](https://github.com/mapbox/mapbox-gl-native/pull/13756)
- Ability to disable any layer using pre-processing flags [#13717](https://github.com/mapbox/mapbox-gl-native/pull/13717)
- Make library loader load the native library once [#13748](https://github.com/mapbox/mapbox-gl-native/pull/13748)
- Allow offline and snapshotter to be disabled [#13730](https://github.com/mapbox/mapbox-gl-native/pull/13730)
- Sorted render tiles for TilePyramid [#13739](https://github.com/mapbox/mapbox-gl-native/pull/13739)

### Bugs

- Adjust maven pom file with vendorized libraries versions [#13765](https://github.com/mapbox/mapbox-gl-native/pull/13765)
- Update attribution feedback URL in code and tests [#13710](https://github.com/mapbox/mapbox-gl-native/pull/13710)
- Re-apply location components "layer-below" when changed [#13749](https://github.com/mapbox/mapbox-gl-native/pull/13749)
- Avoid leaking CompassView context through animator [#13743](https://github.com/mapbox/mapbox-gl-native/pull/13743)
- Allow resetting fps listener [#13733](https://github.com/mapbox/mapbox-gl-native/pull/13733)

## 7.1.0-alpha.2 - January 16, 2019

- Do not invoke #onCancel when animation is scheduled from #onFinish block [#13737](https://github.com/mapbox/mapbox-gl-native/pull/13737)
- Updated licenses after Android vendorization [#13738](https://github.com/mapbox/mapbox-gl-native/pull/13738)
- Use QueryFeatureExtension to query the map for supercluster v0.3.0 properties, add api for getLeaves, getChildren and getNextExpansionZoom [#13631](https://github.com/mapbox/mapbox-gl-native/pull/13631)
- Restore correct Snapshot attribution, make attribution parser backwards compatible [#13732](https://github.com/mapbox/mapbox-gl-native/pull/13732)
- Vendorize Android Mapbox dependencies [#13712](https://github.com/mapbox/mapbox-gl-native/pull/13712)
- Introduce NativeMap abstraction, remove static code block removal utility [#13681](https://github.com/mapbox/mapbox-gl-native/pull/13681)
- Notify requests about network reachability in priority order [#13721](https://github.com/mapbox/mapbox-gl-native/pull/13721)
- SymbolBucket, use single map for paint properties data [#13724](https://github.com/mapbox/mapbox-gl-native/pull/13724)
- Avoid NullPointerException when quickly closing activity for attribution click handling [#13723](https://github.com/mapbox/mapbox-gl-native/pull/13723)
- Return constexpr strings from functions to avoid ODR compile failures [#13720](https://github.com/mapbox/mapbox-gl-native/pull/13720)
- Split layer type specific code in mbgl::Programs [#13577](https://github.com/mapbox/mapbox-gl-native/pull/13577)
- Shader program must always match bucket in render symbol layer [#13667](https://github.com/mapbox/mapbox-gl-native/pull/13667)
- Implement Google benchmark runner for Android platform [#13628](https://github.com/mapbox/mapbox-gl-native/pull/13628)
- Avoid leaking attribution dialog when hosting context is stopped [#13673](https://github.com/mapbox/mapbox-gl-native/pull/13673)
- Android test runner for core unit tests [#13617](https://github.com/mapbox/mapbox-gl-native/pull/13617)
- Throw IllegalStateException when when setting an maximum fps while the mapRenderer isn't created yet [#13706](https://github.com/mapbox/mapbox-gl-native/pull/13706)
- Lift Parceable interface to OfflineRegionDefinition [#13688](https://github.com/mapbox/mapbox-gl-native/pull/13688)
- Espresso test rework [#13576](https://github.com/mapbox/mapbox-gl-native/pull/13576)

## 7.0.1 - January 11, 2019

- Experimental setMaximumFps configuration on MapView [#13498](https://github.com/mapbox/mapbox-gl-native/pull/13498)

## 7.1.0-alpha.1 - January 9, 2019

- Add a Buck build and BUCK file for Android [#13584](https://github.com/mapbox/mapbox-gl-native/pull/13584)
- Instrumented unit tests, add map tests, refactor generated layer tests [#13443](https://github.com/mapbox/mapbox-gl-native/pull/13443)
- Add setMaximumFps configuration on MapView [#13498](https://github.com/mapbox/mapbox-gl-native/pull/13498)
- Fix deduping rings in quering [#11984](https://github.com/mapbox/mapbox-gl-native/pull/11984)
- Updated script detection for Unicode 11 [#13651](https://github.com/mapbox/mapbox-gl-native/pull/13651)
- Drop layer type and remaining usages [#13624](https://github.com/mapbox/mapbox-gl-native/pull/13624)
- Save debug cache state vs map state [#13638](https://github.com/mapbox/mapbox-gl-native/pull/13638)
- Add "localIdeographs" option to OfflineRegionDefinition [#13607](https://github.com/mapbox/mapbox-gl-native/pull/13607)
- Remove timber in favor of logger [#13625](https://github.com/mapbox/mapbox-gl-native/pull/13625)

## 7.0.0 - January 7, 2019

- Update core styles based on the streets-v8 source. This results in breaking changes for any code that references internal layers and sources of the style [#13615](https://github.com/mapbox/mapbox-gl-native/pull/13615)
- Cancel camera transitions only when set CameraMode is not None [#13662](https://github.com/mapbox/mapbox-gl-native/pull/13662)
- Fix location interval workaround initialization [#13670](https://github.com/mapbox/mapbox-gl-native/pull/13670)

## 7.0.0-beta.2 - December 21, 2018

- Mapbox services sdk version bump to 4.3.0 [#13610](https://github.com/mapbox/mapbox-gl-native/pull/13610)
- Added Style object parameter mention to LocationComponent methods [#13597](https://github.com/mapbox/mapbox-gl-native/pull/13597)
- Clean up remaining deprecated methods for 7.0.0 [#13613](https://github.com/mapbox/mapbox-gl-native/pull/13613)
- Dismiss the foreground color only when the style is loaded [#13582](https://github.com/mapbox/mapbox-gl-native/pull/13582)
- Location update fastest interval workaround[#13592](https://github.com/mapbox/mapbox-gl-native/pull/13592)
- CameraMode transition animation and result listeners[#13523](https://github.com/mapbox/mapbox-gl-native/pull/13523)
- Validate if weakreference is still valid [#13555](https://github.com/mapbox/mapbox-gl-native/pull/13555)

## 6.8.1 - December 21, 2018

- Telemetry bump to 3.5.7 [#13645](https://github.com/mapbox/mapbox-gl-native/pull/13645)
- Revert core styles back to streets-v7 source [#13648](https://github.com/mapbox/mapbox-gl-native/pull/13648)

## 6.8.0 - December 19, 2018

- Update core styles based on the streets-v8 source. This results in breaking changes for any code that references internal layers and sources of the style [#13615](https://github.com/mapbox/mapbox-gl-native/pull/13615)
- Fixed compass update time not getting updated [#13569](https://github.com/mapbox/mapbox-gl-native/pull/13569)
- Register compass sensor listener only if necessary [#13573](https://github.com/mapbox/mapbox-gl-native/pull/13573)
- Immediate location animation based on map's projection [#13545](https://github.com/mapbox/mapbox-gl-native/pull/13545)
- Option to disable smooth animation of compass and accuracy values [#13574](https://github.com/mapbox/mapbox-gl-native/pull/13574)
- Add onDidBecomeIdle to Map events [#13513](https://github.com/mapbox/mapbox-gl-native/pull/13513)
- Expose setAccessToken on Mapbox.java [#13397](https://github.com/mapbox/mapbox-gl-native/pull/13397)
- Expose endpoint change option for snapshotter [#13508](https://github.com/mapbox/mapbox-gl-native/pull/13508)
- Use locale sensitive DecimalFormat for color alpha conversion [#13393](https://github.com/mapbox/mapbox-gl-native/pull/13393)
- Enabled RecyclerView integration [#13132](https://github.com/mapbox/mapbox-gl-native/pull/13132)

## 7.0.0-beta.1 - December 12, 2018

- Style builder and Style.java [#13484](https://github.com/mapbox/mapbox-gl-native/pull/13484)
- LatLng bounds cannot be wrapped [#13419](https://github.com/mapbox/mapbox-gl-native/pull/13419)
- Expose endpoint change option for snapshotter [#13508](https://github.com/mapbox/mapbox-gl-native/pull/13508)
- Formatted "text-field" property setter [#13358](https://github.com/mapbox/mapbox-gl-native/pull/13358)
- Expose setAccesToken on Mapbox.java [#13397](https://github.com/mapbox/mapbox-gl-native/pull/13397)
- Immediately notify core about forced connectivity state [#13485](https://github.com/mapbox/mapbox-gl-native/pull/13485)
- Handle transition duration of 0ms for symbols [#13535](https://github.com/mapbox/mapbox-gl-native/pull/13535)
- Returning boolean when removing sources and layers [#13428](https://github.com/mapbox/mapbox-gl-native/pull/13428)
- Telemetry v4.1.1 bump [#13499](https://github.com/mapbox/mapbox-gl-native/pull/13499)
- mapbox-java 4.2.0 bump [#13507](https://github.com/mapbox/mapbox-gl-native/pull/13507)

## 7.0.0-alpha.3 - November 30, 2018

- Add `fill-extrusion-vertical-gradient` fill paint property [#13463](https://github.com/mapbox/mapbox-gl-native/pull/13463)
- Don't cancel core transitions on MotionEvent#ACTION_CANCEL [#13465](https://github.com/mapbox/mapbox-gl-native/pull/13465)
- Require context in AttributionParser.Options [#13444](https://github.com/mapbox/mapbox-gl-native/pull/13444)
- Deprecated annotations package [#13468](https://github.com/mapbox/mapbox-gl-native/pull/13468)
- Send turnstile event while create MapSnapshotter instance [#13475](https://github.com/mapbox/mapbox-gl-native/pull/13475)
- Remove default zoom level restrictions when activating the location component [#13425](https://github.com/mapbox/mapbox-gl-native/pull/13425)

## 6.7.2 - November 30, 2018

- Telemetry v3.5.6 [#13486](https://github.com/mapbox/mapbox-gl-native/pull/13486)
- Add "localIdeographFontFamily" setting to MapSnapshotter to reduce font data usage while snapshotting CJK maps [#13427](https://github.com/mapbox/mapbox-gl-native/pull/13427)
- Send turnstile event while creating MapSnapshotter instance [#13475](https://github.com/mapbox/mapbox-gl-native/pull/13475)

## 7.0.0-alpha.2 - November 21, 2018

- Add overlay example with custom drawing using paint and canvas [#13431](https://github.com/mapbox/mapbox-gl-native/pull/13431)
- Use --info argument to show details during test run [#13415](https://github.com/mapbox/mapbox-gl-native/pull/13415)
- Add specific name logging in failure scenarios of PropertyValue [#13409](https://github.com/mapbox/mapbox-gl-native/pull/13409)
- Fixed constant layer filter [#13406](https://github.com/mapbox/mapbox-gl-native/pull/13406)
- Update map feedback URL [#13396](https://github.com/mapbox/mapbox-gl-native/pull/13396)
- Use locale sensitive DecimalFormat for color alpha conversion [#13393](https://github.com/mapbox/mapbox-gl-native/pull/13393)
- Fixed `{prefix}` in tile resource URLs [#13429](https://github.com/mapbox/mapbox-gl-native/pull/13429)

## 6.7.1 - November 16, 2018

- Telemetry v3.5.4 [#13330](https://github.com/mapbox/mapbox-gl-native/pull/13330)

## 7.0.0-alpha.1 - November 14, 2018

- Added Nullable and NonNull annotations [#13274](https://github.com/mapbox/mapbox-gl-native/pull/13274)
- Catch and re-throw throwables thrown inside map state callbacks [#13351](https://github.com/mapbox/mapbox-gl-native/pull/13351)
- Removed deprecated code and notices [#13244](https://github.com/mapbox/mapbox-gl-native/pull/13244)
- Update to mapbox java services 4.1.0 [#13357](https://github.com/mapbox/mapbox-gl-native/pull/13357)
- Removed deprecated color conversion from PropertyFactory [#13337](https://github.com/mapbox/mapbox-gl-native/pull/13337)
- Removed deprecated OnMapChange interface and implementation [#13240](https://github.com/mapbox/mapbox-gl-native/pull/13240)
- Cleanup isDismissible flag [#13197](https://github.com/mapbox/mapbox-gl-native/pull/13179)
- Removed java 8 language features [#13193](https://github.com/mapbox/mapbox-gl-native/pull/13193)
- Removed ZoomButtonsController support [#13198](https://github.com/mapbox/mapbox-gl-native/pull/13198)
- Changed CustomGeometrySource() signature to be Kotlin friendly [#13178](https://github.com/mapbox/mapbox-gl-native/pull/13178)
- Removed ILatLng and IProjectedMeteres [#13176](https://github.com/mapbox/mapbox-gl-native/pull/13176)
- Option to consume map clicks, consuming location clicks [#13205](https://github.com/mapbox/mapbox-gl-native/pull/13205)
- Consolidated OfflineRegionDefinitions [#13180](https://github.com/mapbox/mapbox-gl-native/pull/13180)
- Added scrollBy() to MapboxMap, depricated CameraUpdate.scrollBy() [#13223](https://github.com/mapbox/mapbox-gl-native/pull/13223)
- Improved CustomGeometrySource constructor typing [#13200](https://github.com/mapbox/mapbox-gl-native/pull/13200)
- Removed deprecated MarkerView[#13194](https://github.com/mapbox/mapbox-gl-native/pull/13194)
- Annotate onMapReady with @NonNull [#13307](https://github.com/mapbox/mapbox-gl-native/pull/13307)

## 6.6.7 - November 8, 2018

- Telemetry v3.5.4 [#13330](https://github.com/mapbox/mapbox-gl-native/pull/13330)

## 6.6.6 - November 8, 2018

- This release should be skipped in favor of the `v6.6.7`.

## 6.7.0 - November 7, 2018

- Handle null getMapAsync invocations, deliver onMapReady only once [#13301](https://github.com/mapbox/mapbox-gl-native/pull/13301)
- Verify if `text-field` is not null before performing compatibility operations [#13294](https://github.com/mapbox/mapbox-gl-native/pull/13294)
- Log Java exception and fail silently instead of throwing a native exception in the map state callbacks[#13294](https://github.com/mapbox/mapbox-gl-native/pull/13294)
- Reintroduce OnMapChange invocation [#13289](https://github.com/mapbox/mapbox-gl-native/pull/13289)
- Null-check nativeMapView in case it's destroyed [#13281](https://github.com/mapbox/mapbox-gl-native/pull/13281)

## 6.6.5 - November 2, 2018

- Telemetry v3.5.2 [#13258](https://github.com/mapbox/mapbox-gl-native/pull/13258)

## 6.6.4 - November 1, 2018

- Fix crash with non AppCompatActivities [#13222](https://github.com/mapbox/mapbox-gl-native/pull/13222)
- Telemetry bump 3.5.1 [#13204](https://github.com/mapbox/mapbox-gl-native/pull/13204)
- Gestures library 0.3.0 [#13234](https://github.com/mapbox/mapbox-gl-native/pull/13234)

## 6.7.0-beta.1 - October 31, 2018

- Remove map initialised check [#13224](https://github.com/mapbox/mapbox-gl-native/pull/13224)
- Upgrade GeoJSONVT to v6.6.2 [#13231](https://github.com/mapbox/mapbox-gl-native/pull/13231)
- Deliver OnMapReady at end of initialisation block [#13220](https://github.com/mapbox/mapbox-gl-native/pull/13220)
- Rework MapCallback to execute in correct order [#13203](https://github.com/mapbox/mapbox-gl-native/pull/13203)
- Deprecate map zoom button controller [#13197](https://github.com/mapbox/mapbox-gl-native/pull/13197)
- RenderState API [#13170](https://github.com/mapbox/mapbox-gl-native/pull/13170)
- Avoid wrapping longitude values at dateline [#13006](https://github.com/mapbox/mapbox-gl-native/pull/13006)

## 6.7.0-alpha.2 - October 24, 2018

- Format expression support [#12985](https://github.com/mapbox/mapbox-gl-native/pull/12985)
- Remove view tree observer [#13133](https://github.com/mapbox/mapbox-gl-native/pull/13133)
- Revert infer nullity [#13147](https://github.com/mapbox/mapbox-gl-native/pull/13147)
- Add priorities to requests [#13019](https://github.com/mapbox/mapbox-gl-native/pull/13019)
- Avoid using <locale> and <iostream> [#13135](https://github.com/mapbox/mapbox-gl-native/pull/13135)
- Introduce mgbl::RenderState [#13123](https://github.com/mapbox/mapbox-gl-native/pull/13123)
- Ensure queryRenderedFeatures accounts for icon-rotate [#13105](https://github.com/mapbox/mapbox-gl-native/pull/13105)
- Add 'putResourceForURL' for pre-warming ambient cache [#13119](https://github.com/mapbox/mapbox-gl-native/pull/13119)

## 6.6.2/6.6.3 - October 24, 2018

- Remove requirement on vectorDrawables.useSupportLibrary [#13168](https://github.com/mapbox/mapbox-gl-native/pull/13168)
- File source deactivate called before callbacks [#13146](https://github.com/mapbox/mapbox-gl-native/pull/13168)

## 6.7.0-alpha.1 - October 17, 2018

- Core format expression integration [#12624](https://github.com/mapbox/mapbox-gl-native/pull/12624)
- Remove obsolete catch block in getLayerAs [#13091](https://github.com/mapbox/mapbox-gl-native/pull/13091)
- Include Mapbox SDK Services proguard configuration [#13103](https://github.com/mapbox/mapbox-gl-native/pull/13103)
- Avoid printing exception for valid code path when loading FileSource [#13102](https://github.com/mapbox/mapbox-gl-native/pull/13102)
- Add equals, hashcode and toString to camera update types [#13089](https://github.com/mapbox/mapbox-gl-native/pull/13089)
- Infer nullity, optimise codebase for consumption by kotlin [#13071](https://github.com/mapbox/mapbox-gl-native/pull/13071)
- Replace OnMapchange with specific callbacks [#13050](https://github.com/mapbox/mapbox-gl-native/pull/13050)
- Offline region creation telemetry event [#12544](https://github.com/mapbox/mapbox-gl-native/pull/12544)

## 6.6.1 - October 17, 2018

- Use appcompat resource configuration for vector drawables [#13114](https://github.com/mapbox/mapbox-gl-native/pull/13114)
- Reverse order of prioritising shape vs marker click handling [#13059](https://github.com/mapbox/mapbox-gl-native/pull/13059)

## 6.6.0 - October 11, 2018

- Fixed an issue where fill and line layers would occasionally flicker on zoom [#12982](https://github.com/mapbox/mapbox-gl-native/pull/12982)

## 6.6.0-beta.1 - October 3, 2018

- Add configuration to change the animation duration multiplier when tracking location [#13011](https://github.com/mapbox/mapbox-gl-native/pull/13011)
- Move camera and puck immediately when starting to track and displacement is big [#13013](https://github.com/mapbox/mapbox-gl-native/pull/13013)

## 6.6.0-alpha.2 - September 26, 2018

- Expose offline database merge API [#12860](https://github.com/mapbox/mapbox-gl-native/pull/12860)
- Remove unnecessary camera callbacks loops [#12973](https://github.com/mapbox/mapbox-gl-native/pull/12973)
- Fix an issue where `OnCameraMoveListener#onCameraMove` would not get invoked while using `LocationComponent` [#12932](https://github.com/mapbox/mapbox-gl-native/pull/12932)
- Try not to re-draw tiles when coming back from background [#12844](https://github.com/mapbox/mapbox-gl-native/pull/12844)
- Proguard config revision [#12929](https://github.com/mapbox/mapbox-gl-native/pull/12929)
- Improve compatibility of offline packs between devices [#12933](https://github.com/mapbox/mapbox-gl-native/pull/12933)
- Update telemetry dependency to 3.2.1 [#12974](https://github.com/mapbox/mapbox-gl-native/pull/12974)
- Fix an issue where some tiles weren't rendered correctly when no internet connectivity was available [#12931](https://github.com/mapbox/mapbox-gl-native/pull/12931)

## 6.6.0-alpha.1 - September 19, 2018

- Merge LocationLayerPlugin to the Maps SDK [#12786](https://github.com/mapbox/mapbox-gl-native/pull/12786)
- Add `symbol-z-order` symbol layout property to style spec [#12783](https://github.com/mapbox/mapbox-gl-native/pull/12783)
- Add "crossSourceCollisions" map option [#12842](https://github.com/mapbox/mapbox-gl-native/pull/12842)
- "is-supported-script" expression support [#12845](https://github.com/mapbox/mapbox-gl-native/pull/12845)
- Update isEmulator check [#12832](https://github.com/mapbox/mapbox-gl-native/pull/12832)
- Prevent crash when expression evaluates to default, no pattern [#12896](https://github.com/mapbox/mapbox-gl-native/pull/12896)
- Fixed bugs in coercion expression operators ("to-array" applied to empty arrays, "to-color" applied to colors, and "to-number" applied to null) [#12864](https://github.com/mapbox/mapbox-gl-native/pull/12864)
- Update proguard config to not warn about AutoValue [#12920](https://github.com/mapbox/mapbox-gl-native/pull/12920)
- Remove nunicode from android binding [#12497](https://github.com/mapbox/mapbox-gl-native/pull/12497)
- Correct LatLngBounds exception message [#12816](https://github.com/mapbox/mapbox-gl-native/pull/12816)

## 6.5.0 - September 11, 2018

- Fixed a cubic-bezier interpolation bug. [#12812](https://github.com/mapbox/mapbox-gl-native/issues/12812)
- Fixed an issue that could cause "allow-overlap" symbols to fade in during pan operations instead of always showing. [#12683](https://github.com/mapbox/mapbox-gl-native/issues/12683)

## 6.5.0-beta.1 - September 5, 2018

- Retain shared thread pool reference [#12811](https://github.com/mapbox/mapbox-gl-native/pull/12811)
- MapStrictMode configuration [#12817](https://github.com/mapbox/mapbox-gl-native/pull/12817)
- Provide correct expression integration for newly created properties [#12809](https://github.com/mapbox/mapbox-gl-native/pull/12809)
- Get source id for supported layer types [#12801](https://github.com/mapbox/mapbox-gl-native/pull/12801)
- Check if map is destroyed before saving state in a fragment [#12804](https://github.com/mapbox/mapbox-gl-native/pull/12804)
- Bump leak canary to 1.6.1 [#12767](https://github.com/mapbox/mapbox-gl-native/pull/12767)
- Add quotes to string representation of string literal [#12768](https://github.com/mapbox/mapbox-gl-native/pull/12768)
- Make improve this map translatable [#12785](https://github.com/mapbox/mapbox-gl-native/pull/12785)
- Add Keep annotation to abstract components [#12796](https://github.com/mapbox/mapbox-gl-native/pull/12796)
- Gradle tools 3.1.4 [#12795](https://github.com/mapbox/mapbox-gl-native/pull/12795)
- Explicitly remove local references [#12774](https://github.com/mapbox/mapbox-gl-native/pull/12774)
- Add more languages to tx config file [#12719](https://github.com/mapbox/mapbox-gl-native/pull/12719)
- Add support for feature expressions in `line-pattern`, `fill-pattern`, and `fill-extrusion-pattern` properties. [#12284](https://github.com/mapbox/mapbox-gl-native/pull/12284)

## 6.5.0-alpha.2 - August 29, 2018

- Keep arbitrary region fields and constructor of OfflineGeometryRegionDefinition while obfuscating [#12734](https://github.com/mapbox/mapbox-gl-native/pull/12734)
- Modularise the SDK [#12468](https://github.com/mapbox/mapbox-gl-native/pull/12468)
- Invalidate InfoWindow's y offset when content is set [#12715](https://github.com/mapbox/mapbox-gl-native/pull/12715)
- Synchronize and coalesce requests targeting the same TileID in the CustomGeometrySource [#12509](https://github.com/mapbox/mapbox-gl-native/pull/12509)
- Line gradient support [#12575](https://github.com/mapbox/mapbox-gl-native/pull/12575)

## 6.5.0-alpha.1 - August 22, 2018

- Don't default-show text/icons that depend on the placement of a paired icon/text [#12483](https://github.com/mapbox/mapbox-gl-native/issues/12483)
- Fix symbol querying for markers near tile boundaries at high zoom. ([#12472](https://github.com/mapbox/mapbox-gl-native/issues/12472))
- Hide peer constructors [#12639](https://github.com/mapbox/mapbox-gl-native/pull/12639)
- Shutting down thread pool of the CustomGeometrySource when the source is destroyed [#12517](https://github.com/mapbox/mapbox-gl-native/pull/12517)
- Harden offline database [#12224](https://github.com/mapbox/mapbox-gl-native/pull/12224)
- Obtain paths to file directories on a worker thread [#12657](https://github.com/mapbox/mapbox-gl-native/pull/12657)
- Arbitrary offline region shapes [#11447](https://github.com/mapbox/mapbox-gl-native/pull/11447)
- Make AndroidRendererFrontend to request render once per event loop [#12586](https://github.com/mapbox/mapbox-gl-native/issues/12586)
- Converting GeoJsonSource features asynchronously [#12580](https://github.com/mapbox/mapbox-gl-native/pull/12580)
- StyleJson configuration on MapboxMapOptions [#12664](https://github.com/mapbox/mapbox-gl-native/pull/12664)
- Update MapInitializer for OfflineRegionDefinition [#12686](https://github.com/mapbox/mapbox-gl-native/pull/12686)
- Offline symbol render example using AssetFileSource [#12676](https://github.com/mapbox/mapbox-gl-native/pull/12676)
- Added Chinese translations [#12696](https://github.com/mapbox/mapbox-gl-native/pull/12696)
- Collator and resolved-locale expressions [#12688](https://github.com/mapbox/mapbox-gl-native/pull/12688)
- Update telemetry user-agent [#12706](https://github.com/mapbox/mapbox-gl-native/pull/12706)
- Replace hardcoded telemetry settings string with resources [#12707](https://github.com/mapbox/mapbox-gl-native/pull/12707)
- Update GeoJsonOptions javadoc [#12709](https://github.com/mapbox/mapbox-gl-native/pull/12709)

## 6.4.0 - August 15, 2018

- Use unconverted bearing value for LatLngBounds calculation [#12616](https://github.com/mapbox/mapbox-gl-native/pull/12616)
- Store release so files with debugging information [#12628](https://github.com/mapbox/mapbox-gl-native/pull/12628)
- Close a security vulnerability introduced in v6.2.0 that would potentially allow the owner of a style to compromise apps loading that style.

## 6.4.0-beta.1 - August 9, 2018

- Don't prefetch tiles for geojson sources [#12529](https://github.com/mapbox/mapbox-gl-native/pull/12529)
- Enable LTO in release builds [#12546](https://github.com/mapbox/mapbox-gl-native/pull/12546)
- Update Java Services to v3.4.0 [#12564](https://github.com/mapbox/mapbox-gl-native/pull/12564)
- Telemetry bump to 3.1.5 [#12589](https://github.com/mapbox/mapbox-gl-native/pull/12589)

## 6.4.0-alpha.2 - August 1, 2018

- Compress shader source code [#12477](https://github.com/mapbox/mapbox-gl-native/pull/12477)
- Add minimal touch target to marker click detection [#12482](https://github.com/mapbox/mapbox-gl-native/pull/12482)
- Improve resulting APK size by using @Keep annotation [#12490](https://github.com/mapbox/mapbox-gl-native/pull/12490)
- Reuse Bitmap, Paint & Canvas instances in LocalGlyphRasterizer [#12488](https://github.com/mapbox/mapbox-gl-native/pull/12488)
- Remove black surface during map start up [#12377](https://github.com/mapbox/mapbox-gl-native/pull/12377)
- Corrected VisibleRegion on rotated map [#12255](https://github.com/mapbox/mapbox-gl-native/pull/12255)
- Tweak conversions to reduce binary size [#12511](https://github.com/mapbox/mapbox-gl-native/pull/12511)

## 6.4.0-alpha.1 - July 25, 2018

- Validate if gesture detector is able to handle gesture [#12375](https://github.com/mapbox/mapbox-gl-native/pull/12375)
- Add a convenience method to check whether the map is in a destroyed state, MapView#isDestroyed [#12373](https://github.com/mapbox/mapbox-gl-native/pull/12373)
- Add GeoJSON regression tests for java method look up from JNI [#12354](https://github.com/mapbox/mapbox-gl-native/pull/12354)
- Bump kotlin, timber and okhttp dependencies [#12393](https://github.com/mapbox/mapbox-gl-native/pull/12393)
- Harden map events creation [#12405](https://github.com/mapbox/mapbox-gl-native/pull/12405)
- Fix possibly race on undefined behavior [#12408](https://github.com/mapbox/mapbox-gl-native/pull/12408)
- Option to request the visible region restricted by the content padding [#12407](https://github.com/mapbox/mapbox-gl-native/pull/12407)
- Expose getMap [#12444](https://github.com/mapbox/mapbox-gl-native/pull/12444)
- Correct access token validation [#12458](https://github.com/mapbox/mapbox-gl-native/pull/12458)
- Port `symbol-placement: line-center` to native [#12337](https://github.com/mapbox/mapbox-gl-native/pull/12337)
- Token string syntax (`"{token}"`) in `SymbolLayer` `textField` and `iconImage` property values is now correctly converted to the appropriate expression equivalent. ([#11659](https://github.com/mapbox/mapbox-gl-native/issues/11659))
- Fixed a crash when switching between two styles having layers with the same identifier but different layer types. ([#12432](https://github.com/mapbox/mapbox-gl-native/issues/12432))

## 6.3.0 - July 18, 2018

- Harden map events creation [#12406](https://github.com/mapbox/mapbox-gl-native/pull/12406)

## 6.3.0-beta.1 - July 11, 2018

- Fix JNI lookup of FeatureCollection, add regression test [#12344](https://github.com/mapbox/mapbox-gl-native/pull/12344)
- Added draggable marker example activity, add kotlin support to test app [@12326](https://github.com/mapbox/mapbox-gl-native/pull/12326)
- Telemetry bump to v3.1.4 [#12349](https://github.com/mapbox/mapbox-gl-native/pull/12349)
- Remove unnecessary memory use when collision debug mode is not enabled ([#12294](https://github.com/mapbox/mapbox-gl-native/issues/12294))

## 6.3.0-alpha.2 - July 5, 2018

- Add LatLngForScreenCoordinate to MapSnapshotter API, This allows to convert a LatLng value to the x,y position on the MapSnasphot image [#12221](https://github.com/mapbox/mapbox-gl-native/pull/12221)
- Expose multiple getCameraFor equivalent methods to convert a geometry or a bounds to a camera position with taking in account padding, tilt and bearing [#12290](https://github.com/mapbox/mapbox-gl-native/pull/12290)
- Avoid race condition when calling getMapAsync from a non-UI thread when running instrumentation tests [#12308](https://github.com/mapbox/mapbox-gl-native/pull/12308)

## 6.2.1 - June 27, 2018

- Backport range alpha values from 0 to 1 with int color conversion [#12235](https://github.com/mapbox/mapbox-gl-native/pull/12235)

## 6.3.0-alpha.1 - June 27, 2018

- Range alpha values from 0 to 1 with int color conversion [#12210](https://github.com/mapbox/mapbox-gl-native/pull/12210)
- Remove reflection lookup for MarkerViewAdapter intialisation [#12218](https://github.com/mapbox/mapbox-gl-native/pull/12218)
- GeoJSON clustering expressions logic change [#12211](https://github.com/mapbox/mapbox-gl-native/pull/12211)
- Add SDF support, allow template images to be tinted [#12181](https://github.com/mapbox/mapbox-gl-native/pull/12181)
- Add android licenses tool, update licenses [#12153](https://github.com/mapbox/mapbox-gl-native/pull/12153)

## 6.2.0 - June 20, 2018

- Remove support for Fabric [#12126](https://github.com/mapbox/mapbox-gl-native/pull/12126)
- Dispatch camera start callback only when actually moving [#12172](https://github.com/mapbox/mapbox-gl-native/pull/12172)

## 6.2.0-beta.3 - June 15, 2018

- Always commit symbol placement [#12076](https://github.com/mapbox/mapbox-gl-native/pull/12076)
- Ceil map dimensions [#12116](https://github.com/mapbox/mapbox-gl-native/pull/12116)
- Fix match stop conversion [#12128](https://github.com/mapbox/mapbox-gl-native/pull/12128)
- Telemetry bump to v3.1.3 [#12156](https://github.com/mapbox/mapbox-gl-native/pull/12156)

## 6.2.0-beta.2 - June 7, 2018

- Ensure that camera is invalidated before generating telemetry event [#12042](https://github.com/mapbox/mapbox-gl-native/pull/12042)
- Offline download batches [#11284](https://github.com/mapbox/mapbox-gl-native/pull/11284)
- Parse Fragment's xml attributes [#12078](https://github.com/mapbox/mapbox-gl-native/pull/12078)

## 6.2.0-beta.1 - May 31, 2018

- Bump mapbox-java to 3.2.0 [#12036](https://github.com/mapbox/mapbox-gl-native/pull/12036)
- Optional camera position for map snapshotter [#12028](https://github.com/mapbox/mapbox-gl-native/pull/12029)
- Expose enableOnUserRequest and disableOnUserRequest on Telemetry [#12024](https://github.com/mapbox/mapbox-gl-native/pull/12024)
- Add style json setter/getter to map snapshotter [#12031](https://github.com/mapbox/mapbox-gl-native/pull/12031)
- Fix literal wrapping in comparison expressions [#12022](https://github.com/mapbox/mapbox-gl-native/pull/12022)
- Null check access token in filesource initializer [#12023](https://github.com/mapbox/mapbox-gl-native/pull/12023)
- Allow literal expression property arguments [#12018](https://github.com/mapbox/mapbox-gl-native/pull/12018)
- Raw expression support [#12007](https://github.com/mapbox/mapbox-gl-native/pull/12007)

## 6.2.0-alpha.2 - May 25, 2018

- UI Thread checking [#12000](https://github.com/mapbox/mapbox-gl-native/pull/12000)
- Don't force having an Mapbox access token [#12001](https://github.com/mapbox/mapbox-gl-native/pull/12001)
- Unknown tokens in URLs are now preserved, rather than replaced with an empty string [#11787](https://github.com/mapbox/mapbox-gl-native/issues/11787)
- Update onMapChange Listener javadoc [#11972](https://github.com/mapbox/mapbox-gl-native/pull/11972)
- Set Tile loaded/rendered instead of marking tile as optional [#11985](https://github.com/mapbox/mapbox-gl-native/pull/11985)
- Wrap glGetString in `MBGL_CHECK_ERROR` too [#11106](https://github.com/mapbox/mapbox-gl-native/pull/11106)
- Accept constant expression in non-dds properties [#11960](https://github.com/mapbox/mapbox-gl-native/pull/11960)
- Style JSON configuration in Snapshotter [#11976](https://github.com/mapbox/mapbox-gl-native/pull/11976)
- Remove mips and armeabi as supported ABI, update to NDK 17 [#11458](https://github.com/mapbox/mapbox-gl-native/pull/11458)
- Re-assign ids when lng jumps to avoid flicker [#11938](https://github.com/mapbox/mapbox-gl-native/pull/11938)
- Change modifier MapView#initialize to allow subclassing [#11969](https://github.com/mapbox/mapbox-gl-native/pull/11969)
- Avoid symbol flickering when longitude is wrapped [#11938](https://github.com/mapbox/mapbox-gl-native/pull/11938)
- Fix hang when parsing very large angles for hue in hsl colors [#11968](https://github.com/mapbox/mapbox-gl-native/pull/11968)
- Clamp TileJSON.bounds latitudes to [-90, 90] [#11964](https://github.com/mapbox/mapbox-gl-native/pull/11964)
- Align URL token replacement with GL-JS [#11953](https://github.com/mapbox/mapbox-gl-native/pull/11953)
- Align match behaviour [#11935](https://github.com/mapbox/mapbox-gl-native/pull/11935)

## 6.1.3 - May 23, 2018

- Circle querying fixes [#11571](https://github.com/mapbox/mapbox-gl-native/pull/11571)
- Global symbol query [#11742](https://github.com/mapbox/mapbox-gl-native/pull/11742)
- Change MapView#initialize modifier to allow overriding [#11969](https://github.com/mapbox/mapbox-gl-native/pull/11969)
- Update telemetry to 3.1.2 [#11987](https://github.com/mapbox/mapbox-gl-native/pull/11987)

## 6.1.2 - May 18, 2018

- Update telemetry to 3.1.1 [#11942](https://github.com/mapbox/mapbox-gl-native/pull/11942)

## 6.2.0-alpha.1 - May 17, 2018

- `"to-string"` expression operator converts `null` to empty string rather than to `"null"` [#11904](https://github.com/mapbox/mapbox-gl-native/pull/11904)
- Expose MapView#setOfflineRegion [#1922](https://github.com/mapbox/mapbox-gl-native/pull/11922)
- Add nullability annotations to public API for kotlin language integration [#11925](https://github.com/mapbox/mapbox-gl-native/pull/11925)
- Expose MapView created callbacks on MapFragment and SupportMapFragment [#11934](https://github.com/mapbox/mapbox-gl-native/pull/11934)
- Update mapbox-java to 3.1.0 [#11916](https://github.com/mapbox/mapbox-gl-native/pull/11916)

## 6.1.1 - May 7, 2018

- Update telemetry to 3.1.0 [#11855](https://github.com/mapbox/mapbox-gl-native/pull/11855)

## 6.1.0 - May 4, 2018

- Unwrap LatLngBounds during JNI conversion [#11807](https://github.com/mapbox/mapbox-gl-native/pull/11807)
- Check if renderer is not destroyed before delivering snapshot [#11800](https://github.com/mapbox/mapbox-gl-native/pull/11800)
- Null-check source before removing [#11789](https://github.com/mapbox/mapbox-gl-native/pull/11789)
- Flutter support: promote pixel-ratio to public API NativeMapView [#11772](https://github.com/mapbox/mapbox-gl-native/pull/11772)
- Unwrap LatLngBounds for the shortest path when requesting camera [#11759](https://github.com/mapbox/mapbox-gl-native/pull/11759)
- Flutter support: integrate view callback abstraction [#11706](https://github.com/mapbox/mapbox-gl-native/pull/11706)
- Match expression doc tweaks [#11691](https://github.com/mapbox/mapbox-gl-native/pull/11691)
- Improve stop javadoc to include interpolate [#11677](https://github.com/mapbox/mapbox-gl-native/pull/11677)
- Reduce per-frame render CPU time [#11811](https://github.com/mapbox/mapbox-gl-native/issues/11811)
- Add Korean localization [#11792](https://github.com/mapbox/mapbox-gl-native/pull/11792)
- Add Danish localization; update Hungarian, Russian, Swedish translations [#11136](https://github.com/mapbox/mapbox-gl-native/pull/11136)

## 5.5.3 - May 4, 2018

- Check if renderer is not destroyed before delivering snapshot [#11800](https://github.com/mapbox/mapbox-gl-native/pull/11800)

## 6.0.1 - April 17, 2018

- Bump telemetry version to 3.0.2 [#11710](https://github.com/mapbox/mapbox-gl-native/pull/11710)

## 6.0.0 - April 17, 2018

- Bump telemetry version to 3.0.1 [#11700](https://github.com/mapbox/mapbox-gl-native/pull/11700)
- Update layer when changing its min/max zoom [#11687](https://github.com/mapbox/mapbox-gl-native/pull/11687)

## 6.0.0-beta.7 - April 12, 2018

- Add abs, round, floor, ceil expression operators [#11653](https://github.com/mapbox/mapbox-gl-native/pull/11653)
- LatLngBounds correct center calculation [#11650](https://github.com/mapbox/mapbox-gl-native/pull/11650)
- Bump telemetry to 3.0.0 final [#11658](https://github.com/mapbox/mapbox-gl-native/pull/11658)
- Correctly calculate LatLngBounds [#11647](https://github.com/mapbox/mapbox-gl-native/pull/11647)
- Add convenience step expression [#11641](https://github.com/mapbox/mapbox-gl-native/pull/11641)
- Add javadoc examples for Android [#11540](https://github.com/mapbox/mapbox-gl-native/pull/11540)
- Add paused state to map renderer, don't render snapshots after onPause [#11358](https://github.com/mapbox/mapbox-gl-native/pull/11358)
- Rework internal expression conversion [#11490](https://github.com/mapbox/mapbox-gl-native/pull/11490)
- Fixed gesture event listeners javadoc [#11630](https://github.com/mapbox/mapbox-gl-native/pull/11630)
- Add delete local reference on jni strings [#11608](https://github.com/mapbox/mapbox-gl-native/pull/11608)
- Release local references early [#11599](https://github.com/mapbox/mapbox-gl-native/pull/11599)
- Re-bind uniform locations after re-linking program [#11618](https://github.com/mapbox/mapbox-gl-native/pull/11618)
- Bump mapbox-sdk-services to 3.0.1 [#11593](https://github.com/mapbox/mapbox-gl-native/pull/11593)

## 5.5.2 - April 10, 2018

- Correct animation scale point [#11643](https://github.com/mapbox/mapbox-gl-native/pull/11643)
- Re-bind uniform locations after re-linking program [#11583](https://github.com/mapbox/mapbox-gl-native/pull/11583)
- Invalidate camera position before delivering onMapReady [#11585](https://github.com/mapbox/mapbox-gl-native/pull/11585)
- Null java peer callback [#11358](https://github.com/mapbox/mapbox-gl-native/pull/11358)
- Add missing delete local reference [#11608](https://github.com/mapbox/mapbox-gl-native/pull/11608)
- Release local refs [#11599](https://github.com/mapbox/mapbox-gl-native/pull/11599)

## 6.0.0-beta.6 - April 4, 2018

- Fix race condition crash for heavily modified annotations [#11551](https://github.com/mapbox/mapbox-gl-native/pull/11551)
- Throw exception when converting PropertyValue with an expression [#11572](https://github.com/mapbox/mapbox-gl-native/pull/11572)
- Invalidate camera position before delivering onMapReady [#11585](https://github.com/mapbox/mapbox-gl-native/pull/11585)
- Rework logical condition convenience expressions [#11555](https://github.com/mapbox/mapbox-gl-native/pull/11555)
- Telemetry library version update to 3.0.0-beta.4 [#11590](https://github.com/mapbox/mapbox-gl-native/pull/11590)

## 6.0.0-beta.5 - March 27, 2018

- Avoid flashing on pitched overzoomed tiles [#11488](https://github.com/mapbox/mapbox-gl-native/pull/11488)
- Literal array conversion of primitive arrays [#11500](https://github.com/mapbox/mapbox-gl-native/pull/11500)
- Make default output from step more descriptive [#11501](https://github.com/mapbox/mapbox-gl-native/pull/11501)
- Expose public Telemetry API [#11503](https://github.com/mapbox/mapbox-gl-native/pull/11503)
- Convert Android int colors with to-color expression [#11506](https://github.com/mapbox/mapbox-gl-native/pull/11506)
- Prevent default style reload when string style json was set [#11520](https://github.com/mapbox/mapbox-gl-native/pull/11520)
- String, number and bool Expressions with multiple values [#11512](https://github.com/mapbox/mapbox-gl-native/pull/11512)
- Telemetry library 3.0.0-beta.3 [#11534](https://github.com/mapbox/mapbox-gl-native/pull/11534)
- Gestures library v0.2.0 [#11535](https://github.com/mapbox/mapbox-gl-native/pull/11535)

## 5.5.1 - March 26, 2018

- Verify optional access of FileSource deactivation [#11480](https://github.com/mapbox/mapbox-gl-native/pull/11480)
- Prevent default style loading when style json was set [#11519](https://github.com/mapbox/mapbox-gl-native/pull/11519)
- Delete local reference when convering Image.java [#11350](https://github.com/mapbox/mapbox-gl-native/pull/11350)
- Use float for pixel ratio when creating a snapshotter [#11367](https://github.com/mapbox/mapbox-gl-native/pull/11367)
- Validate if width and height aren't 0 when creating a snapshot [#11364](https://github.com/mapbox/mapbox-gl-native/pull/11364)
- Null check body of http request [#11413](https://github.com/mapbox/mapbox-gl-native/pull/11413)
- Clamp TileJSON bounds [#11425](https://github.com/mapbox/mapbox-gl-native/pull/11425)

## 6.0.0-beta.4 - March 20, 2018

- Gesture library 0.1.0 stable [#11483](https://github.com/mapbox/mapbox-gl-native/pull/11483)
- Filters with expressions [#11429](https://github.com/mapbox/mapbox-gl-native/pull/11429)
- Telemetry 3.0.0-beta.2 [#11474](https://github.com/mapbox/mapbox-gl-native/pull/11474)
- High level JNI conversion for geojson [#11471](https://github.com/mapbox/mapbox-gl-native/pull/11471)
- Update to MAS 3.0.0-beta.4 [#11468](https://github.com/mapbox/mapbox-gl-native/pull/11468)
- Support for expression literal on arrays [#11457](https://github.com/mapbox/mapbox-gl-native/pull/11457)
- Fix telemetry integration for two finger tap gesture [#11460](https://github.com/mapbox/mapbox-gl-native/pull/11460)
- Revisit proguard configuration [#11434](https://github.com/mapbox/mapbox-gl-native/pull/11434)
- Expressions accessor support [#11352](https://github.com/mapbox/mapbox-gl-native/pull/11352)
- Calculate camera's LatLng for bounds without map padding [#11410](https://github.com/mapbox/mapbox-gl-native/pull/11410)
- Check for null on http body request [#11413](https://github.com/mapbox/mapbox-gl-native/pull/11413)
- Expose more gesture settings [#11407](https://github.com/mapbox/mapbox-gl-native/pull/11407)
- Revert java 8 language support [#11398](https://github.com/mapbox/mapbox-gl-native/pull/11398)
- Update to MAS 3.0.0-beta.3 [#11373](https://github.com/mapbox/mapbox-gl-native/pull/11373)
- Rework match expression to style specification syntax [#11388](https://github.com/mapbox/mapbox-gl-native/pull/11388)
- Update javadoc configuration for Gradle 4.4 [#11384](https://github.com/mapbox/mapbox-gl-native/pull/11384)
- Rework zoomIn and zoomOut to use ValueAnimators [#11382](https://github.com/mapbox/mapbox-gl-native/pull/11382)
- Delete LocalRef when converting Image.java [#11350](https://github.com/mapbox/mapbox-gl-native/pull/11350)
- Use float for pixelratio when creating a snapshotter [#11367](https://github.com/mapbox/mapbox-gl-native/pull/11367)
- Validate width/height when creating a snapshot [#11364](https://github.com/mapbox/mapbox-gl-native/pull/11364)

## 6.0.0-beta.3 - March 2, 2018

- Added missing local reference deletes [#11243](https://github.com/mapbox/mapbox-gl-native/pull/11243), [#11272](https://github.com/mapbox/mapbox-gl-native/pull/11272)
- Remove obsolete camera api [#11201](https://github.com/mapbox/mapbox-gl-native/pull/11201)
- Fix UTF-8 encoding, add missing package-info.java files [#11261](https://github.com/mapbox/mapbox-gl-native/pull/11261)
- Rework expression api [#11210](https://github.com/mapbox/mapbox-gl-native/pull/11210)
- LatLngBounds fixes [#11333](https://github.com/mapbox/mapbox-gl-native/pull/11333), [#11307](https://github.com/mapbox/mapbox-gl-native/pull/11307), [#11308](https://github.com/mapbox/mapbox-gl-native/pull/11308), [#11309](https://github.com/mapbox/mapbox-gl-native/pull/11309), [#11226](https://github.com/mapbox/mapbox-gl-native/pull/11226)
- New gestures library [#11221](https://github.com/mapbox/mapbox-gl-native/pull/11221)
- Expose ImageSource coordinates setter [#11262](https://github.com/mapbox/mapbox-gl-native/pull/11262)
- Add heatmap color property [#11220](https://github.com/mapbox/mapbox-gl-native/pull/11220)
- Add support for mapzen terrarium raster-dem encoding [#11339](https://github.com/mapbox/mapbox-gl-native/pull/11339)

## 5.5.0 - March 1, 2018

- TileJSON Bounds allows values inclusive of world extents [#11178](https://github.com/mapbox/mapbox-gl-native/pull/11178)
- LatLngBounds returned by VisibleRegion when map is rotated [#11226](https://github.com/mapbox/mapbox-gl-native/pull/11226)
- Custom Layer fixes & black list VAO on mali t720 [#11239](https://github.com/mapbox/mapbox-gl-native/pull/11239)
- Check if Activity isn't finishing before showing dialog [#11244](https://github.com/mapbox/mapbox-gl-native/pull/11244)
- Decouple MapPadding from overlain views [#11258](https://github.com/mapbox/mapbox-gl-native/pull/11258)
- Don't disable zoom button controller zooming with gesture disabled zoom [#11259](https://github.com/mapbox/mapbox-gl-native/pull/11259)
- Expose ImageSource coordinates setter [#11262](https://github.com/mapbox/mapbox-gl-native/pull/11262)
- Add missing DeleteLocalRefs [#11272](https://github.com/mapbox/mapbox-gl-native/pull/11272)
- Continue loading style even if we mutate it [#11294](https://github.com/mapbox/mapbox-gl-native/pull/11294)
- Update telemetry version for OkHttp [#11338](https://github.com/mapbox/mapbox-gl-native/pull/11338)

## 6.0.0-beta.2 - February 13, 2018

- Deprecate LocationEngine [#11185](https://github.com/mapbox/mapbox-gl-native/pull/11185)
- Remove LOST from SDK [11186](https://github.com/mapbox/mapbox-gl-native/pull/11186)
- Transparent surface configuration on TextureView [#11065](https://github.com/mapbox/mapbox-gl-native/pull/11065)
- Constrained setLatLng documentation, expose setLatLngZoom method [#11184](https://github.com/mapbox/mapbox-gl-native/pull/11184)
- Integration of new events library [#10999](https://github.com/mapbox/mapbox-gl-native/pull/10999)
- AddImage performance improvement [#11111](https://github.com/mapbox/mapbox-gl-native/pull/11111)
- Migrate MAS to 3.0.0, refactor GeoJson integration [#11149](https://github.com/mapbox/mapbox-gl-native/pull/11149)
- Remove @jar and @aar dependency suffixes [#11161](https://github.com/mapbox/mapbox-gl-native/pull/11161)

## 5.4.1 - February 9, 2018

- Don't recreate TextureView surface as part of view resizing, solves OOM crashes [#11148](https://github.com/mapbox/mapbox-gl-native/pull/11148)
- Don't invoke OnLowMemory before map is ready, solves startup crash on low memory devices [#11109](https://github.com/mapbox/mapbox-gl-native/pull/11109)
- Programmatically create GLSurfaceView, solves fragment bug [#11124](https://github.com/mapbox/mapbox-gl-native/pull/11124)
- Proguard config for optional location provider, solves obfuscation warnings [#11127](https://github.com/mapbox/mapbox-gl-native/pull/11127)
- MapView weak reference in global layout listener, solves memory leak [#11128](https://github.com/mapbox/mapbox-gl-native/pull/11128)

## 5.4.0 - January 30, 2018

- Blacklist Adreno 2xx GPU for VAO support [#11047](https://github.com/mapbox/mapbox-gl-native/pull/11047)
- Bearing tracking mode GPS_NORTH_FACING [#11095](https://github.com/mapbox/mapbox-gl-native/pull/11095)
- Disable logging for missing location permissions when location is disabled [#11084](https://github.com/mapbox/mapbox-gl-native/pull/11084)
- Create offline handler using the main thread looper [#11021](https://github.com/mapbox/mapbox-gl-native/pull/11021)

## 6.0.0-beta.1 - January 26, 2018

- Binding integration for expressions [#10654](https://github.com/mapbox/mapbox-gl-native/pull/10654)
- CustomGeometrySource [#9983](https://github.com/mapbox/mapbox-gl-native/pull/9983)
- HillshadeLayer and RasterDemSource [#11031](https://github.com/mapbox/mapbox-gl-native/pull/11031)
- Revisit marker placement for snapshot [#11029](https://github.com/mapbox/mapbox-gl-native/pull/11029)
- SafeVarargs annotation for expressions [#11027](https://github.com/mapbox/mapbox-gl-native/pull/11027)
- Expression#toString [#11024](https://github.com/mapbox/mapbox-gl-native/pull/11024)
- Rename initRenderSurface to onSurfaceCreated [#11023](https://github.com/mapbox/mapbox-gl-native/pull/11023)
- Expose attribution manager as public API [#10942](https://github.com/mapbox/mapbox-gl-native/pull/10942)
- Replace Mapzen vector source example with Mapillary [#10931](https://github.com/mapbox/mapbox-gl-native/pull/10931)
- Add Hebrew localization [#10967](https://github.com/mapbox/mapbox-gl-native/pull/10967)
- Cleanup gradle configuration files [#10903](https://github.com/mapbox/mapbox-gl-native/pull/10903)
- Send double tap event only once [#10855](https://github.com/mapbox/mapbox-gl-native/pull/10855)
- Parameter validation for LatLngBounds#from [#10831](https://github.com/mapbox/mapbox-gl-native/pull/10831)
- Replace JSON parsing [#10815](https://github.com/mapbox/mapbox-gl-native/pull/10815)
- Orientation change regression test [#10814](https://github.com/mapbox/mapbox-gl-native/pull/10814)
- Max & min LatLng constants [#10780](https://github.com/mapbox/mapbox-gl-native/pull/10780)
- LatLng#wrap return new instance of LatLng [#10769](https://github.com/mapbox/mapbox-gl-native/pull/10769)
- Custom library loader [#10733](https://github.com/mapbox/mapbox-gl-native/pull/10733)
- Inconsistent parameters for LatLngBounds.union [#10728](https://github.com/mapbox/mapbox-gl-native/pull/10728)
- Gradle 4.1 / AS 3.0 [#10549](https://github.com/mapbox/mapbox-gl-native/pull/10549)

## 5.3.2 - January 22, 2018

- Validate surface creation before destroying [#10890](https://github.com/mapbox/mapbox-gl-native/pull/10890)
- Add filesource activation ot OfflineRegion [#10904](https://github.com/mapbox/mapbox-gl-native/pull/10904)
- Save configuration of UiSettings [#10908](https://github.com/mapbox/mapbox-gl-native/pull/10908)
- Do not overwrite user-set focal point [#10910](https://github.com/mapbox/mapbox-gl-native/pull/10910)
- Camera callbacks for velocity animated movements [#10925](https://github.com/mapbox/mapbox-gl-native/pull/10925)
- Allow changing the used OkHttpClient [#10948](https://github.com/mapbox/mapbox-gl-native/pull/10948)
- Validate zoom level before creating telemetry event [#10959](https://github.com/mapbox/mapbox-gl-native/pull/10959)
- Handle null call instances in HttpRequest [#10987](https://github.com/mapbox/mapbox-gl-native/pull/10987)

## 5.3.1 - January 10, 2018

- Blacklist binary program loading for Vivante GC4000 GPUs [#10862](https://github.com/mapbox/mapbox-gl-native/pull/10862)
- Support Genymotion [#10841](https://github.com/mapbox/mapbox-gl-native/pull/10841)
- Getting bitmap from drawable fix [#10763](https://github.com/mapbox/mapbox-gl-native/pull/10763)
- Use US locale when converting between int color and RGBA string [#10810](https://github.com/mapbox/mapbox-gl-native/pull/10810)
- Harden map destruction [#10811](https://github.com/mapbox/mapbox-gl-native/pull/10811)
- RTL support [#10828](https://github.com/mapbox/mapbox-gl-native/pull/10828)
- Allow configuring Http url logging when a request fails [#10830](https://github.com/mapbox/mapbox-gl-native/pull/10830)
- Don't send double tap event multiple times for telemetry [#10854](https://github.com/mapbox/mapbox-gl-native/pull/10854)
- Fix code generation [#10856](https://github.com/mapbox/mapbox-gl-native/pull/10856)
- Use the correct cancelable callback after posting cancel [#10871](https://github.com/mapbox/mapbox-gl-native/pull/10871)

## 5.3.0 - December 20, 2017

- Add support for TinySDF [#10706](https://github.com/mapbox/mapbox-gl-native/pull/10706)
- Save restore MyLocationViewSettings [#10746](https://github.com/mapbox/mapbox-gl-native/pull/10746)
- Post animation callback invocation [#10664](https://github.com/mapbox/mapbox-gl-native/pull/10664)
- Allow configuring Http logging [#10681](https://github.com/mapbox/mapbox-gl-native/pull/10681)
- Fix reverse scale gesture [#10688](https://github.com/mapbox/mapbox-gl-native/pull/10688)
- Update offline region metadata documentation [#10693](https://github.com/mapbox/mapbox-gl-native/pull/10693)
- Post camera listener invocation [#10690](https://github.com/mapbox/mapbox-gl-native/pull/10690)
- Activate filesource for offline region creation [#10718](https://github.com/mapbox/mapbox-gl-native/pull/10718)
- Update Spanish/Vietnamese translations [#10740](https://github.com/mapbox/mapbox-gl-native/pull/10740)
- Update instrumented make target [#10724](https://github.com/mapbox/mapbox-gl-native/pull/10724)
- Remove black flash on start for fragments [#10717](https://github.com/mapbox/mapbox-gl-native/pull/10717)
- CompassView decode crash [#10717](https://github.com/mapbox/mapbox-gl-native/pull/10717)
- Android SDK renaming [#10609](https://github.com/mapbox/mapbox-gl-native/pull/10609)
- Map touch listener based lists [#10749](https://github.com/mapbox/mapbox-gl-native/pull/10749)

## 5.2.1 - December 6, 2017

- Close race condition in RunLoop [#10537](https://github.com/mapbox/mapbox-gl-native/pull/10537)
- OkHttp 3.9.1 [#10515](https://github.com/mapbox/mapbox-gl-native/pull/10515)
- Attribution anchor point fix [#10558](https://github.com/mapbox/mapbox-gl-native/pull/10558)
- Pre API 19 VerifyError [#10579](https://github.com/mapbox/mapbox-gl-native/pull/10579)
- Set larger Http request limit [#10567](https://github.com/mapbox/mapbox-gl-native/pull/10567)
- Remove jar generation from maven publish [#10625](https://github.com/mapbox/mapbox-gl-native/pull/10625)
- Enable Map Rendering when paused for multiple window support [#10509](https://github.com/mapbox/mapbox-gl-native/pull/10509)
- Activate FileSource when listing offline regions [#10531](https://github.com/mapbox/mapbox-gl-native/pull/10531)
- Harden MarkerView integration by checking for null bitmap [#10532](https://github.com/mapbox/mapbox-gl-native/pull/10532)
- Use concurrent lists for camera change listeners [#10542](https://github.com/mapbox/mapbox-gl-native/pull/10542)
- Handle destroy activity as part of theme switching [#10589](https://github.com/mapbox/mapbox-gl-native/pull/10589)
- add FileSource activation/deactivation to MapSnapshotter [#10556](https://github.com/mapbox/mapbox-gl-native/pull/10556)

## 5.2.0 - November 17, 2017

- Monkey crashes [#10472](https://github.com/mapbox/mapbox-gl-native/pull/10472)

## 5.2.0-beta.5 - November 14, 2017

- MapSnapshot attribution [#10362](https://github.com/mapbox/mapbox-gl-native/pull/10362)
- Downgrade min sdk to 14 [#10355](https://github.com/mapbox/mapbox-gl-native/pull/10355)
- Harden deselection mechanism for markers [#10403](https://github.com/mapbox/mapbox-gl-native/pull/10403)
- Cherry picks to agua [#10442](https://github.com/mapbox/mapbox-gl-native/pull/10442)
- Rework test activity gen script setup [#10365](https://github.com/mapbox/mapbox-gl-native/pull/10365)
- Fix broken android unit tests, update test make target to SDK [#10387](https://github.com/mapbox/mapbox-gl-native/pull/10387)
- Check for null value when calling mapboxMap.clear [#10388](https://github.com/mapbox/mapbox-gl-native/pull/10388)

## 5.2.0-beta.4 - November 3, 2017

- Revert adding mapbox-android-core dependency (#10354) [#10380](https://github.com/mapbox/mapbox-gl-native/pull/10380)
- Asynchronous TextureView [#10370](https://github.com/mapbox/mapbox-gl-native/pull/10370)
- Workaround OkHttp bug on Android O [10366](https://github.com/mapbox/mapbox-gl-native/pull/10366)
- Revisit logo resize [10553](https://github.com/mapbox/mapbox-gl-native/pull/10353)
- Logo resize for MapSnapshotter [#10312](https://github.com/mapbox/mapbox-gl-native/pull/10312)
- Make location provider optional [#10354](https://github.com/mapbox/mapbox-gl-native/pull/10354)
- Check for positive animation value [#10348](https://github.com/mapbox/mapbox-gl-native/pull/10348)
- Fix IAE of ease/animate [#10338](https://github.com/mapbox/mapbox-gl-native/pull/10338)
- Run full test suite on CI [#10333](https://github.com/mapbox/mapbox-gl-native/pull/10333)
- Make sure camera position gets updated in onFinish() callback after camera.move [#10324](https://github.com/mapbox/mapbox-gl-native/pull/10324)
- throw IAE in animate() and easeCamera() when duration <= 0 [#10321](https://github.com/mapbox/mapbox-gl-native/pull/10321)
- Don't save state if map hasn't been initialised [#10320](https://github.com/mapbox/mapbox-gl-native/pull/10320)
- Make map snapshot optional [#10310](https://github.com/mapbox/mapbox-gl-native/pull/10310)
- Synchronise locationlastions with Transifex [#10309](https://github.com/mapbox/mapbox-gl-native/pull/10309)
- MapboxMap#addImages [#10281](https://github.com/mapbox/mapbox-gl-native/pull/10281)
- Move shape annotation click handling to core [#10267](https://github.com/mapbox/mapbox-gl-native/pull/10267)
- Map snapshotter additions [#10163](https://github.com/mapbox/mapbox-gl-native/pull/10163)
- Add velocity to gestures / port animations to SDK animators [#10202](https://github.com/mapbox/mapbox-gl-native/pull/10202)
- Don't save state if map hasn't been initialised [#10320](https://github.com/mapbox/mapbox-gl-native/pull/10320)
- android.hardware.location.gps feature should not be required [#10347](https://github.com/mapbox/mapbox-gl-native/pull/10347)

## 5.2.0-beta.3 - October 26, 2017

- Reorganize dependencies [#10268](https://github.com/mapbox/mapbox-gl-native/pull/10268)
- Blacklist VAO usage on adreno 3xx [#10291](https://github.com/mapbox/mapbox-gl-native/pull/10291)
- On stop null check [#10259](https://github.com/mapbox/mapbox-gl-native/pull/10259)

## 5.2.0-beta.2 - October 19, 2017

- Wire up MapZoomButtonController with camera change events [#10221](https://github.com/mapbox/mapbox-gl-native/pull/10221)
- Execute callbacks only when not idle [#10220](https://github.com/mapbox/mapbox-gl-native/pull/10220)
- Cleanup unused gradle plugins [#10211](https://github.com/mapbox/mapbox-gl-native/pull/10211)
- add FileSource pause/resume [#9977](https://github.com/mapbox/mapbox-gl-native/pull/9977)
- add make target for ndk-stack [#10185](https://github.com/mapbox/mapbox-gl-native/pull/10185)
- Add interpolator examples [#10067](https://github.com/mapbox/mapbox-gl-native/pull/10067)
- Add an UnsatisfiedLinkError safeguard [#10180](https://github.com/mapbox/mapbox-gl-native/pull/10180)
- Hold off handling hover events untill map has been created [#10142](https://github.com/mapbox/mapbox-gl-native/pull/10142)
- Added `MapboxMap.getCameraForGeometry()` to get a camera with zoom level and center coordinate computed to fit a shape [#10107](https://github.com/mapbox/mapbox-gl-native/pull/10107)
- Fine tune gesture zoom & rotation [#10134](https://github.com/mapbox/mapbox-gl-native/pull/10134)

## 5.2.0-beta.1 - October 6, 2017

- Allow multiple listeners for camera events, deprecate old API [#10141](https://github.com/mapbox/mapbox-gl-native/pull/10141)
- Update symbol layer example with location [#10092](https://github.com/mapbox/mapbox-gl-native/pull/10092)
- Make OfflineTilePyramidRegionDefinition parceable [#10080](https://github.com/mapbox/mapbox-gl-native/pull/10080)
- Fix 5.2.0-SNAPSHOT CI build failing [#10079](https://github.com/mapbox/mapbox-gl-native/pull/10079)
- Deprecate MarkerView [#9782](https://github.com/mapbox/mapbox-gl-native/pull/9782)
- Hide overlain views on initalisation [#10068](https://github.com/mapbox/mapbox-gl-native/pull/10068)
- API for platform side animations [#10001](https://github.com/mapbox/mapbox-gl-native/pull/10001)
- Android asynchronous rendering [#9576](https://github.com/mapbox/mapbox-gl-native/pull/9576)
- Set error handler when starting snapshotter [#10035](https://github.com/mapbox/mapbox-gl-native/pull/10035)
- Hook camera events into compass [#10019](https://github.com/mapbox/mapbox-gl-native/pull/10019)
- Testapp cleanup [#10006](https://github.com/mapbox/mapbox-gl-native/pull/10006)
- Update zoom function example with selected state [#9987](https://github.com/mapbox/mapbox-gl-native/pull/9987)
- Add style inspection to debug activity [#9773](https://github.com/mapbox/mapbox-gl-native/pull/9773)
- Bump external dependencies [#9972](https://github.com/mapbox/mapbox-gl-native/pull/9972)
- Don't recycle bitmap for icon reuse. [#9966](https://github.com/mapbox/mapbox-gl-native/pull/9966)
- Android snapshotter [#9748](https://github.com/mapbox/mapbox-gl-native/pull/9748)
- Revert #9764 [#9851](https://github.com/mapbox/mapbox-gl-native/pull/9851)
- Update docs replacing Bitrise mentions with CircleCI [#9515](https://github.com/mapbox/mapbox-gl-native/pull/9515)
- Style image accessor [#9763](https://github.com/mapbox/mapbox-gl-native/pull/9763)
- Update readme with checkstyle and ndk-stack [#9788](https://github.com/mapbox/mapbox-gl-native/pull/9788)
- make android-check [#9787](https://github.com/mapbox/mapbox-gl-native/pull/9787)
- Deprecate MyLocationView in favor of LocationLayer plugin [#9771](https://github.com/mapbox/mapbox-gl-native/pull/9771)
- Increase firebase timeout for CI testing [#9774](https://github.com/mapbox/mapbox-gl-native/pull/9774)
- Restore max zoom to 25.5 [#9765](https://github.com/mapbox/mapbox-gl-native/pull/9765)
- Update example of camera zoom function on a symbol layer. [#9743](https://github.com/mapbox/mapbox-gl-native/pull/9743)
- Optimise icon management [#9643](https://github.com/mapbox/mapbox-gl-native/pull/9643)
- Expose setStyleJson and getStyleJson [#9714](https://github.com/mapbox/mapbox-gl-native/pull/9714)
- update LatLngBounds activity with BottomSheet interaction [#9736](https://github.com/mapbox/mapbox-gl-native/pull/9736)
- post updating InfoWindow update for InfoWindowAdapter [#9716](https://github.com/mapbox/mapbox-gl-native/pull/9716)
- Annotate MapboxMap class with UiThread [#9712](https://github.com/mapbox/mapbox-gl-native/pull/9712)
- Move ZoomButtonController creation to view initalisation [#9587](https://github.com/mapbox/mapbox-gl-native/pull/9587)
- Solve lint issues, reduce baseline [#9627](https://github.com/mapbox/mapbox-gl-native/pull/9627)
- Remove wear module from project [#9618](https://github.com/mapbox/mapbox-gl-native/pull/9618)
- Add zMediaOverlay configuration + bottom sheet integration [#9592](https://github.com/mapbox/mapbox-gl-native/pull/9592)
- Forward getMapAsync to map for fragment [#9621](https://github.com/mapbox/mapbox-gl-native/pull/9621)
- Make target for dumping system gfx information [#9616](https://github.com/mapbox/mapbox-gl-native/pull/9616)
- Make target documentation [#9617](https://github.com/mapbox/mapbox-gl-native/pull/9617)
- onGlobalLayout hook for map creation [#9607](https://github.com/mapbox/mapbox-gl-native/pull/9607)
- Custom viewpager for horizontal swiping [#9601](https://github.com/mapbox/mapbox-gl-native/pull/9601)
- Disable program caching on Adreno 3xx, 4xx, and 5xx GPUs due to known bugs [#9574](https://github.com/mapbox/mapbox-gl-native/pull/9574)
- Avoid creating InfoWindow iterator if no InfoWindows are shown [#9477](https://github.com/mapbox/mapbox-gl-native/pull/9477)
- Rewire map initialisation [#9462](https://github.com/mapbox/mapbox-gl-native/pull/9462)
- Trying to update non-existent polyline fix [#9544](https://github.com/mapbox/mapbox-gl-native/pull/9544)
- Location accuracy threshold [#9472](https://github.com/mapbox/mapbox-gl-native/pull/9472)
- Rewire gesture handling and telemetry event push [#9494](https://github.com/mapbox/mapbox-gl-native/pull/9494)
- run style instrumentation tests on CI [#9353](https://github.com/mapbox/mapbox-gl-native/pull/9353)
- Fix javadoc comment for public setOfflineMapboxTileCountLimit method [#9454](https://github.com/mapbox/mapbox-gl-native/pull/9454)
- add Map change & visibility test activities [#9425](https://github.com/mapbox/mapbox-gl-native/pull/9425)
- build release package once during ci build [#9351](https://github.com/mapbox/mapbox-gl-native/pull/9351)
- Add support for ImageSource [#9110](https://github.com/mapbox/mapbox-gl-native/pull/9110)
- Increased the default maximum zoom level from 20 to 22. [#9835](https://github.com/mapbox/mapbox-gl-native/pull/9835)

## 5.1.5 - October 31, 2017

- Remove obsolete terminate context/display calls [#10162](https://github.com/mapbox/mapbox-gl-native/pull/10162)
- Determine need for clip ID based on actual layers/tiles [#10216](https://github.com/mapbox/mapbox-gl-native/pull/10216)
- Correctly alter sprite URLs [#10217](https://github.com/mapbox/mapbox-gl-native/pull/10217)
- Russian and Ukrainian localizations [#9945](https://github.com/mapbox/mapbox-gl-native/pull/9945)

## 5.1.4 - September 25, 2017

- Update translations [#10033](https://github.com/mapbox/mapbox-gl-native/pull/10033) & [#9945](https://github.com/mapbox/mapbox-gl-native/pull/9945)
- Continue rendering tiles despite erros [#10012](https://github.com/mapbox/mapbox-gl-native/pull/10012)
- Fix layer z-fighting [#9942](https://github.com/mapbox/mapbox-gl-native/pull/9942)
- Align line vertex to 4-byte boundary [#9943](https://github.com/mapbox/mapbox-gl-native/pull/9943)
- Bump proguard config for OkHttp [#9970](https://github.com/mapbox/mapbox-gl-native/pull/9970)
- Remove database on schema downgrade [#9837](https://github.com/mapbox/mapbox-gl-native/pull/9837)
- Disable rotation gesture when pinch zooming [#10026](https://github.com/mapbox/mapbox-gl-native/pull/10026)
- Do not check if connection is local request [#9968](https://github.com/mapbox/mapbox-gl-native/pull/9968)
- Harden offline region deletion [#9967](https://github.com/mapbox/mapbox-gl-native/pull/9967)
- Clear out mapCallback's OnMapReadyListeners on destroy [#9957](https://github.com/mapbox/mapbox-gl-native/pull/9957)
- Avoid adding duplicate points to bounds [#9955](https://github.com/mapbox/mapbox-gl-native/pull/9955)
- Download is complete fix [#9913](https://github.com/mapbox/mapbox-gl-native/pull/9913)
- MAS 2.2.3 [#9901](https://github.com/mapbox/mapbox-gl-native/pull/9901)
- Russian and Ukrainian localizations [#9945](https://github.com/mapbox/mapbox-gl-native/pull/9945)

## 5.1.3 - August 18, 2017

- Use separate attribute component for line normals [#9753](https://github.com/mapbox/mapbox-gl-native/pull/9753)
- Track state of initial overlain views margins [#9391](https://github.com/mapbox/mapbox-gl-native/pull/9391)
- Compability for Samsung devices forcing 3-4 array vector length [#9746](https://github.com/mapbox/mapbox-gl-native/pull/9746)
- Smallest LatLngBounds when visible region crosses dateline [#9747](https://github.com/mapbox/mapbox-gl-native/pull/9747)
- Readd ProjectMeters [#9766](https://github.com/mapbox/mapbox-gl-native/pull/9766)
- Enable location change animation of MyLocationView by default [#9779](https://github.com/mapbox/mapbox-gl-native/pull/9779)
- Avoid IndexOutOfBoundsException when destroying map object [#9789](https://github.com/mapbox/mapbox-gl-native/pull/9789)
- MAS 2.2.1 [#9796](https://github.com/mapbox/mapbox-gl-native/pull/9796)

## 5.1.2 - August 2, 2017

- Disable program caching on Ardreno GPU 3xx, 4xx and 5xx [#9625](https://github.com/mapbox/mapbox-gl-native/pull/9625)
- GeoJSON proguard issue [#9577](https://github.com/mapbox/mapbox-gl-native/pull/9577)
- Harden click events of shape annotations [#9585](https://github.com/mapbox/mapbox-gl-native/pull/9585)
- Validate Marker before opening InfoWindow [#9586](https://github.com/mapbox/mapbox-gl-native/pull/9586)
- Fix added to map check [#9602](https://github.com/mapbox/mapbox-gl-native/pull/9602)
- Don't query shape annotations if none were added [#9606](https://github.com/mapbox/mapbox-gl-native/pull/9606)
- Fix compass direction [#9632](https://github.com/mapbox/mapbox-gl-native/pull/9632)
- Remove preview image integration [#9657](https://github.com/mapbox/mapbox-gl-native/pull/9657)
- Try/catch initialization of telemetry [#9658](https://github.com/mapbox/mapbox-gl-native/pull/9658)
- Fix typo OnCameraMoveStartedListener [#9664](https://github.com/mapbox/mapbox-gl-native/pull/9664)
- Bump MAS dependency to 5.2.0 [#9671](https://github.com/mapbox/mapbox-gl-native/pull/9671)

## 5.1.1 - July 21, 2017

- Rework attribution binding [#9433](https://github.com/mapbox/mapbox-gl-native/pull/9433)
- BackendScope changes [#9538](https://github.com/mapbox/mapbox-gl-native/pull/9538)
- Invisible MarkerView performance fix [#9420](https://github.com/mapbox/mapbox-gl-native/pull/9420)
- Polyline/Polygon click listeners [#9443](https://github.com/mapbox/mapbox-gl-native/pull/9443)
- Hit test Marker/MarkerViews [#9424](https://github.com/mapbox/mapbox-gl-native/pull/9424)
- Fine tune fling gesture [#9532](https://github.com/mapbox/mapbox-gl-native/pull/9532)
- Bump OkHttp 3.8.0, Android 7.x crashes [#9522](https://github.com/mapbox/mapbox-gl-native/pull/9522)
- MyLocationView tint fix [#9410](https://github.com/mapbox/mapbox-gl-native/pull/9410)
- VisibleRegion logic [#9428](https://github.com/mapbox/mapbox-gl-native/pull/9428)
- Validate motion event [#9434](https://github.com/mapbox/mapbox-gl-native/pull/9434)
- Add French localization [#9545](https://github.com/mapbox/mapbox-gl-native/pull/9545)
- Fix public.xml [#9525](https://github.com/mapbox/mapbox-gl-native/pull/9525/files)
- Manage InfoWindow selection in AnnotationManager [#9567](https://github.com/mapbox/mapbox-gl-native/pull/9567)
- Increase touch target size [#9565](https://github.com/mapbox/mapbox-gl-native/pull/9565)
- Work around Adreno 2xx GPU bugs [#9573](https://github.com/mapbox/mapbox-gl-native/pull/9573)

## 5.1.0 - June 30, 2017

- Update to MAS 2.1.3 [#9402](https://github.com/mapbox/mapbox-gl-native/pull/9402)
- Downgrade LOST to v1.1.1 [#9394](https://github.com/mapbox/mapbox-gl-native/pull/9394)
- OnCameraIdle hook into quickzoom gesture [#9339](https://github.com/mapbox/mapbox-gl-native/pull/9339)
- LatLngBounds conversion regression, add test [#9324](https://github.com/mapbox/mapbox-gl-native/pull/9324)

## 5.1.0-beta.5 - June 21, 2017

- Update MAS dependency to 2.1.2 [#9311](https://github.com/mapbox/mapbox-gl-native/pull/9311)
- Update LOST dependency to 3.0.1 [#9302](https://github.com/mapbox/mapbox-gl-native/pull/9302)
- Fix Pulse example Parcelable creator [#9283](https://github.com/mapbox/mapbox-gl-native/pull/9283)
- Custom marker view anchoring [#9282](https://github.com/mapbox/mapbox-gl-native/pull/9282)
- Update Activity test generation [#9276](https://github.com/mapbox/mapbox-gl-native/pull/9276)
- Validate camera position before transforming [#9275](https://github.com/mapbox/mapbox-gl-native/pull/9275)
- Revisit javadoc [#9266](https://github.com/mapbox/mapbox-gl-native/pull/9266)
- Build with NDK 15 [#9263](https://github.com/mapbox/mapbox-gl-native/pull/9263)
- Snapshot with view content [#9263](https://github.com/mapbox/mapbox-gl-native/pull/9263)
- Update source changed javadoc [#9243](https://github.com/mapbox/mapbox-gl-native/pull/9243)
- Run tests on UI-thread [#9198](https://github.com/mapbox/mapbox-gl-native/pull/9198)
- Fix trackball on worker thread [#9305](https://github.com/mapbox/mapbox-gl-native/pull/9305)

## 5.1.0-beta.4 - June 9, 2017

- Option to disable location change animation [#9210](https://github.com/mapbox/mapbox-gl-native/pull/9210)
- Invalidating MyLocationView bearing when not following [#9212](https://github.com/mapbox/mapbox-gl-native/pull/9212)
- Remove upgrade runtime exceptions [#9191](https://github.com/mapbox/mapbox-gl-native/pull/9191)
- Check source usage before removal [#9129](https://github.com/mapbox/mapbox-gl-native/pull/9129)
- Fix tracking mode + camera race condition [#9133](https://github.com/mapbox/mapbox-gl-native/pull/9133)
- Harden orientation changes [#9128](https://github.com/mapbox/mapbox-gl-native/pull/9128)
- Infinite location animation updates [#9194](https://github.com/mapbox/mapbox-gl-native/pull/9194)
- Invoke callback with valid fling gestures [#9192](https://github.com/mapbox/mapbox-gl-native/pull/9192)
- Keep location tracking after screen rotation [#9187](https://github.com/mapbox/mapbox-gl-native/pull/9187)
- Update components with camera values when animating [#9174](https://github.com/mapbox/mapbox-gl-native/pull/9174)
- Validate if gestures should execute [#9173](https://github.com/mapbox/mapbox-gl-native/pull/9173)
- Custom location source and LOST integration [#9142](https://github.com/mapbox/mapbox-gl-native/pull/9142)

## 5.1.0-beta.3 - May 26, 2017

- Add binding support for Light [#9013](https://github.com/mapbox/mapbox-gl-native/pull/9013)
- Update attribution with new Mapbox wordmark [#8774](https://github.com/mapbox/mapbox-gl-native/pull/8774)
- LatLngBounds bearing default value [#9102](https://github.com/mapbox/mapbox-gl-native/pull/9102)
- Stop location updates when toggling MyLocationView [#9099](https://github.com/mapbox/mapbox-gl-native/pull/9099)
- Horizontally rotated in snapshot [#9083](https://github.com/mapbox/mapbox-gl-native/pull/9083)
- Disable letter spacing for Arabic text [#9071](https://github.com/mapbox/mapbox-gl-native/pull/9071)
- Correct bearing conversion when animating the map [#9050](https://github.com/mapbox/mapbox-gl-native/pull/9050)
- Don't leak selected markers when removing [#9047](https://github.com/mapbox/mapbox-gl-native/pull/9047)
- Bump tools and support lib version [#9046](https://github.com/mapbox/mapbox-gl-native/pull/9046)
- MarkerView deselect events with OnMarkerViewClickListener [#9047](https://github.com/mapbox/mapbox-gl-native/pull/9047)
- LOST update to v3.0.0 [#9112](https://github.com/mapbox/mapbox-gl-native/pull/9112)
- Convert dp to pixels for meters per pixel at latitude [#9048](https://github.com/mapbox/mapbox-gl-native/pull/9048)

## 5.1.0-beta.2 - May 12, 2017

5.1.0-beta.2 builds further on 5.1.0-beta.1 and adds:

- When a layer is added, reload its source's tiles [#8963](https://github.com/mapbox/mapbox-gl-native/pull/8963)
- Update release script to support CircleCI builds [#8950](https://github.com/mapbox/mapbox-gl-native/pull/8950)
- URL getter on Sources [#8959](https://github.com/mapbox/mapbox-gl-native/pull/8959)
- Build SNAPSHOT from release branch CI configuration [#8958](https://github.com/mapbox/mapbox-gl-native/pull/8958)
- Fix UI test filter in Makefile [#8960](https://github.com/mapbox/mapbox-gl-native/pull/8960)
- Bump & fixup dependencies [#8921](https://github.com/mapbox/mapbox-gl-native/pull/8921)
- Ignore already deleted region [#8920](https://github.com/mapbox/mapbox-gl-native/pull/8920)
- Keep offline observer when timeout occurs [#8919](https://github.com/mapbox/mapbox-gl-native/pull/8919)
- Show error message when no browser installed [#8920](https://github.com/mapbox/mapbox-gl-native/pull/8920)
- Reset observers of removed Sources and Layers [#8900](https://github.com/mapbox/mapbox-gl-native/pull/8900)
- Only build custom layer .so for debug builds [#8885](https://github.com/mapbox/mapbox-gl-native/pull/8885)
- Update localizations [#8883](https://github.com/mapbox/mapbox-gl-native/pull/8883)
- Reset observers of removed Sources and Layers [#8862](https://github.com/mapbox/mapbox-gl-native/pull/8862)
- Remove force style cascade [#8866](https://github.com/mapbox/mapbox-gl-native/pull/8866)
- Update proguard config [#8944](https://github.com/mapbox/mapbox-gl-native/pull/8944)
- Update LOST to 2.3.0-SNAPSHOT [#8872](https://github.com/mapbox/mapbox-gl-native/pull/8872)
- Update logo [#8774](https://github.com/mapbox/mapbox-gl-native/pull/8774)
- Camera change listener v2.0 [#8644](https://github.com/mapbox/mapbox-gl-native/pull/8644)
- Allow filesource url transform reset [#8957](https://github.com/mapbox/mapbox-gl-native/pull/8957)

## 5.1.0-beta.1 - May 2, 2017

5.1.0-beta.1 builds further on 5.0.2 and adds:

- Support for FillExtrusionLayer [#8431](https://github.com/mapbox/mapbox-gl-native/pull/8431)
- Limit Viewport [#8622](https://github.com/mapbox/mapbox-gl-native/pull/8622)
- Transition Properties for Layer attributes [#8509](https://github.com/mapbox/mapbox-gl-native/pull/8509)
- Style wide transition duration and transition offset in milliseconds [#8576](https://github.com/mapbox/mapbox-gl-native/pull/8576)
- Transifex integration, Catalan & Dutch translations [#8556](https://github.com/mapbox/mapbox-gl-native/pull/8556)
- LatLngBounds includes with another bounds [#8517](https://github.com/mapbox/mapbox-gl-native/pull/8517)
- LatLngBounds includes takes in account LatLng on the edges (cfr. core) [#8517](https://github.com/mapbox/mapbox-gl-native/pull/8517)
- LatLngBounds facility getters/setters for LatLnbg on the edges of the bounds [#8517](https://github.com/mapbox/mapbox-gl-native/pull/8517)
- Expose world bounds in LatLngBounds [#8517](https://github.com/mapbox/mapbox-gl-native/pull/8517)
- OfflineRegion are validated if the bounds is found in the world bounds, else onError will be invoked [#8517](https://github.com/mapbox/mapbox-gl-native/pull/8517)
- Polygon holes [#8557](https://github.com/mapbox/mapbox-gl-native/pull/8557) and [#8722](https://github.com/mapbox/mapbox-gl-native/pull/8722)
- Custom location source [#8710](https://github.com/mapbox/mapbox-gl-native/pull/8710)
- Expose source layer identifier [#8709](https://github.com/mapbox/mapbox-gl-native/pull/8709)
- Ensure surface is created after display and context [#8759](https://github.com/mapbox/mapbox-gl-native/pull/8759)
- Harden telemetry event dispatch [#8767](https://github.com/mapbox/mapbox-gl-native/pull/8767)
- Move LatLngBounds calculation for CameraUpdateFactory to core [#8765](https://github.com/mapbox/mapbox-gl-native/pull/8765)
- Spanish, Lithuanian, and Vietnamese localizations [#8852](https://github.com/mapbox/mapbox-gl-native/pull/8852)
- Warning when updating a non-added annotation [#8832](https://github.com/mapbox/mapbox-gl-native/pull/8832)
- Share location source between components [#8825](https://github.com/mapbox/mapbox-gl-native/pull/8825)
- Test application runtime permissions for pre lollipop devices [#8823](https://github.com/mapbox/mapbox-gl-native/pull/8823)
- Do not call OnMarkerClick listener twice [#8804](https://github.com/mapbox/mapbox-gl-native/pull/8804)
- Rework instrumentation tests [#8793](https://github.com/mapbox/mapbox-gl-native/pull/8793)
- Set first render flag to false when destroying the surface [#8739](https://github.com/mapbox/mapbox-gl-native/pull/8739)
- Post resetting tracking settings to avoid race condition reset [#8738](https://github.com/mapbox/mapbox-gl-native/pull/8738)
- Expose Source Layer identifier [#8709](https://github.com/mapbox/mapbox-gl-native/pull/8709)
- Derived source attribution [#8630](https://github.com/mapbox/mapbox-gl-native/pull/8630)
- Consistent use of duration unit [#8578](https://github.com/mapbox/mapbox-gl-native/pull/8578)
- Swedish localization [#8883](https://github.com/mapbox/mapbox-gl-native/pull/8883)
- Streets v10, Outdoors v10, Satellite Streets v10, Traffic Day v2, Traffic Night v2 [#6301](https://github.com/mapbox/mapbox-gl-native/pull/6301)

## 5.0.2 - April 3, 2017

5.0.2 is a patch release that contains the following changes:

- Binary shader caching [#8604](https://github.com/mapbox/mapbox-gl-native/pull/8604)
- Fix resource transform callback [#8582](https://github.com/mapbox/mapbox-gl-native/pull/8582)
- Restore onTouch behaviour to 4.x version [#8585](https://github.com/mapbox/mapbox-gl-native/pull/8585)
- Restore anchoring after updating MarkerView Icon [#8519](https://github.com/mapbox/mapbox-gl-native/pull/8519)

## 5.0.1 - March 22nd, 2017

5.0.1 is a patch release that addresses a shader precision issue that created a rendering problem on some devices.

- Use `highp` for color attributes [#8385](https://github.com/mapbox/mapbox-gl-native/issues/8385)

## 5.0.0 - March 17th, 2017

5.0.0 final release contains:

- Support for data-driven styles [#7752](https://github.com/mapbox/mapbox-gl-native/pull/7752)
- Additional APIs to manage runtime styling layers [#8195](https://github.com/mapbox/mapbox-gl-native/pull/8195)
- Allow query all features in sources (`querySourceFeatures`) [#5792](https://github.com/mapbox/mapbox-gl-native/issues/5792)
- Improve accessibility TalkBack support by including content description in views [#8230](https://github.com/mapbox/mapbox-gl-native/pull/8230)
- Consistent double tap zoom acceleration [#7514](https://github.com/mapbox/mapbox-gl-native/issues/7514)
- Allow zooming/scaling to use `AnimationOptions` [#8181](https://github.com/mapbox/mapbox-gl-native/pull/8181)
- Cleanup inconsistencies `float` vs `double` [#4445](https://github.com/mapbox/mapbox-gl-native/issues/4445)
- Add `mapbox_` prefix to attributes [#6482](https://github.com/mapbox/mapbox-gl-native/issues/6482)
- Update LOST to 2.x [#6573](https://github.com/mapbox/mapbox-gl-native/issues/6537)
- MAS submodules (`geojson`, `telemetry`) are now smaller to reduce the overall method count [#7642](https://github.com/mapbox/mapbox-gl-native/pull/7642)
- Telemetry Service is now include automatically via Manifest merge to simplify set up [#8338](https://github.com/mapbox/mapbox-gl-native/pull/8338)
- Support for Android Nougat [#5910](5910-move-listener-logic-for-nougat)
  - Move location listening logic to `onStart`/`onStop` activity lifecylce methods
- Removal of `accessToken` on `MapView` and `MapboxMap` [#5621](https://github.com/mapbox/mapbox-gl-native/issues/5621)
- Introduction of new make targets [#5940](https://github.com/mapbox/mapbox-gl-native/issues/5940)
  - Targets for testing, running and using external tools directly from the command line
- Cleanup Gradle files [#6009](https://github.com/mapbox/mapbox-gl-native/issues/6009)
  - Introducing single purpose `.gradle` files
- Checkstyle integration [#7442](https://github.com/mapbox/mapbox-gl-native/pull/7442)
- Transform refactor [#6532](https://github.com/mapbox/mapbox-gl-native/issues/6532)
  - All camera related code is moved to a dedicated transform class cfr. core architecture
- Encapsulate gestures [#6557](https://github.com/mapbox/mapbox-gl-native/issues/6557)
  - All gesture event code is encapsulated in `MapGestureDetector`
  - All key event code is encapsulated in `MapKeyListener`
- Transparent background for `MyLocationView` [#7116](https://github.com/mapbox/mapbox-gl-native/issues/7116)
- Correctly scale accuracy circle for `MyLocationView` [#8255](https://github.com/mapbox/mapbox-gl-native/pull/8255)
- Update documentation on generate sanity tests [#7147](https://github.com/mapbox/mapbox-gl-native/issues/7147)
- Update documentation on style templates [#7157](https://github.com/mapbox/mapbox-gl-native/issues/7157)
- Fix `NullPointerException` during save instance state [#7157](https://github.com/mapbox/mapbox-gl-native/issues/7157)
- Move management style to core [#7275](https://github.com/mapbox/mapbox-gl-native/issues/7275)
- Fix `OnCameraChange` not called for the final position of animation bug [#7350](https://github.com/mapbox/mapbox-gl-native/issues/7350)
- `MapboxAccountManager` refactor [#6534](https://github.com/mapbox/mapbox-gl-native/issues/6534)
  - Consolidating the location where tokens are set
  - Rename to reflect the increased responsibilities introduced in prior releases
- AnnotationManager refactor [#6067](https://github.com/mapbox/mapbox-gl-native/issues/6067)
  - Extracting all business logic related to annotations into a separate class cfr. to core and the iOS codebase
- Gesture handling improvements:
  - Fix missing call to `onFling` when while pinch zooming [#7666](https://github.com/mapbox/mapbox-gl-native/issues/7666)
  - Round tap-zoom gestures to nearest integer [#8027](https://github.com/mapbox/mapbox-gl-native/pull/8027)
- Support for style-wide transition animation duration and delay [#6779](https://github.com/mapbox/mapbox-gl-native/issues/6779)
- Allow callback parameter for style setting [#8262](https://github.com/mapbox/mapbox-gl-native/issues/8262)
- Support for all animated camera changes to configure dismissing tracking modes [#7854](https://github.com/mapbox/mapbox-gl-native/issues/7854)
- Avoid disabling location tracking on all gestures [#7878](https://github.com/mapbox/mapbox-gl-native/issues/7878)
- `LatLng` objects produced by the SDK are wrapped by default [#4522](https://github.com/mapbox/mapbox-gl-native/issues/4522)
- Proguard update for new SDK packages [#8437](https://github.com/mapbox/mapbox-gl-native/pull/8437)
- Javadoc cleanup [#8438](https://github.com/mapbox/mapbox-gl-native/pull/8438)
- MarkerViewClickListener and MapClickListener called simultaneously [#8447](https://github.com/mapbox/mapbox-gl-native/pull/8447)
- Fix LOST v2.0.0 integration [#8445](https://github.com/mapbox/mapbox-gl-native/pull/8445)
- Crash with OnMapChangeListener [#8380](https://github.com/mapbox/mapbox-gl-native/pull/8380)
- Feature query crash [#8374](https://github.com/mapbox/mapbox-gl-native/pull/8374)
- flyTo fix [#8302](https://github.com/mapbox/mapbox-gl-native/pull/8302)
- Don't dispatch MarkerView motion event up to MapView [#8447](https://github.com/mapbox/mapbox-gl-native/pull/8447)

## 5.0.0-beta.3 - March 10th, 2017

5.0.0-beta.3 focuses on addressing stability issues and bug fixes. It also includes:

- Allow query all features in sources (`querySourceFeatures`) [#5792](https://github.com/mapbox/mapbox-gl-native/issues/5792)
- Improve accessibility TalkBack support by including content description in views [#8230](https://github.com/mapbox/mapbox-gl-native/pull/8230)
- Allow zooming/scaling to use `AnimationOptions` [#8181](https://github.com/mapbox/mapbox-gl-native/pull/8181)
- Telemetry Service is now include automatically via Manifest merge to simplify set up [#8338](https://github.com/mapbox/mapbox-gl-native/pull/8338)
- Correctly scale accuracy circle for `MyLocationView` [#8255](https://github.com/mapbox/mapbox-gl-native/pull/8255)
- Round tap-zoom gestures to nearest integer [#8027](https://github.com/mapbox/mapbox-gl-native/pull/8027)
- Allow callback parameter for style setting [#8262](https://github.com/mapbox/mapbox-gl-native/issues/8262)
- Avoid disabling location tracking on all gestures [#7878](https://github.com/mapbox/mapbox-gl-native/issues/7878)

## 5.0.0-beta.2 - March 1st, 2017

5.0.0-beta.2 focuses on addressing stability issues and bug fixes. It also includes:

- Data-driven styling for `{text,icon}-{color,opacity,halo-color,halo-blur,halo-width}` [#7939](https://github.com/mapbox/mapbox-gl-native/pull/7939)
- Additional APIs to manage runtime styling layers [#8195](https://github.com/mapbox/mapbox-gl-native/pull/8195)

## 5.0.0-beta.1 - February 14th, 2017

- Support for data-driven styles [#7752](https://github.com/mapbox/mapbox-gl-native/pull/7752)
- Consistent double tap zoom acceleration [#7514](https://github.com/mapbox/mapbox-gl-native/issues/7514)
- Cleanup inconsistencies `float` vs `double` [#4445](https://github.com/mapbox/mapbox-gl-native/issues/4445)
- Add `mapbox_` prefix to attributes [#6482](https://github.com/mapbox/mapbox-gl-native/issues/6482)
- Update LOST to 2.0.0 [#6573](https://github.com/mapbox/mapbox-gl-native/issues/6537)
- MAS submodules (geojson, telemetry) are now smaller to reduce the overall method count [#7642](https://github.com/mapbox/mapbox-gl-native/pull/7642)
- Support for Android Nougat [#5910](5910-move-listener-logic-for-nougat)
  - Move location listening logic to onStart/onStop Activity lifecylce methods
- Removal of `accessToken` on `MapView` and `MapboxMap` [#5621](https://github.com/mapbox/mapbox-gl-native/issues/5621)
- Introduction of new make targets [#5940](https://github.com/mapbox/mapbox-gl-native/issues/5940)
  - Targets for testing, running and using external tools directly from the command line
- Cleanup Gradle files [#6009](https://github.com/mapbox/mapbox-gl-native/issues/6009)
  - Introducing single purpose `.gradle` files
- Checkstyle integration [#7442](https://github.com/mapbox/mapbox-gl-native/pull/7442)
- Transform refactor [#6532](https://github.com/mapbox/mapbox-gl-native/issues/6532)
  - All camera related code is moved to a dedicated transform class cfr. core architecture
- Encapsulate Gestures [#6557](https://github.com/mapbox/mapbox-gl-native/issues/6557)
  - All gesture event code is encapsulated in `MapGestureDetector`
  - All key event code is encapsulated in `MapKeyListener`
- Transparent background for `MyLocationView` [#7116](https://github.com/mapbox/mapbox-gl-native/issues/7116)
- Update documentation on generate sanity tests [#7147](https://github.com/mapbox/mapbox-gl-native/issues/7147)
- Update documentation on style templates [#7157](https://github.com/mapbox/mapbox-gl-native/issues/7157)
- `NullPointerException` during save instance state [#7157](https://github.com/mapbox/mapbox-gl-native/issues/7157)
- Move management style to core [#7275](https://github.com/mapbox/mapbox-gl-native/issues/7275)
- `OnCameraChange` not called for the final position of animation bug [#7350](https://github.com/mapbox/mapbox-gl-native/issues/7350)
- `MapboxAccountManager` refactor [#6534](https://github.com/mapbox/mapbox-gl-native/issues/6534)
  - Consolidating the location where tokens are set
  - Rename to reflect the increased responsibilities introduced in prior releases
- AnnotationManager refactor [#6067](https://github.com/mapbox/mapbox-gl-native/issues/6067)
  - Extracting all business logic related to annotations into a separate class cfr. to core and the iOS codebase
- Gesture handling bugs
  - Avoid calls to onFling when while pinch zooming [#7666](https://github.com/mapbox/mapbox-gl-native/issues/7666)
- Support for style-wide transition animation duration and delay [#6779](https://github.com/mapbox/mapbox-gl-native/issues/6779)
- Support for all animated camera changes to configure dismissing tracking modes [#7854](https://github.com/mapbox/mapbox-gl-native/issues/7854)
- `LatLng` objects produced by the SDK are wrapped by default for [compatibility](https://developers.google.com/android/reference/com/google/android/gms/maps/model/LatLng) with the Google Maps API on Android [#4522](https://github.com/mapbox/mapbox-gl-native/issues/4522)

## 4.2.2 - January 27, 2017

Mapbox Android 4.2.2 builds further on v4.2.1 and resolves:

- OnFling gesture improvements

* Improve fling by calculating animation time based on velocity [#7676](https://github.com/mapbox/mapbox-gl-native/pull/7676)
* Avoid triggering a fling after a scale gesture [#7675](https://github.com/mapbox/mapbox-gl-native/issues/7675)

- Deselect marker on infowindow click [#7784](https://github.com/mapbox/mapbox-gl-native/issues/7784)
- Update build tools to Bitrise supported one [#7729](https://github.com/mapbox/mapbox-gl-native/issues/7729)
- Removal of redudant log statements in native_map_view.cpp [#7801](https://github.com/mapbox/mapbox-gl-native/issues/7801)
- Invalidate markersviews when updating their position [#7794](https://github.com/mapbox/mapbox-gl-native/issues/7794)
- Avoid clustering unclusterable GeoJSON [#7633](https://github.com/mapbox/mapbox-gl-native/pull/7633)
- Prevent null pointer exceptions when receiving connecitivy change events [#6858](https://github.com/mapbox/mapbox-gl-native/issues/6858)

## 4.2.1 - December 22, 2016

Mapbox Android 4.2.1 is a bugfix release build on top of 4.2.0 and resolves:

- Gesture handling bugs
  - Reverse fling when tilted [#7383](https://github.com/mapbox/mapbox-gl-native/pull/7383)
  - Rotation is hard trigger [#7299](https://github.com/mapbox/mapbox-gl-native/pull/7299)
- Null pointer exception on TrackingSettings [#7505](https://github.com/mapbox/mapbox-gl-native/pull/7505)
- Patches to our CI integration [#7363](https://github.com/mapbox/mapbox-gl-native/pull/7363) & [#7385](https://github.com/mapbox/mapbox-gl-native/pull/7385)
- Screen density is now correctly applied for attributes [#7327](https://github.com/mapbox/mapbox-gl-native/pull/7327)
- Usage of LocalBroadCastReceiver [#7511](https://github.com/mapbox/mapbox-gl-native/pull/7511)

## 4.2.0 - December 14, 2016

Mapbox Android 4.2.0 contains all 4.2.0-beta.5 changes and adds:

- Adds additional documentation to APIs
- Resolved issue with marker views occasionally not showing up until a gesture was performed on map [#7239](https://github.com/mapbox/mapbox-gl-native/pull/7239)
- Added horizontal accuracy to location events [#7237](https://github.com/mapbox/mapbox-gl-native/pull/7237)
- Resolved issue with changing visibility of a layer [#7242](https://github.com/mapbox/mapbox-gl-native/pull/7242)
- Proguard improvement and fixes

## 4.2.0-beta.5 - October 25, 2016

Mapbox Android 4.2.0-beta.5 adds no new features, only bug fixes.

## 4.2.0-beta.4 - October 25, 2016

Mapbox Android 4.2.0-beta.4 contains all 4.2.0-beta.3 changes and adds:

- Query rendered features by source [#6516](https://github.com/mapbox/mapbox-gl-native/issues/6516)
- Decreased logging for release builds [#6787](https://github.com/mapbox/mapbox-gl-native/pull/6787)
- setConnected method to manually set a connected flag [#6618](https://github.com/mapbox/mapbox-gl-native/pull/6618)

## 4.2.0-beta.3 - September 21, 2016

Mapbox Android 4.2.0-beta.3 contains all 4.2.0-beta.2 changes and adds:

- Configurable API endpoint [#6309](https://github.com/mapbox/mapbox-gl-native/pull/6309)
- Adjusted logging level for failed OkHttp requests [6356](https://github.com/mapbox/mapbox-gl-native/issues/6356)
- Offline improvements:
  - Fix SQLite crashes by reverting WAL journal mode [#6320](https://github.com/mapbox/mapbox-gl-native/pull/6320)

## 4.2.0-beta.2 - August 25, 2016

Mapbox Android 4.2.0-beta.2 contains all 4.2.0-beta.1 changes and adds:

- Query rendered features [#5869](https://github.com/mapbox/mapbox-gl-native/issues/5869)
- Clustering [#5852](https://github.com/mapbox/mapbox-gl-native/issues/5852)
- Integrated support for [Mapbox Android Services](https://www.mapbox.com/android-sdk/#mapbox_android_services)
- Offline improvements:
  - Enable external storage to host the tiles database [#5589](https://github.com/mapbox/mapbox-gl-native/issues/5589)
  - Stop delivering status updates when offline download is canceled [#5538](https://github.com/mapbox/mapbox-gl-native/issues/5538)
  - Update SQLite schema with WAL journal mode and normal sync [#5796](https://github.com/mapbox/mapbox-gl-native/pull/5796)
  - Check for network connectivity before requesting resources [#6123](https://github.com/mapbox/mapbox-gl-native/pull/6123)

## 4.2.0-beta.1 - August 6, 2016

Mapbox Android 4.2.0 introduces the Runtime Style API and improves performance.

- Runtime Style API ([#5610](https://github.com/mapbox/mapbox-gl-native/issues/5610))
- Earcut ([#2444](https://github.com/mapbox/mapbox-gl-native/pull/2444))
- Map view rendered with `SurfaceView` for improved performance [#5000](https://github.com/mapbox/mapbox-gl-native/issues/5000)

## 4.1.1 - July 19, 2016

Mapbox Android 4.1.1 is a patch release and is recommended to upgrade to it as soon as possible.

- Default Styles Caching Crash ([#5722](https://github.com/mapbox/mapbox-gl-native/issues/5722))

## 4.1.0 - June 29, 2016

Mapbox Android 4.1.0 builds off our ambitious 4.0.0 version with 3 major new features being released.

- View Based Annotations ([#3276](https://github.com/mapbox/mapbox-gl-native/issues/3276))
- UserLocationView Refactor ([#4396](https://github.com/mapbox/mapbox-gl-native/issues/4396))
- MapboxAccountManager ([#5004](https://github.com/mapbox/mapbox-gl-native/issues/5004))

## 4.1.0-beta.3 - June 20, 2016

- New samples:
  - [Location picker](https://github.com/mapbox/mapbox-gl-native/pull/5391)
  - [Animate and rotate multiple markers](https://github.com/mapbox/mapbox-gl-native/issues/5299)
  - [Scaling marker activity](https://github.com/mapbox/mapbox-gl-native/issues/5409)
- Marker improvements:
  - [Expose MarkerView alpha](https://github.com/mapbox/mapbox-gl-native/pull/5329)
  - [Icon should be optional for MarkerView](https://github.com/mapbox/mapbox-gl-native/pull/5328)
  - [Expose an API to enable selection/deselection of markers on a map tap](https://github.com/mapbox/mapbox-gl-native/pull/5312)
  - [Bring selected MarkerView to the front](https://github.com/mapbox/mapbox-gl-native/pull/5294)
- [Make gesture focal point configurable](https://github.com/mapbox/mapbox-gl-native/pull/5332)

## 4.1.0-beta.2 - June 8, 2016

- Dynamically Update InfoWindow ([#5237](https://github.com/mapbox/mapbox-gl-native/issues/5237))
- armeabi ABI Work On armv7 Devices ([#3985](https://github.com/mapbox/mapbox-gl-native/issues/3985))
- Remove Adapter Requirement For MarkerView ([#5214](https://github.com/mapbox/mapbox-gl-native/issues/5214))
- Always Current Version Style URL Constants ([#5193](https://github.com/mapbox/mapbox-gl-native/issues/5193))
- Random NullPointerException On Telemetry ([#5186](https://github.com/mapbox/mapbox-gl-native/issues/5186))

## 4.1.0-beta.1 - May 26, 2016

Mapbox Android 4.1.0 builds off our ambitious 4.0.0 version with 3 major new features being released. To help us produce the highest quality SDK possible we're releasing an official Beta release first so that everyone has time to explore it and help hardened it before the official 4.1.0 Final Release.

- View Based Annotations ([#3276](https://github.com/mapbox/mapbox-gl-native/issues/3276))
- UserLocationView Refactor ([#4396](https://github.com/mapbox/mapbox-gl-native/issues/4396))
- MapboxAccountManager ([#5004](https://github.com/mapbox/mapbox-gl-native/issues/5004))

## 4.0.1 - May 12, 2016

Mapbox Android 4.0.1 is a patch release to make this bug fix available sooner.

- MapboxMap.removeAnnotations() doesn't remove markers ([#4553](https://github.com/mapbox/mapbox-gl-native/issues/4553))

## 4.0.0 - March 30, 2016

Mapbox Android 4.0.0 contains the following 3 major new features.

- MapboxMap API Change ([#3145](https://github.com/mapbox/mapbox-gl-native/issues/3145))
- Offline Maps ([#3891](https://github.com/mapbox/mapbox-gl-native/issues/3891))
- Telemetry ([#2421](https://github.com/mapbox/mapbox-gl-native/issues/2421))

## 4.0.0-rc.1 - March 25, 2016

- Default Value Bug Fix for MapboxMapOptions ([#4398](https://github.com/mapbox/mapbox-gl-native/issues/4398))
- NullPointerException When Scrolling ([#4424](https://github.com/mapbox/mapbox-gl-native/issues/4424))
- Platform Specific CHANGELOGS ([#4432](https://github.com/mapbox/mapbox-gl-native/issues/4432))
- Introduce LatLng.wrap() ([#4475](https://github.com/mapbox/mapbox-gl-native/issues/4475))

## 4.0.0-beta.2 - March 21, 2016

- arm64 ABI Support ([#3128](https://github.com/mapbox/mapbox-gl-native/issues/3128))
- Unify Offline And Cache Databases ([#4362](https://github.com/mapbox/mapbox-gl-native/issues/4362))
- Offline Database Vacuuming ([#4342](https://github.com/mapbox/mapbox-gl-native/pull/4342))
- Telemetry User Agent Fix ([#4328](https://github.com/mapbox/mapbox-gl-native/issues/4328))
- OnCameraChange Bug Fix ([#4327](https://github.com/mapbox/mapbox-gl-native/issues/4327))
- OnCameraChangeListener vs getCameraPosition ([#4326](https://github.com/mapbox/mapbox-gl-native/issues/4326))

## 4.0.0-beta.1 - March 15, 2016

Mapbox Android 4.0.0 is the most ambitious Android release to date with 3 major new features being released. To help us produce the highest quality SDK possible we're releasing an official Beta release first so that everyone has time to explore it and help hardened it before the official 4.0.0 Final Release.

- MapboxMap API Change ([#3145](https://github.com/mapbox/mapbox-gl-native/issues/3145))
- Offline Maps ([#3891](https://github.com/mapbox/mapbox-gl-native/issues/3891))
- Telemetry ([#2421](https://github.com/mapbox/mapbox-gl-native/issues/2421))

## 3.2.0 - January 28, 2016

- Fixed crash caused by annotation image with non-integer width or height ([#3031](https://github.com/mapbox/mapbox-gl-native/issues/3031))
- Tracking Mode Reverses Bearing Fix ([#3664](https://github.com/mapbox/mapbox-gl-native/issues/3664))
- GPS Extra Rotation Fix ([#3661](https://github.com/mapbox/mapbox-gl-native/issues/3661))
- Added new methods for getting and setting the min and max zoom levels: `getMinZoom`, `setMinZoom`, `getMaxZoom`, `setMaxZoom`. ([#509](https://github.com/mapbox/mapbox-gl-native/issues/509))

## 3.1.0 - January 20, 2016

- Camera API Callback Improvements ([#3412](https://github.com/mapbox/mapbox-gl-native/issues/3412))
- Coordinate Deprecated For LatLng ([#3309](https://github.com/mapbox/mapbox-gl-native/issues/3309))
- Responsive User Dot Location Tracking ([#2049](https://github.com/mapbox/mapbox-gl-native/issues/2049))

## 3.0.0 - December 21, 2015

- Added Camera API ([#3244](https://github.com/mapbox/mapbox-gl-native/issues/3244))
- Custom Layer Support ([#3248](https://github.com/mapbox/mapbox-gl-native/issues/3348))
- Reverse Tilt Gesutre Detection ([#3245](https://github.com/mapbox/mapbox-gl-native/issues/3245))
- Decoupled Location Provider ([#2954](https://github.com/mapbox/mapbox-gl-native/issues/2954))

## 2.3.0 - December 4, 2015

- Added Tilt / Pitch Support ([#2805](https://github.com/mapbox/mapbox-gl-native/issues/2805))
- Keep InfoWindow Open When Panning ([#3121](https://github.com/mapbox/mapbox-gl-native/issues/3121))
- Concurrent Multiple Open InfoWindows ([#3115](https://github.com/mapbox/mapbox-gl-native/issues/3115))
- OkHttp Replace curl ([#2856](https://github.com/mapbox/mapbox-gl-native/issues/2856))
- GPS and Compass Customization Support ([#2858](https://github.com/mapbox/mapbox-gl-native/issues/2858))

## 2.2.0 - October 28, 2015

- New User Dot location graphics ([#2732](https://github.com/mapbox/mapbox-gl-native/issues/2732))
- Custom Drawable Markers ([#2744](https://github.com/mapbox/mapbox-gl-native/issues/2744))
- `MapView.setOnInfoWindowClickListener()` support ([#2448](https://github.com/mapbox/mapbox-gl-native/issues/2448))
- Completed Annotations API ([#1716](https://github.com/mapbox/mapbox-gl-native/issues/1716))
- Satellite Streets Style ([#2739](https://github.com/mapbox/mapbox-gl-native/issues/2739))
- **RESOLVED** Black Screen On Ice Cream Sandwich and Jelly Bean devices ([#2802](https://github.com/mapbox/mapbox-gl-native/issues/2802))

## 2.1.0 - October 21, 2015

- Initial Android release.
