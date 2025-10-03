

package com.prashu.busTracking.controller;

import com.prashu.busTracking.model.Stop;
import com.prashu.busTracking.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // ADD THIS IMPORT
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stops")
public class StopController {
    @Autowired
    private StopRepository stopRepository;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'STUDENT')") // ADD THIS
    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // ADD THIS - Only ADMIN can create stops
    public Stop createStop(@RequestBody StopRequest request) {
        Stop stop = new Stop(request.getName(), request.getLatitude(), request.getLongitude());
        return stopRepository.save(stop);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'STUDENT')") // ADD THIS
    public ResponseEntity<?> getStop(@PathVariable Long id) {
        Optional<Stop> stop = stopRepository.findById(id);
        if (stop.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stop.get());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // ADD THIS - Only ADMIN can delete stops
    public ResponseEntity<?> deleteStop(@PathVariable Long id) {
        try {
            stopRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    public static class StopRequest {
        private String name;
        private Double latitude;
        private Double longitude;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}

