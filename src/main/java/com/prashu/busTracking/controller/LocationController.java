package com.prashu.busTracking.controller;

import com.prashu.busTracking.model.Location;
import com.prashu.busTracking.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<?> updateLocation(@RequestBody LocationRequest request) {
        try {
            Location location = locationService.updateLocation(
                request.getBusId(), 
                request.getLatitude(), 
                request.getLongitude(), 
                request.getSpeed()
            );
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/bus/{busId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'STUDENT')")
    public List<Location> getBusLocations(@PathVariable Long busId) {
        return locationService.getBusLocations(busId);
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'STUDENT')")
    public List<Location> getRecentLocations() {
        return locationService.getRecentLocations();
    }
    
    @GetMapping("/latest/{busId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'STUDENT')")
    public ResponseEntity<?> getLatestLocation(@PathVariable Long busId) {
        Optional<Location> location = locationService.getLatestLocation(busId);
        if (location.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location.get());
    }
    
    public static class LocationRequest {
        private Long busId;
        private Double latitude;
        private Double longitude;
        private Float speed;
        
        // Getters and setters
        public Long getBusId() { return busId; }
        public void setBusId(Long busId) { this.busId = busId; }
        
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        
        public Float getSpeed() { return speed; }
        public void setSpeed(Float speed) { this.speed = speed; }
    }
}