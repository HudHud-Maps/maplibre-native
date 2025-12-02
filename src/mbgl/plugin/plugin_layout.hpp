#pragma once

#include <mbgl/layout/layout.hpp>
#include <mbgl/style/layer_properties.hpp>
#include <mbgl/style/image_impl.hpp>
#include <mbgl/layout/symbol_feature.hpp>
#include <mbgl/renderer/bucket_parameters.hpp>
#include <mbgl/plugin/feature_collection.hpp>
#include <mbgl/text/bidi.hpp>

namespace mbgl {

class PluginLayout final : public Layout {
public:
    PluginLayout(const BucketParameters&,
                 const std::vector<Immutable<style::LayerProperties>>&,
                 std::unique_ptr<GeometryTileLayer>,
                 const LayoutParameters& parameters);

    ~PluginLayout() final = default;

    void prepareSymbols(const GlyphMap& glyphMap,
                        const GlyphPositions&,
                        const ImageMap&,
                        const ImagePositions&) override;

    void createBucket(const ImagePositions&,
                      std::unique_ptr<FeatureIndex>&,
                      mbgl::unordered_map<std::string, LayerRenderData>&,
                      bool firstLoad,
                      bool showCollisionBoxes,
                      const CanonicalTileID& canonical) override;

    bool hasSymbolInstances() const override;
    bool hasDependencies() const override;

private:
    const std::unique_ptr<GeometryTileLayer> sourceLayer;
    const CanonicalTileID canonicalID;
    const BucketParameters parameters;
    const std::vector<Immutable<style::LayerProperties>> layers;

    std::vector<SymbolFeature> features;
    std::vector<std::pair<TaggedString, std::string>> formattedTexts;
    plugin::SpriteToTexMapping spriteIdToTex;
    plugin::GlyphToTexMapping glyphToTex;

    BiDi bidi; // Consider moving this up to geometry tile worker to reduce
               // reinstantiation costs; use of BiDi/ubiditransform object must
               // be constrained to one thread
};
} // namespace mbgl
