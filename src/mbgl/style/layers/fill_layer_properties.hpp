// clang-format off

// This file is generated. Edit scripts/generate-style-code.js, then run `make style-code`.

#pragma once

#include <mbgl/style/types.hpp>
#include <mbgl/style/layer_properties.hpp>
#include <mbgl/style/layers/fill_layer.hpp>
#include <mbgl/style/layout_property.hpp>
#include <mbgl/style/paint_property.hpp>
#include <mbgl/style/properties.hpp>
#include <mbgl/shaders/attributes.hpp>
#include <mbgl/shaders/uniforms.hpp>

namespace mbgl {
namespace style {

struct FillSortKey : DataDrivenLayoutProperty<float> {
    static constexpr const char *name() { return "fill-sort-key"; }
    static float defaultValue() { return 0.f; }
};

struct FillAntialias : PaintProperty<bool> {
    static bool defaultValue() { return true; }
};

struct FillColor : DataDrivenPaintProperty<Color, attributes::color, uniforms::color> {
    static Color defaultValue() { return Color::black(); }
};

struct FillOpacity : DataDrivenPaintProperty<float, attributes::opacity, uniforms::opacity> {
    static float defaultValue() { return 1.f; }
};

struct FillOutlineColor : DataDrivenPaintProperty<Color, attributes::outline_color, uniforms::outline_color> {
    static Color defaultValue() { return {}; }
};

struct FillPattern : CrossFadedDataDrivenPaintProperty<expression::Image, attributes::pattern_to, uniforms::pattern_to, attributes::pattern_from, uniforms::pattern_from> {
    static expression::Image defaultValue() { return {}; }
};

struct FillTranslate : PaintProperty<std::array<float, 2>> {
    static std::array<float, 2> defaultValue() { return {{0.f, 0.f}}; }
};

struct FillTranslateAnchor : PaintProperty<TranslateAnchorType> {
    static TranslateAnchorType defaultValue() { return TranslateAnchorType::Map; }
};

class FillLayoutProperties : public Properties<
    FillSortKey
> {};

class FillPaintProperties : public Properties<
    FillAntialias,
    FillColor,
    FillOpacity,
    FillOutlineColor,
    FillPattern,
    FillTranslate,
    FillTranslateAnchor
> {};

class FillLayerProperties final : public LayerProperties {
public:
    explicit FillLayerProperties(Immutable<FillLayer::Impl>);
    FillLayerProperties(
        Immutable<FillLayer::Impl>,
        CrossfadeParameters,
        FillPaintProperties::PossiblyEvaluated);
    ~FillLayerProperties() override;

    unsigned long constantsMask() const override;

    expression::Dependency getDependencies() const noexcept override;

    const FillLayer::Impl& layerImpl() const noexcept;
    // Data members.
    CrossfadeParameters crossfade;
    FillPaintProperties::PossiblyEvaluated evaluated;
};

} // namespace style
} // namespace mbgl

// clang-format on
