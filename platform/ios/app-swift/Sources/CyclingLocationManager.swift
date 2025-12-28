import MapLibre
import CoreLocation

/// A location update containing coordinate and bearing information
struct LocationUpdate {
    let coordinate: CLLocationCoordinate2D
    let bearing: CLLocationDirection

    init(latitude: CLLocationDegrees, longitude: CLLocationDegrees, bearing: CLLocationDirection) {
        self.coordinate = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        self.bearing = bearing
    }
}

/// A custom location manager that cycles through a predefined set of coordinates
/// at a fixed time interval, providing simulated location updates for testing and demonstration.
class CyclingLocationManager: NSObject, MLNLocationManager {
    // MARK: - MLNLocationManager Required Properties

    var delegate: (any MLNLocationManagerDelegate)?
    var authorizationStatus: CLAuthorizationStatus = .authorizedAlways
    var headingOrientation: CLDeviceOrientation = .portrait

    // MARK: - Configuration Properties

    /// Time interval between location updates (in seconds)
    var updateInterval: TimeInterval

    /// Array of location updates to cycle through
    var locationUpdates: [LocationUpdate]

    // MARK: - Internal State

    private var currentIndex = 0
    private var isRunning = false

    // MARK: - Initialization

    /// Initialize a cycling location manager with location updates and update interval
    /// - Parameters:
    ///   - locationUpdates: Array of location updates to cycle through
    ///   - updateInterval: Time between updates in seconds (default: 2.0)
    init(locationUpdates: [LocationUpdate], updateInterval: TimeInterval = 2.0) {
        self.locationUpdates = locationUpdates
        self.updateInterval = updateInterval
        super.init()
    }

    // MARK: - MLNLocationManager Required Methods

    func requestAlwaysAuthorization() {}
    func requestWhenInUseAuthorization() {}
    func dismissHeadingCalibrationDisplay() {}
    func startUpdatingHeading() {}
    func stopUpdatingHeading() {}

    func startUpdatingLocation() {
        guard !locationUpdates.isEmpty else { return }
        isRunning = true
        scheduleNextUpdate()
    }

    func stopUpdatingLocation() {
        isRunning = false
        NSObject.cancelPreviousPerformRequests(
            withTarget: self,
            selector: #selector(update),
            object: nil
        )
    }

    // MARK: - Private Implementation

    private func scheduleNextUpdate() {
        guard isRunning else { return }
        perform(#selector(update), with: nil, afterDelay: updateInterval)
    }

    @objc private func update() {
        guard isRunning else { return }

        let currentUpdate = locationUpdates[currentIndex]
        let nextIndex = (currentIndex + 1) % locationUpdates.count

        // Create location with provided bearing
        let location = CLLocation(
            coordinate: currentUpdate.coordinate,
            altitude: 0.0,
            horizontalAccuracy: 10.0,  // Good GPS accuracy
            verticalAccuracy: 10.0,
            course: currentUpdate.bearing,
            speed: 5.0,  // ~18 km/h walking speed
            timestamp: Date()
        )

        // Notify delegate
        delegate?.locationManager(self, didUpdate: [location])

        // Move to next coordinate (cycle back to start if at end)
        currentIndex = nextIndex

        // Schedule next update
        scheduleNextUpdate()
    }
}
