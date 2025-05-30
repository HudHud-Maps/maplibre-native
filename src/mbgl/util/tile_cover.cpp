#include <mbgl/math/log2.hpp>
#include <mbgl/util/bounding_volumes.hpp>
#include <mbgl/util/constants.hpp>
#include <mbgl/util/interpolate.hpp>
#include <mbgl/util/tile_coordinate.hpp>
#include <mbgl/util/tile_cover.hpp>
#include <mbgl/util/tile_cover_impl.hpp>

#include <functional>
#include <list>

using namespace std::numbers;

namespace mbgl {

namespace {

using ScanLine = const std::function<void(int32_t x0, int32_t x1, int32_t y)>;

// Taken from polymaps src/Layer.js
// https://github.com/simplegeo/polymaps/blob/master/src/Layer.js#L333-L383
struct edge {
    double x0 = 0, y0 = 0;
    double x1 = 0, y1 = 0;
    double dx = 0, dy = 0;

    edge(Point<double> a, Point<double> b) {
        if (a.y > b.y) std::swap(a, b);
        x0 = a.x;
        y0 = a.y;
        x1 = b.x;
        y1 = b.y;
        dx = b.x - a.x;
        dy = b.y - a.y;
    }
};

// scan-line conversion
void scanSpans(edge e0, edge e1, int32_t ymin, int32_t ymax, ScanLine& scanLine) {
    const double y0 = ::fmax(ymin, std::floor(e1.y0));
    const double y1 = ::fmin(ymax, std::ceil(e1.y1));

    // sort edges by x-coordinate
    if ((e0.x0 == e1.x0 && e0.y0 == e1.y0) ? (e0.x0 + e1.dy / e0.dy * e0.dx < e1.x1)
                                           : (e0.x1 - e1.dy / e0.dy * e0.dx < e1.x0)) {
        std::swap(e0, e1);
    }

    // scan lines!
    const double m0 = e0.dx / e0.dy;
    const double m1 = e1.dx / e1.dy;
    const double d0 = e0.dx > 0; // use y + 1 to compute x0
    const double d1 = e1.dx < 0; // use y + 1 to compute x1
    for (double y = y0; y < y1; y++) {
        double x0 = m0 * ::fmax(0, ::fmin(e0.dy, y + d0 - e0.y0)) + e0.x0;
        double x1 = m1 * ::fmax(0, ::fmin(e1.dy, y + d1 - e1.y0)) + e1.x0;
        scanLine(static_cast<int32_t>(std::floor(x1)), static_cast<int32_t>(std::ceil(x0)), static_cast<int32_t>(y));
    }
}

// scan-line conversion
void scanTriangle(const Point<double>& a,
                  const Point<double>& b,
                  const Point<double>& c,
                  int32_t ymin,
                  int32_t ymax,
                  ScanLine& scanLine) {
    edge ab = edge(a, b);
    edge bc = edge(b, c);
    edge ca = edge(c, a);

    // sort edges by y-length
    if (ab.dy > bc.dy) {
        std::swap(ab, bc);
    }
    if (ab.dy > ca.dy) {
        std::swap(ab, ca);
    }
    if (bc.dy > ca.dy) {
        std::swap(bc, ca);
    }

    // scan span! scan span!
    if (ab.dy) scanSpans(ca, ab, ymin, ymax, scanLine);
    if (bc.dy) scanSpans(ca, bc, ymin, ymax, scanLine);
}

} // namespace

namespace util {

namespace {

std::vector<UnwrappedTileID> tileCover(const Point<double>& tl,
                                       const Point<double>& tr,
                                       const Point<double>& br,
                                       const Point<double>& bl,
                                       const Point<double>& c,
                                       uint8_t z) {
    const int32_t tiles = 1 << z;

    struct ID {
        int32_t x, y;
        double sqDist;
    };

    std::vector<ID> t;

    // skip the first few allocations, assuming we usually end up with at least a few tiles
    t.reserve(8);

    auto scanLine = [&](int32_t x0, int32_t x1, int32_t y) {
        int32_t x;
        if (y >= 0 && y <= tiles) {
            for (x = x0; x < x1; ++x) {
                const auto dx = x + 0.5 - c.x;
                const auto dy = y + 0.5 - c.y;
                t.emplace_back(ID{x, y, dx * dx + dy * dy});
            }
        }
    };

    // Divide the screen up in two triangles and scan each of them:
    // \---+
    // | \ |
    // +---\.
    scanTriangle(tl, tr, br, 0, tiles, scanLine);
    scanTriangle(br, bl, tl, 0, tiles, scanLine);

    // Sort first by distance, then by x/y.
    std::sort(t.begin(), t.end(), [](const ID& a, const ID& b) noexcept {
        return std::tie(a.sqDist, a.x, a.y) < std::tie(b.sqDist, b.x, b.y);
    });

    // Erase duplicate tile IDs (they typically occur at the common side of both triangles).
    t.erase(std::unique(t.begin(), t.end(), [](const ID& a, const ID& b) { return a.x == b.x && a.y == b.y; }),
            t.end());

    std::vector<UnwrappedTileID> result;
    result.reserve(t.size());
    for (const auto& id : t) {
        result.emplace_back(z, id.x, id.y);
    }
    return result;
}

} // namespace

int32_t coveringZoomLevel(double zoom, style::SourceType type, uint16_t size) noexcept {
    zoom += util::log2(util::tileSize_D / size);
    if (type == style::SourceType::Raster || type == style::SourceType::Video) {
        return static_cast<int32_t>(std::round(zoom));
    } else {
        return static_cast<int32_t>(std::floor(zoom));
    }
}

std::vector<OverscaledTileID> tileCover(const TileCoverParameters& state,
                                        uint8_t z,
                                        const std::optional<uint8_t>& overscaledZ) {
    struct Node {
        AABB aabb;
        uint8_t zoom;
        uint32_t x, y;
        int16_t wrap;
        bool fullyVisible;
    };

    struct ResultTile {
        OverscaledTileID id;
        double sqrDist;
    };

    const auto& transform = state.transformState;
    const double numTiles = std::pow(2.0, z);
    const double worldSize = Projection::worldSize(transform.getScale());
    const uint8_t minZoom = transform.getPitch() <= state.tileLodPitchThreshold ? z : 0;
    const uint8_t maxZoom = z;
    const uint8_t overscaledZoom = std::max(overscaledZ.value_or(z), z);
    const bool flippedY = transform.getViewportMode() == ViewportMode::FlippedY;

    const auto centerPoint = TileCoordinate::fromScreenCoordinate(
                                 transform, z, {transform.getSize().width / 2.0, transform.getSize().height / 2.0})
                                 .p;

    const vec3 centerCoord = {{centerPoint.x, centerPoint.y, 0.0}};

    const Frustum frustum = Frustum::fromInvProjMatrix(transform.getInvProjectionMatrix(), worldSize, z, flippedY);

    // There should always be a certain number of maximum zoom level tiles
    // surrounding the center location
    assert(state.tileLodMinRadius >= 1);
    const double radiusOfMaxLvlLodInTiles = std::max(1.0, state.tileLodMinRadius);

    const auto newRootTile = [&](int16_t wrap) -> Node {
        return {AABB({{wrap * numTiles, 0.0, 0.0}}, {{(wrap + 1) * numTiles, numTiles, 0.0}}),
                uint8_t(0),
                uint16_t(0),
                uint16_t(0),
                wrap,
                false};
    };

    // Perform depth-first traversal on tile tree to find visible tiles
    std::vector<Node> stack;
    std::vector<ResultTile> result;
    stack.reserve(128);

    // World copies shall be rendered three times on both sides from closest to farthest
    for (int i = 1; i <= 3; i++) {
        stack.push_back(newRootTile(-i));
        stack.push_back(newRootTile(i));
    }

    stack.push_back(newRootTile(0));

    while (!stack.empty()) {
        Node node = stack.back();
        stack.pop_back();

        // Use cached visibility information of ancestor nodes
        if (!node.fullyVisible) {
            const IntersectionResult intersection = frustum.intersects(node.aabb);

            if (intersection == IntersectionResult::Separate) continue;

            node.fullyVisible = intersection == IntersectionResult::Contains;
        }

        const vec3 distanceXyz = node.aabb.distanceXYZ(centerCoord);
        const double* longestDim = std::max_element(distanceXyz.data(), distanceXyz.data() + distanceXyz.size());
        assert(longestDim);

        // We're using distance based heuristics to determine if a tile should
        // be split into quadrants or not. radiusOfMaxLvlLodInTiles defines that
        // there's always a certain number of maxLevel tiles next to the map
        // center. Using the fact that a parent node in quadtree is twice the
        // size of its children (per dimension) we can define distance
        // thresholds for each relative level:
        // f(k) = offset + 2 + 4 + 8 + 16 + ... + 2^k
        // This is the same as:
        // f(k) = offset + 2^(k+1)-2
        const double distToSplit = radiusOfMaxLvlLodInTiles + (1 << (maxZoom - node.zoom)) - 2;

        // Have we reached the target depth or is the tile too far away to be any split further?
        if (node.zoom == maxZoom || (*longestDim * state.tileLodScale > distToSplit && node.zoom >= minZoom)) {
            // Perform precise intersection test between the frustum and aabb.
            // This will cull < 1% false positives missed by the original test
            if (node.fullyVisible || frustum.intersectsPrecise(node.aabb, true) != IntersectionResult::Separate) {
                const OverscaledTileID id = {
                    node.zoom == maxZoom ? overscaledZoom : node.zoom, node.wrap, node.zoom, node.x, node.y};
                const double dx = node.wrap * numTiles + node.x + 0.5 - centerCoord[0];
                const double dy = node.y + 0.5 - centerCoord[1];

                result.push_back({id, dx * dx + dy * dy});
            }
            continue;
        }

        for (int i = 0; i < 4; i++) {
            const uint32_t childX = (node.x << 1) + (i % 2);
            const uint32_t childY = (node.y << 1) + (i >> 1);

            // Create child node and push to the stack for traversal
            stack.emplace_back(node);
            Node& child = stack.back();

            child.aabb = node.aabb.quadrant(i);
            child.zoom = node.zoom + 1;
            child.x = childX;
            child.y = childY;
        }
    }

    // Sort results by distance
    std::sort(
        result.begin(), result.end(), [](const ResultTile& a, const ResultTile& b) { return a.sqrDist < b.sqrDist; });

    std::vector<OverscaledTileID> ids;
    ids.reserve(result.size());

    for (const auto& tile : result) {
        ids.push_back(tile.id);
    }

    return ids;
}

std::vector<UnwrappedTileID> tileCover(const LatLngBounds& bounds_, uint8_t z) {
    if (bounds_.isEmpty() || bounds_.south() > util::LATITUDE_MAX || bounds_.north() < -util::LATITUDE_MAX) {
        return {};
    }

    const LatLngBounds bounds = LatLngBounds::hull({std::max(bounds_.south(), -util::LATITUDE_MAX), bounds_.west()},
                                                   {std::min(bounds_.north(), util::LATITUDE_MAX), bounds_.east()});

    return tileCover(Projection::project(bounds.northwest(), z),
                     Projection::project(bounds.northeast(), z),
                     Projection::project(bounds.southeast(), z),
                     Projection::project(bounds.southwest(), z),
                     Projection::project(bounds.center(), z),
                     z);
}

std::vector<UnwrappedTileID> tileCover(const Geometry<double>& geometry, uint8_t z) {
    std::vector<UnwrappedTileID> result;
    TileCover tc(geometry, z, true);
    while (tc.hasNext()) {
        result.push_back(*tc.next());
    };

    return result;
}

// Taken from https://github.com/mapbox/sphericalmercator#xyzbbox-zoom-tms_style-srs
// Computes the projected tiles for the lower left and upper right points of the bounds
// and uses that to compute the tile cover count
uint64_t tileCount(const LatLngBounds& bounds, uint8_t zoom) noexcept {
    if (zoom == 0) {
        return 1;
    }
    const auto sw = Projection::project(bounds.southwest(), zoom);
    const auto ne = Projection::project(bounds.northeast(), zoom);
    const auto maxTile = std::pow(2.0, zoom);
    const auto x1 = floor(sw.x);
    const auto x2 = ceil(ne.x) - 1;
    const auto y1 = util::clamp(floor(sw.y), 0.0, maxTile - 1);
    const auto y2 = util::clamp(floor(ne.y), 0.0, maxTile - 1);

    const auto dx = x1 > x2 ? (maxTile - x1) + x2 : x2 - x1;
    const auto dy = y1 - y2;
    return static_cast<uint64_t>((dx + 1) * (dy + 1));
}

uint64_t tileCount(const Geometry<double>& geometry, uint8_t z) {
    uint64_t tileCount = 0;

    TileCover tc(geometry, z, true);
    while (tc.next()) {
        tileCount++;
    };
    return tileCount;
}

TileCover::TileCover(const LatLngBounds& bounds_, uint8_t z) {
    LatLngBounds bounds = LatLngBounds::hull({std::max(bounds_.south(), -util::LATITUDE_MAX), bounds_.west()},
                                             {std::min(bounds_.north(), util::LATITUDE_MAX), bounds_.east()});

    if (bounds.isEmpty() || bounds.south() > util::LATITUDE_MAX || bounds.north() < -util::LATITUDE_MAX) {
        bounds = LatLngBounds::world();
    }

    const auto sw = Projection::project(bounds.southwest(), z);
    const auto ne = Projection::project(bounds.northeast(), z);
    const auto se = Projection::project(bounds.southeast(), z);
    const auto nw = Projection::project(bounds.northwest(), z);

    const Polygon<double> p({{sw, nw, ne, se, sw}});
    impl = std::make_unique<TileCover::Impl>(z, p, false);
}

TileCover::TileCover(const Geometry<double>& geom, uint8_t z, bool project /* = true*/)
    : impl(std::make_unique<TileCover::Impl>(z, geom, project)) {}

TileCover::~TileCover() = default;

std::optional<UnwrappedTileID> TileCover::next() {
    return impl->next();
}

bool TileCover::hasNext() {
    return impl->hasNext();
}

} // namespace util
} // namespace mbgl
