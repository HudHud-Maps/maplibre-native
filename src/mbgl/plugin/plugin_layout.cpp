#include <mbgl/plugin/plugin_layout.hpp>
#include <mbgl/renderer/bucket_parameters.hpp>
#include <mbgl/style/expression/formatted.hpp>
#include <mbgl/util/utf.hpp>
#include <mbgl/util/logging.hpp>
#include <mbgl/text/quads.hpp>
#include <mbgl/plugin/plugin_layer.hpp>
#include <mbgl/plugin/plugin_layer_impl.hpp>
#include <mbgl/text/shaping.hpp>
#include <mbgl/layout/symbol_instance.hpp>
#include <mbgl/plugin/feature_collection_bucket.hpp>

namespace mbgl {

PluginLayout::PluginLayout(const BucketParameters& parameters_,
                           const std::vector<Immutable<style::LayerProperties>>& layers_,
                           std::unique_ptr<GeometryTileLayer> sourceLayer_,
                           const LayoutParameters& layoutParameters)
    : sourceLayer(std::move(sourceLayer_)),
      canonicalID(parameters_.tileID.canonical),
      parameters(parameters_),
      layers(layers_) {
    std::vector<plugin::FeatureSymbolProperty> spriteProperties;
    std::vector<plugin::FeatureSymbolProperty> glyphProperties;
    FontStack baseFontStack;
    for (const auto& layer : layers) {
        auto bi = layer->baseImpl;
        auto bip = bi.get();
        auto pluginLayer = static_cast<const mbgl::style::PluginLayer::Impl*>(bip);

        if (pluginLayer != nullptr) {
            if (pluginLayer->_spritePropertiesFunction) {
                const auto& layerSpriteProperties = pluginLayer->_spritePropertiesFunction();
                for (const auto& property : layerSpriteProperties) {
                    if (property.type == plugin::FeatureSymbolProperty::Type::LITERAL) {
                        layoutParameters.imageDependencies.emplace(property.name, ImageType::Icon);
                    } else {
                        spriteProperties.push_back(property);
                    }
                }
            }

            if (pluginLayer->_glyphPropertiesFunction) {
                const auto& layerGlyphProperties = pluginLayer->_glyphPropertiesFunction();
                for (const auto& property : layerGlyphProperties) {
                    if (property.type == plugin::FeatureSymbolProperty::Type::LITERAL) {
                        // TODO(yousifd): Add literal value to dependencies
                    } else {
                        glyphProperties.push_back(property);
                    }
                }
            }

            if (pluginLayer->_baseFontStackFunction) {
                baseFontStack = pluginLayer->_baseFontStackFunction();
            }
        }
    }

    const size_t featureCount = sourceLayer->featureCount();
    for (size_t i = 0; i < featureCount; ++i) {
        auto feature = sourceLayer->getFeature(i);

        SymbolFeature ft(std::move(feature));
        ft.index = i;

        for (const auto& property : spriteProperties) {
            const auto& it = ft.getProperties().find(property.name);
            if (it != ft.getProperties().end()) {
                ft.icon = style::expression::Image(*it->second.getString());
                layoutParameters.imageDependencies.emplace(ft.icon->id(), ImageType::Icon);
            }
        }

        for (const auto& property : glyphProperties) {
            const auto& it = ft.getProperties().find(property.name);
            if (it != ft.getProperties().end()) {
                TaggedString formattedText;
                std::string u8string(it->second.getString()->c_str());
                try {
                    formattedText.addTextSection(
                        applyArabicShaping(util::convertUTF8ToUTF16(u8string)), 1.0, baseFontStack);
                } catch (...) {
                    mbgl::Log::Error(
                        mbgl::Event::ParseTile,
                        "Encountered section with invalid UTF-8 in tile, source: " + sourceLayer->getName() +
                            " z: " + std::to_string(canonicalID.z) + " x: " + std::to_string(canonicalID.x) +
                            " y: " + std::to_string(canonicalID.y));
                    continue; // skip section
                }

                formattedTexts.push_back({formattedText, u8string});

                // Loop through all characters of this text and collect unique codepoints.
                for (std::size_t j = 0; j < formattedText.length(); j++) {
                    GlyphIDs& dependencies = layoutParameters.glyphDependencies[baseFontStack];
                    char16_t codePoint = formattedText.getCharCodeAt(j);
                    dependencies.insert(codePoint);
                }
            }
        }

        features.push_back(std::move(ft));
    }
}

bool PluginLayout::hasDependencies() const {
    return !features.empty();
}

bool PluginLayout::hasSymbolInstances() const {
    return true;
}

void PluginLayout::prepareSymbols(const GlyphMap& glyphMap,
                                  const GlyphPositions& glyphPositions,
                                  const ImageMap& imageMap,
                                  const ImagePositions& imagePositions) {
    for (const auto& feature : features) {
        if (feature.geometry.empty()) continue;

        if (!formattedTexts.empty()) {
            for (const auto& formattedText : formattedTexts) {
                const auto& it = glyphToTex.find(formattedText.second);
                if (it != glyphToTex.end()) {
                    continue;
                }

                // TODO(yousifd): Customize all hard coded properties
                Shaping shaping = getShaping(
                    /* string */ formattedText.first,
                    /* maxWidth: ems */
                    //                    isPointPlacement ? layout->evaluate<TextMaxWidth>(zoom, feature, canonicalID)
                    //                    * util::ONE_EM : 0.0f,
                    0.0f,
                    //                    /* ems */ lineHeight,
                    util::ONE_EM,
                    style::SymbolAnchorType::Center,
                    style::TextJustifyType::Center,
                    //                    /* ems */ spacing,
                    0.0f,
                    /* translate */ {0, 0},
                    /* writingMode */ WritingModeType::Horizontal,
                    /* bidirectional algorithm object */ bidi,
                    glyphMap,
                    /* glyphs */ glyphPositions,
                    /* images */ imagePositions,
                    //                    layoutTextSize,
                    1.f,
                    //                    layoutTextSizeAtBucketZoomLevel,
                    1.f,
                    false);

                // TODO(yousifd): Placement type should be customizable
                //                style::SymbolLayoutProperties::Evaluated layout;
                //                layout.get<style::TextRotate>() = 100;
                //                printf("TEXT ROTATE: %f\n", layout.get<style::TextRotate>());
                glyphToTex[formattedText.second] = getGlyphQuads(
                    shaping, {0, 0}, {}, style::SymbolPlacementType::Point, imageMap, false);
            }
        }

        if (feature.icon) {
            auto image = imageMap.find(feature.icon->id());
            PositionedIcon shapedIcon = PositionedIcon::shapeIcon(
                imagePositions.at(feature.icon->id()), {0, 0}, style::SymbolAnchorType::Center);
            // TODO(yousifd): Deal with SDF Images
            spriteIdToTex[feature.icon->id()] = getIconQuads(shapedIcon, 0.f, SymbolContent::IconRGBA, false);
        }
    }
}

void PluginLayout::createBucket(const ImagePositions& imagePositions,
                                std::unique_ptr<FeatureIndex>&,
                                mbgl::unordered_map<std::string, LayerRenderData>& renderData,
                                const bool firstLoad,
                                const bool showCollisionBoxes,
                                const CanonicalTileID& canonical) {
    auto bucket = std::make_shared<FeatureCollectionBucket>(parameters, layers);
    bucket->_spriteIdToTex = spriteIdToTex;
    bucket->_glyphToTex = glyphToTex;

    for (const auto& feature : features) {
        bucket->addFeature(*feature.feature, feature.getGeometries(), {}, PatternLayerMap(), feature.index, canonical);
    }

    if (bucket->hasData()) {
        for (const auto& layer : layers) {
            renderData.emplace(layer->baseImpl->id, LayerRenderData{bucket, layer});
        }
    }
}

} // namespace mbgl
