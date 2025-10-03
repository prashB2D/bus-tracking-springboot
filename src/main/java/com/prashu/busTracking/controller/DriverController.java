package com.prashu.busTracking.controller;

import com.prashu.busTracking.model.Bus;
import com.prashu.busTracking.model.DriverSession;
import com.prashu.busTracking.model.Location;
import com.prashu.busTracking.model.User;
import com.prashu.busTracking.service.DriverSessionService;
import com.prashu.busTracking.service.UserService;
import com.prashu.busTracking.service.BusService;
import com.prashu.busTracking.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/driver")
@PreAuthorize("hasRole('DRIVER')")
public class DriverController {

    @Autowired
    private DriverSessionService driverSessionService;

    @Autowired
    private BusService busService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService; // ADDED

    // Endpoint to start a new driving session
    @PostMapping("/session/start")
    public ResponseEntity<?> startSession(@RequestParam Long busId, Authentication authentication) {
        try {
            String username = authentication.getName();
            User driver = userService.findByUserId(username);
            if (driver == null) {
                return ResponseEntity.badRequest().body("Error: Driver not found.");
            }
            
            Optional<Bus> bus = busService.getBusById(busId);
            if (bus.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Bus not found.");
            }
            
            DriverSession session = driverSessionService.startSession(driver, bus.get());
            return ResponseEntity.ok(session);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Endpoint to share location - FIXED TO SAVE TO DATABASE
    @PostMapping("/location")
    public ResponseEntity<?> shareLocation(@RequestBody LocationRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            User driver = userService.findByUserId(username);
            
            if (driver == null) {
                return ResponseEntity.badRequest().body("Error: Driver not found.");
            }
            
            // Get the driver's active session to find which bus they're driving
            Optional<DriverSession> activeSession = driverSessionService.getActiveSessionByDriver(driver);
            if (activeSession.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: No active session. Start a session first.");
            }
            
            DriverSession session = activeSession.get();
            Bus currentBus = session.getAssignedBus(); // CORRECTED: getAssignedBus()
            Long busId = currentBus.getBusId(); // Get the ID from the Bus object
            
            // ACTUALLY SAVE THE LOCATION TO DATABASE
            Location savedLocation = locationService.updateLocation(
                busId, 
                request.getLatitude(), 
                request.getLongitude(), 
                request.getSpeed().floatValue() // Convert Double to Float
            );
            
            return ResponseEntity.ok(savedLocation);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Endpoint to start location sharing
    @PostMapping("/location/start")
    public ResponseEntity<?> startSharing(Authentication authentication) {
        try {
            String username = authentication.getName();
            User driver = userService.findByUserId(username);
            
            if (driver == null) {
                return ResponseEntity.badRequest().body("Error: Driver not found.");
            }
            
            driverSessionService.toggleLocationSharing(driver, true);
            return ResponseEntity.ok("Location sharing started successfully.");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Endpoint to stop location sharing
    @PostMapping("/location/stop")
    public ResponseEntity<?> stopSharing(Authentication authentication) {
        try {
            String username = authentication.getName();
            User driver = userService.findByUserId(username);
            
            if (driver == null) {
                return ResponseEntity.badRequest().body("Error: Driver not found.");
            }
            
            driverSessionService.toggleLocationSharing(driver, false);
            return ResponseEntity.ok("Location sharing stopped successfully.");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Endpoint to end the current session
    @PostMapping("/session/end")
    public ResponseEntity<?> endSession(Authentication authentication) {
        try {
            String username = authentication.getName();
            User driver = userService.findByUserId(username);
            
            if (driver == null) {
                return ResponseEntity.badRequest().body("Error: Driver not found.");
            }
            
            driverSessionService.endSession(driver);
            return ResponseEntity.ok("Session ended successfully.");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Endpoint to get current active session details
    @GetMapping("/session")
    public ResponseEntity<?> getMySession(Authentication authentication) {
        try {
            String username = authentication.getName();
            User driver = userService.findByUserId(username);
            
            if (driver == null) {
                return ResponseEntity.badRequest().body("Error: Driver not found.");
            }
            
            Optional<DriverSession> session = driverSessionService.getActiveSessionByDriver(driver);
            if (session.isPresent()) {
                return ResponseEntity.ok(session.get());
            } else {
                return ResponseEntity.ok("No active session found.");
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Request DTO for location sharing
    public static class LocationRequest {
        private Double latitude;
        private Double longitude;
        private Double speed;
        
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        
        public Double getSpeed() { return speed; }
        public void setSpeed(Double speed) { this.speed = speed; }
    }
}