import MapLibre
import SwiftUI
import CoreLocation

// #-example-code(CustomLocationExample)
/// Example demonstrating a custom location manager that cycles through hardcoded coordinates
struct CustomLocationExampleView: UIViewRepresentable {
    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> MLNMapView {
        let mapView = MLNMapView(frame: .zero, styleURL: OPENFREEMAP_LIBERTY_STYLE)

        // Set delegate to handle style loading
        mapView.delegate = context.coordinator

        // Create custom location manager with demo location updates
        let locationManager = CyclingLocationManager(
            locationUpdates: demoLocationUpdates,
            updateInterval: 1.0
        )

        // Set custom location manager
        mapView.locationManager = locationManager

        // Enable user location display
        mapView.showsUserLocation = true

        // Set initial camera position
        if let firstUpdate = demoLocationUpdates.first {
            mapView.setCenter(
                firstUpdate.coordinate,
                zoomLevel: 18,
                animated: false
            )
        }

        // Enable tracking mode
        mapView.userTrackingMode = .followWithCourse

        return mapView
    }

    func updateUIView(_ mapView: MLNMapView, context: Context) {}

    // MARK: - Coordinator

    class Coordinator: NSObject, MLNMapViewDelegate {
        private let routeSourceIdentifier = "routeSource"
        private let routeLayerIdentifier = "routeLayer"

        func mapView(_ mapView: MLNMapView, didFinishLoading style: MLNStyle) {
            addRoutePolyline(to: mapView)
        }

        private func addRoutePolyline(to mapView: MLNMapView) {
            guard let style = mapView.style else { return }

            // Extract coordinates from location updates
            let coordinates = demoLocationUpdates.map { $0.coordinate }

            // Create polyline from demo coordinates
            let polylineFeature = MLNPolylineFeature(coordinates: coordinates, count: UInt(coordinates.count))
            let shapeCollection = MLNShapeCollectionFeature(shapes: [polylineFeature])

            // Add source
            let source = MLNShapeSource(identifier: routeSourceIdentifier, shape: shapeCollection, options: nil)
            style.addSource(source)

            // Create line layer
            let layer = MLNLineStyleLayer(identifier: routeLayerIdentifier, source: source)

            // Style the line
            layer.lineColor = NSExpression(forConstantValue: UIColor.systemBlue)
            layer.lineWidth = NSExpression(forConstantValue: 4.0)
            layer.lineOpacity = NSExpression(forConstantValue: 0.7)
            layer.lineCap = NSExpression(forConstantValue: "round")
            layer.lineJoin = NSExpression(forConstantValue: "round")

            // Insert layer below symbol layers to keep labels on top
            for styleLayer in style.layers.reversed() {
                if !(styleLayer is MLNSymbolStyleLayer) {
                    style.insertLayer(layer, below: styleLayer)
                    return
                }
            }

            // If no non-symbol layers found, just add the layer
            style.addLayer(layer)
        }
    }
}

// MARK: - Demo Location Updates

/// Location updates with specific coordinates and bearings (Netherlands)
/// The location manager will continuously cycle through these updates
private let demoLocationUpdates: [LocationUpdate] = [
    LocationUpdate(latitude: 52.276349, longitude: 4.860968, bearing: 103),
    LocationUpdate(latitude: 52.276351, longitude: 4.860950, bearing: 105),
    LocationUpdate(latitude: 52.276347, longitude: 4.860975, bearing: 109),
    LocationUpdate(latitude: 52.276351, longitude: 4.860956, bearing: 116),
    LocationUpdate(latitude: 52.276347, longitude: 4.860976, bearing: 124),
    LocationUpdate(latitude: 52.276348, longitude: 4.860973, bearing: 134),
    LocationUpdate(latitude: 52.276345, longitude: 4.860989, bearing: 143),
    LocationUpdate(latitude: 52.276337, longitude: 4.860996, bearing: 155),
]
// #-end-example-code
