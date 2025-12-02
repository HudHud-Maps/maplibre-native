#pragma once

#include <mbgl/tile/tile_id.hpp>
#include <mbgl/util/rect.hpp>
#include <mbgl/gfx/texture2d.hpp>
#include <mbgl/util/containers.hpp>
#include <mbgl/text/quads.hpp>

#include <map>
#include <memory>
#include <string>
#include <vector>

namespace mbgl {

namespace plugin {

using SpriteToTexMapping = mbgl::unordered_map<std::string, SymbolQuads>;
using GlyphToTexMapping = mbgl::unordered_map<std::string, SymbolQuads>;

class FeatureCoordinate {
public:
    FeatureCoordinate(double lat, double lon, double tileX, double tileY)
        : _lat(lat),
          _lon(lon),
          _tileX(tileX),
          _tileY(tileY) {}
    double _lat = 0;
    double _lon = 0;
    double _tileX = 0; // Tile coord
    double _tileY = 0; // Tile coord
};

// This is a list of coordinates.  Broken out into its own class because
// a raw bucket feature can have an array of these
class FeatureCoordinateCollection {
public:
    std::vector<FeatureCoordinate> _coordinates;
};

class Feature {
public:
    Feature() {};
    enum class FeatureType {
        FeatureTypeUnknown,
        FeatureTypePoint,
        FeatureTypeLine,
        FeatureTypePolygon
    };
    FeatureType _featureType = FeatureType::FeatureTypeUnknown;
    std::map<std::string, std::string> _featureProperties;
    std::vector<FeatureCoordinateCollection> _featureCoordinates;
    std::string _featureID; // Unique id from the data source for this
};

class FeatureCollection {
public:
    FeatureCollection(OverscaledTileID tileID)
        : _featureCollectionTileID(tileID) {};
    std::vector<std::shared_ptr<Feature>> _features;
    OverscaledTileID _featureCollectionTileID;
    SpriteToTexMapping _sprites;
    GlyphToTexMapping _glyphs;
    std::shared_ptr<gfx::Texture2D> _spriteAtlas, _glyphAtlas;
};

class FeatureSymbolProperty {
public:
    std::string name;
    enum Type {
        LITERAL,
        PROPERTY
    } type;
};

} // namespace plugin

} // namespace mbgl
