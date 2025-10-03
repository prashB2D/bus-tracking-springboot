

package com.prashu.busTracking.controller;

import com.prashu.busTracking.model.Bus;
import com.prashu.busTracking.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // ADD THIS IMPORT
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/buses")
public class BusController {
    @Autowired
    private BusService busService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'STUDENT')") // ADD THIS
    public List<Bus> getAllBuses() {
        return busService.getAllBuses();
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // ADD THIS - Only ADMIN can create buses
    public ResponseEntity<?> createBus(@RequestBody BusRequest request) {
        try {
            Bus bus = busService.createBus(request.getBusNumber(), request.getColor());
            return ResponseEntity.ok(bus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'STUDENT')") // ADD THIS
    public ResponseEntity<?> getBus(@PathVariable Long id) {
        Optional<Bus> bus = busService.getBusById(id);
        if (bus.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bus.get());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // ADD THIS - Only ADMIN can delete buses
    public ResponseEntity<?> deleteBus(@PathVariable Long id) {
        try {
            busService.deleteBus(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    public static class BusRequest {
        private String busNumber;
        private String color;
        
        public String getBusNumber() { return busNumber; }
        public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
    }
}

