#import "MLNPluginLayer.h"

#include <mbgl/gfx/texture2d.hpp>
#include <mbgl/text/quads.hpp>

#include <memory>

@interface MLNTexture (Private)

- (instancetype)initWithTexture:(std::shared_ptr<mbgl::gfx::Texture2D>)texture;

+ (instancetype)textureWithTexture:(std::shared_ptr<mbgl::gfx::Texture2D>)texture;

- (void)setTexture:(std::shared_ptr<mbgl::gfx::Texture2D>)texture;

- (std::shared_ptr<mbgl::gfx::Texture2D>)getTexture;

@end

@interface MLNQuad (Private)

- (instancetype)initWithSymbolQuad:(const mbgl::SymbolQuad&)quad;

+ (instancetype)quadWithSymbolQuad:(const mbgl::SymbolQuad&)quad;

@end

typedef void (^MLNPluginLayerTextureBindingCallback)(std::shared_ptr<mbgl::gfx::Texture2D>,
                                                     int32_t);

@interface MLNPluginLayer (Private)

- (void)setTextureBindingCallback:(MLNPluginLayerTextureBindingCallback)callback;

@end
