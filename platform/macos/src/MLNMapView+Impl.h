#import <mbgl/gfx/renderer_backend.hpp>
#import <mbgl/map/map_observer.hpp>
#import <mbgl/util/image.hpp>

#import "MLNBackendResource.h"

@class MLNMapView;

typedef struct _CGLContextObject* CGLContextObj;

class MLNMapViewImpl : public mbgl::MapObserver {
 public:
  static std::unique_ptr<MLNMapViewImpl> Create(MLNMapView*);

  MLNMapViewImpl(MLNMapView*);
  virtual ~MLNMapViewImpl() = default;

  virtual mbgl::gfx::RendererBackend& getRendererBackend() = 0;

  // We need a static image of what was rendered for printing.
  virtual mbgl::PremultipliedImage readStillImage() = 0;

  virtual CGLContextObj getCGLContextObj() { return nullptr; }

  // Called by the view delegate when it's time to render.
  void render();

  virtual MLNBackendResource getObject() = 0;

  // mbgl::MapObserver implementation
  void onCameraWillChange(mbgl::MapObserver::CameraChangeMode) override;
  void onCameraIsChanging() override;
  void onCameraDidChange(mbgl::MapObserver::CameraChangeMode) override;
  void onWillStartLoadingMap() override;
  void onDidFinishLoadingMap() override;
  void onDidFailLoadingMap(mbgl::MapLoadError mapError, const std::string& what) override;
  void onWillStartRenderingFrame() override;
  void onDidFinishRenderingFrame(mbgl::MapObserver::RenderFrameStatus) override;
  void onWillStartRenderingMap() override;
  void onDidFinishRenderingMap(mbgl::MapObserver::RenderMode) override;
  void onDidFinishLoadingStyle() override;
  void onSourceChanged(mbgl::style::Source& source) override;
  void onDidBecomeIdle() override;
  bool onCanRemoveUnusedStyleImage(const std::string& imageIdentifier) override;

 protected:
  /// Cocoa map view that this adapter bridges to.
  __weak MLNMapView* mapView = nullptr;
};
