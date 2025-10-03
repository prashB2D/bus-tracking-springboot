package com.prashu.busTracking.service;

import com.prashu.busTracking.dto.EtaResponseDTO;
import com.prashu.busTracking.model.Bus;
import com.prashu.busTracking.model.Location;
import com.prashu.busTracking.repository.BusRepository;
import com.prashu.busTracking.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private BusRepository busRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private ETAService etaService;
    
    public Location updateLocation(Long busId, Double latitude, Double longitude, Float speed) {
        Optional<Bus> bus = busRepository.findById(busId);
        if (bus.isEmpty()) {
            throw new RuntimeException("Bus not found");
        }
        
        Location location = new Location(bus.get(), latitude, longitude, speed);
        Location savedLocation = locationRepository.save(location);
        
        // Broadcast to WebSocket subscribers
        broadcastLocation(savedLocation);
        
        return savedLocation;
    }
    
    private void broadcastLocation(Location location) {
        try {
            // Broadcast raw location (for mapping)
            messagingTemplate.convertAndSend("/topic/locations", location);
            
            // Broadcast to specific bus
            messagingTemplate.convertAndSend("/topic/bus-" + location.getBus().getBusId() + "-locations", location);
            
            // Calculate and broadcast optimized ETAs
            List<EtaResponseDTO> etaResponses = etaService.calculateETAs(location, location.getBus());
            messagingTemplate.convertAndSend("/topic/eta", etaResponses);
            
            // Broadcast confidence level
            Map<String, Object> confidenceData = new HashMap<>();
            confidenceData.put("busId", location.getBus().getBusId());
            confidenceData.put("confidence", calculateOverallConfidence(etaResponses));
            messagingTemplate.convertAndSend("/topic/confidence", confidenceData);
            
        } catch (Exception e) {
            System.err.println("WebSocket broadcast error: " + e.getMessage());
        }
    }
    
    private double calculateOverallConfidence(List<EtaResponseDTO> etaResponses) {
        if (etaResponses.isEmpty()) return 0.0;
        return etaResponses.stream()
                .mapToDouble(EtaResponseDTO::getConfidence)
                .average()
                .orElse(0.0);
    }
    
    public List<Location> getBusLocations(Long busId) {
        return locationRepository.findByBus_BusIdOrderByTimestampDesc(busId);
    }
    
    public List<Location> getRecentLocations() {
        return locationRepository.findTop10ByOrderByTimestampDesc();
    }
    
    public Optional<Location> getLatestLocation(Long busId) {
        return locationRepository.findLatestByBusId(busId);
    }
}