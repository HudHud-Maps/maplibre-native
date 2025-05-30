#include <rapidjson/writer.h>
#include <rapidjson/stringbuffer.h>
#include <mbgl/style/expression/value.hpp>
#include <mbgl/style/conversion/stringify.hpp>

namespace mbgl {
namespace style {
namespace expression {

type::Type typeOf(const Value& value) {
    return value.match(
        [&](bool) -> type::Type { return type::Boolean; },
        [&](double) -> type::Type { return type::Number; },
        [&](const std::string&) -> type::Type { return type::String; },
        [&](const Color&) -> type::Type { return type::Color; },
        [&](const Padding&) -> type::Type { return type::Padding; },
        [&](const VariableAnchorOffsetCollection&) -> type::Type { return type::VariableAnchorOffsetCollection; },
        [&](const Collator&) -> type::Type { return type::Collator; },
        [&](const Formatted&) -> type::Type { return type::Formatted; },
        [&](const Image&) -> type::Type { return type::Image; },
        [&](const NullValue&) -> type::Type { return type::Null; },
        [&](const std::unordered_map<std::string, Value>&) -> type::Type { return type::Object; },
        [&](const std::vector<Value>& arr) -> type::Type {
            std::optional<type::Type> itemType;
            for (const auto& item : arr) {
                const type::Type t = typeOf(item);
                if (!itemType) {
                    itemType = {t};
                } else if (*itemType == t) {
                    continue;
                } else {
                    itemType = {type::Value};
                    break;
                }
            }

            return type::Array(itemType.value_or(type::Value), arr.size());
        });
}

std::string toString(const Value& value) {
    return value.match([](const NullValue&) { return std::string(); },
                       [](const Color& c) { return c.stringify(); },                         // avoid quoting
                       [](const VariableAnchorOffsetCollection& v) { return v.toString(); }, // avoid quoting
                       [](const Formatted& f) { return f.toString(); },
                       [](const Image& i) { return i.id(); },
                       [](const std::string& s) { return s; }, // avoid quoting
                       [](const auto& v_) { return stringify(v_); });
}

void writeJSON(rapidjson::Writer<rapidjson::StringBuffer>& writer, const Value& value) {
    value.match([&](const NullValue&) { writer.Null(); },
                [&](bool b) { writer.Bool(b); },
                [&](double f) {
                    // make sure integer values are stringified without trailing ".0".
                    f == std::floor(f) ? writer.Int(static_cast<int>(f)) : writer.Double(f);
                },
                [&](const std::string& s) { writer.String(s); },
                [&](const Color& c) { writer.String(c.stringify()); },
                [&](const Padding& p) { mbgl::style::conversion::stringify(writer, p); },
                [&](const VariableAnchorOffsetCollection& v) { mbgl::style::conversion::stringify(writer, v); },
                [&](const Collator&) {
                    // Collators are excluded from constant folding and there's no Literal parser
                    // for them so there shouldn't be any way to serialize this value.
                    assert(false);
                },
                [&](const Formatted& f) {
                    // `stringify` in turns calls ValueConverter::fromExpressionValue
                    // below Serialization strategy for Formatted objects is to return
                    // the constant expression that would generate them.
                    mbgl::style::conversion::stringify(writer, f);
                },
                [&](const Image& i) { mbgl::style::conversion::stringify(writer, i); },
                [&](const std::vector<Value>& arr) {
                    writer.StartArray();
                    for (const auto& item : arr) {
                        writeJSON(writer, item);
                    }
                    writer.EndArray();
                },
                [&](const std::unordered_map<std::string, Value>& obj) {
                    writer.StartObject();
                    for (const auto& entry : obj) {
                        writer.Key(entry.first.c_str());
                        writeJSON(writer, entry.second);
                    }
                    writer.EndObject();
                });
}

std::string stringify(const Value& value) {
    rapidjson::StringBuffer buffer;
    rapidjson::Writer<rapidjson::StringBuffer> writer(buffer);
    writeJSON(writer, value);
    return buffer.GetString();
}

struct FromMBGLValue {
    Value operator()(const std::vector<mbgl::Value>& v) {
        std::vector<Value> result;
        result.reserve(v.size());
        for (const auto& item : v) {
            result.emplace_back(toExpressionValue(item));
        }
        return result;
    }

    Value operator()(const std::unordered_map<std::string, mbgl::Value>& v) {
        std::unordered_map<std::string, Value> result;
        result.reserve(v.size());
        for (const auto& entry : v) {
            result.emplace(entry.first, toExpressionValue(entry.second));
        }
        return result;
    }

    Value operator()(const std::string& s) noexcept { return s; }
    Value operator()(const bool b) noexcept { return b; }
    Value operator()(const NullValue) noexcept { return Null; }
    Value operator()(const double v) noexcept { return v; }
    Value operator()(const uint64_t& v) noexcept { return static_cast<double>(v); }
    Value operator()(const int64_t& v) noexcept { return static_cast<double>(v); }
};

Value ValueConverter<mbgl::Value>::toExpressionValue(const mbgl::Value& value) {
    return mbgl::Value::visit(value, FromMBGLValue());
}

mbgl::Value ValueConverter<mbgl::Value>::fromExpressionValue(const Value& value) {
    return value.match(
        [&](const Color& color) -> mbgl::Value { return color.serialize(); },
        [&](const Padding& padding) -> mbgl::Value { return padding.serialize(); },
        [&](const VariableAnchorOffsetCollection& anchorOffset) -> mbgl::Value { return anchorOffset.serialize(); },
        [&](const Collator&) -> mbgl::Value {
            // fromExpressionValue can't be used for Collator values,
            // because they have no meaningful representation as an mbgl::Value
            assert(false);
            return mbgl::Value();
        },
        [&](const Formatted& formatted) -> mbgl::Value {
            // Serialization strategy for Formatted objects is to return the
            // constant expression that would generate them.
            std::vector<mbgl::Value> serialized;
            static std::string formatOperator("format");
            serialized.emplace_back(formatOperator);
            for (const auto& section : formatted.sections) {
                if (section.image) {
                    serialized.emplace_back(std::vector<mbgl::Value>{std::string("image"), section.image->id()});
                    continue;
                }

                serialized.emplace_back(section.text);
                std::unordered_map<std::string, mbgl::Value> options;

                if (section.fontScale) {
                    options.emplace("font-scale", *section.fontScale);
                }

                if (section.fontStack) {
                    std::vector<mbgl::Value> fontStack;
                    for (const auto& font : *section.fontStack) {
                        fontStack.emplace_back(font);
                    }
                    options.emplace("text-font", std::vector<mbgl::Value>{std::string("literal"), fontStack});
                }

                if (section.textColor) {
                    options.emplace("text-color", fromExpressionValue(*section.textColor));
                }

                serialized.emplace_back(options);
            }
            return serialized;
        },
        [&](const Image& i) -> mbgl::Value { return i.toValue(); },
        [&](const std::vector<Value>& values) -> mbgl::Value {
            std::vector<mbgl::Value> converted;
            converted.reserve(values.size());
            for (const Value& v : values) {
                converted.emplace_back(fromExpressionValue(v));
            }
            return converted;
        },
        [&](const std::unordered_map<std::string, Value>& values) -> mbgl::Value {
            std::unordered_map<std::string, mbgl::Value> converted;
            converted.reserve(values.size());
            for (const auto& entry : values) {
                converted.emplace(entry.first, fromExpressionValue(entry.second));
            }
            return converted;
        },
        [&](const auto& a) -> mbgl::Value { return a; });
}

Value ValueConverter<float>::toExpressionValue(const float value) {
    return static_cast<double>(value);
}

std::optional<float> ValueConverter<float>::fromExpressionValue(const Value& value) {
    return value.template is<double>() ? static_cast<float>(value.template get<double>()) : std::optional<float>();
}

template <typename T, typename Container>
std::vector<Value> toArrayValue(const Container& value) {
    std::vector<Value> result;
    result.reserve(value.size());
    for (const T& item : value) {
        result.push_back(ValueConverter<T>::toExpressionValue(item));
    }
    return result;
}

template <typename T, std::size_t N>
Value ValueConverter<std::array<T, N>>::toExpressionValue(const std::array<T, N>& value) {
    return toArrayValue<T>(value);
}

template <typename T, std::size_t N>
std::optional<std::array<T, N>> ValueConverter<std::array<T, N>>::fromExpressionValue(const Value& value) {
    return value.match(
        [&](const std::vector<Value>& v) -> std::optional<std::array<T, N>> {
            if (v.size() != N) return std::optional<std::array<T, N>>();
            std::array<T, N> result;
            auto it = result.begin();
            for (const Value& item : v) {
                std::optional<T> convertedItem = ValueConverter<T>::fromExpressionValue(item);
                if (!convertedItem) {
                    return std::optional<std::array<T, N>>();
                }
                *it = *convertedItem;
                it = std::next(it);
            }
            return result;
        },
        [&](const auto&) { return std::optional<std::array<T, N>>(); });
}

template <typename T>
Value ValueConverter<std::vector<T>>::toExpressionValue(const std::vector<T>& value) {
    return toArrayValue<T>(value);
}

template <typename T>
std::optional<std::vector<T>> ValueConverter<std::vector<T>>::fromExpressionValue(const Value& value) {
    return value.match(
        [&](const std::vector<Value>& v) -> std::optional<std::vector<T>> {
            std::vector<T> result;
            result.reserve(v.size());
            for (const Value& item : v) {
                std::optional<T> convertedItem = ValueConverter<T>::fromExpressionValue(item);
                if (!convertedItem) {
                    return std::optional<std::vector<T>>();
                }
                result.push_back(*convertedItem);
            }
            return result;
        },
        [&](const auto&) { return std::optional<std::vector<T>>(); });
}

Value ValueConverter<Position>::toExpressionValue(const mbgl::style::Position& value) {
    return ValueConverter<std::array<float, 3>>::toExpressionValue(value.getSpherical());
}

std::optional<Position> ValueConverter<Position>::fromExpressionValue(const Value& v) {
    auto pos = ValueConverter<std::array<float, 3>>::fromExpressionValue(v);
    return pos ? std::optional<Position>(Position(*pos)) : std::optional<Position>();
}

Value ValueConverter<Rotation>::toExpressionValue(const mbgl::style::Rotation& value) {
    return ValueConverter<float>::toExpressionValue(static_cast<float>(value.getAngle()));
}

std::optional<Rotation> ValueConverter<Rotation>::fromExpressionValue(const Value& v) {
    auto angle = ValueConverter<float>::fromExpressionValue(v);
    return angle ? std::optional<Rotation>(Rotation(*angle)) : std::optional<Rotation>();
}

template <typename T>
Value ValueConverter<T, std::enable_if_t<std::is_enum_v<T>>>::toExpressionValue(const T& value) {
    return std::string(Enum<T>::toString(value));
}

template <typename T>
std::optional<T> ValueConverter<T, std::enable_if_t<std::is_enum_v<T>>>::fromExpressionValue(const Value& value) {
    return value.match([&](const std::string& v) { return Enum<T>::toEnum(v); },
                       [&](const auto&) { return std::optional<T>(); });
}

template <typename T>
type::Type valueTypeToExpressionType() {
    return ValueConverter<T>::expressionType();
}

template <>
type::Type valueTypeToExpressionType<Value>() {
    return type::Value;
}
template <>
type::Type valueTypeToExpressionType<NullValue>() {
    return type::Null;
}
template <>
type::Type valueTypeToExpressionType<bool>() {
    return type::Boolean;
}
template <>
type::Type valueTypeToExpressionType<double>() {
    return type::Number;
}
template <>
type::Type valueTypeToExpressionType<std::string>() {
    return type::String;
}
template <>
type::Type valueTypeToExpressionType<Color>() {
    return type::Color;
}
template <>
type::Type valueTypeToExpressionType<Padding>() {
    return type::Padding;
}
template <>
type::Type valueTypeToExpressionType<VariableAnchorOffsetCollection>() {
    return type::VariableAnchorOffsetCollection;
}
template <>
type::Type valueTypeToExpressionType<Collator>() {
    return type::Collator;
}
template <>
type::Type valueTypeToExpressionType<Formatted>() {
    return type::Formatted;
}
template <>
type::Type valueTypeToExpressionType<Image>() {
    return type::Image;
}
template <>
type::Type valueTypeToExpressionType<std::unordered_map<std::string, Value>>() {
    return type::Object;
}
template <>
type::Type valueTypeToExpressionType<std::vector<Value>>() {
    return type::Array(type::Value);
}

// used only for the special (and private) "error" expression
template <>
type::Type valueTypeToExpressionType<type::ErrorType>() {
    return type::Error;
}

// for to_rgba expression
template type::Type valueTypeToExpressionType<std::array<double, 4>>();
template struct ValueConverter<std::array<double, 4>>;

// for LocationIndicator position
template type::Type valueTypeToExpressionType<std::array<double, 3>>();
template struct ValueConverter<std::array<double, 3>>;

// layout/paint property types
template type::Type valueTypeToExpressionType<float>();
template type::Type valueTypeToExpressionType<Position>();
template type::Type valueTypeToExpressionType<Rotation>();

template type::Type valueTypeToExpressionType<std::array<float, 2>>();
template struct ValueConverter<std::array<float, 2>>;

template type::Type valueTypeToExpressionType<std::array<float, 4>>();
template struct ValueConverter<std::array<float, 4>>;

template type::Type valueTypeToExpressionType<std::vector<float>>();
template struct ValueConverter<std::vector<float>>;

template type::Type valueTypeToExpressionType<std::vector<std::string>>();
template struct ValueConverter<std::vector<std::string>>;

template type::Type valueTypeToExpressionType<std::vector<TextVariableAnchorType>>();
template struct ValueConverter<std::vector<TextVariableAnchorType>>;

template type::Type valueTypeToExpressionType<AlignmentType>();
template struct ValueConverter<AlignmentType>;

template type::Type valueTypeToExpressionType<CirclePitchScaleType>();
template struct ValueConverter<CirclePitchScaleType>;

template type::Type valueTypeToExpressionType<IconTextFitType>();
template struct ValueConverter<IconTextFitType>;

template type::Type valueTypeToExpressionType<LineCapType>();
template struct ValueConverter<LineCapType>;

template type::Type valueTypeToExpressionType<LineJoinType>();
template struct ValueConverter<LineJoinType>;

template type::Type valueTypeToExpressionType<SymbolPlacementType>();
template struct ValueConverter<SymbolPlacementType>;

template type::Type valueTypeToExpressionType<SymbolZOrderType>();
template struct ValueConverter<SymbolZOrderType>;

template type::Type valueTypeToExpressionType<SymbolAnchorType>();
template struct ValueConverter<SymbolAnchorType>;

template type::Type valueTypeToExpressionType<TextJustifyType>();
template struct ValueConverter<TextJustifyType>;

template type::Type valueTypeToExpressionType<TextTransformType>();
template struct ValueConverter<TextTransformType>;

template type::Type valueTypeToExpressionType<TranslateAnchorType>();
template struct ValueConverter<TranslateAnchorType>;

template type::Type valueTypeToExpressionType<RasterResamplingType>();
template struct ValueConverter<RasterResamplingType>;

template type::Type valueTypeToExpressionType<HillshadeIlluminationAnchorType>();
template struct ValueConverter<HillshadeIlluminationAnchorType>;

template type::Type valueTypeToExpressionType<LightAnchorType>();
template struct ValueConverter<LightAnchorType>;

template type::Type valueTypeToExpressionType<std::vector<TextWritingModeType>>();
template struct ValueConverter<std::vector<TextWritingModeType>>;

} // namespace expression
} // namespace style
} // namespace mbgl
