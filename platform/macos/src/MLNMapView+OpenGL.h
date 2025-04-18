#import "MLNMapView+Impl.h"
#import "MLNMapView_Private.h"

#include <mbgl/gfx/renderable.hpp>
#include <mbgl/gl/renderer_backend.hpp>

/// Adapter responsible for bridging calls from mbgl to MLNMapView and Cocoa.
class MLNMapViewOpenGLImpl final : public MLNMapViewImpl,
                                   public mbgl::gl::RendererBackend,
                                   public mbgl::gfx::Renderable {
public:
    MLNMapViewOpenGLImpl(MLNMapView*);
    ~MLNMapViewOpenGLImpl() override = default;

public:
    void restoreFramebufferBinding();

    // Implementation of mbgl::gfx::RendererBackend
public:
    mbgl::gfx::Renderable& getDefaultRenderable() override { return *this; }

private:
    void activate() override;
    void deactivate() override;
    // End implementation of mbgl::gfx::RendererBackend

    // Implementation of mbgl::gl::RendererBackend
public:
    void updateAssumedState() override;

private:
    mbgl::gl::ProcAddress getExtensionFunctionPointer(const char* name) override;
    // End implementation of mbgl::gl::Rendererbackend

    // Implementation of MLNMapViewImpl
public:
    mbgl::gfx::RendererBackend& getRendererBackend() override { return *this; }

    mbgl::PremultipliedImage readStillImage() override;
    CGLContextObj getCGLContextObj() override;

    MLNBackendResource getObject() override;
};
