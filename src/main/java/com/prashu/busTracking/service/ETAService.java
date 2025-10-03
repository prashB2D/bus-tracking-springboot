// FILE: src/main/java/com/prashu/busTracking/service/ETAService.java
package com.prashu.busTracking.service;

import com.prashu.busTracking.dto.EtaResponseDTO;
import com.prashu.busTracking.model.Bus;
import com.prashu.busTracking.model.BusStop;
import com.prashu.busTracking.model.Location;
import com.prashu.busTracking.repository.BusStopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ETAService {
    
    @Autowired
    private BusStopRepository busStopRepository;
    
    private final Map<Long, List<Location>> locationCache = new ConcurrentHashMap<>();
    private final Map<Long, Double> speedCache = new ConcurrentHashMap<>();
    
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
    
    public List<EtaResponseDTO> calculateETAs(Location location, Bus bus) {
        List<EtaResponseDTO> etaList = new ArrayList<>();
        
        // FIXED: Use correct method name findByBusBusId instead of findByBusId
        List<BusStop> stops = busStopRepository.findByBusBusId(bus.getBusId());
        
        if (stops.isEmpty()) {
            return etaList;
        }
        
        // Calculate current speed based on recent movement
        double currentSpeed = calculateCurrentSpeed(bus.getBusId(), location);
        speedCache.put(bus.getBusId(), currentSpeed);
        
        for (BusStop busStop : stops) {
            double distance = calculateDistance(
                location.getLatitude(), 
                location.getLongitude(),
                busStop.getStop().getLatitude(),
                busStop.getStop().getLongitude()
            );
            
            int etaMinutes = (int) Math.round((distance / currentSpeed) * 60);
            double confidence = calculateConfidence(bus.getBusId(), distance);
            boolean isApproaching = distance < 2.0 && isMovingTowardStop(bus.getBusId(), busStop);
            
            EtaResponseDTO etaResponse = new EtaResponseDTO(
                bus.getBusId(),
                bus.getBusNumber(),
                busStop.getStop().getStopId(),
                busStop.getStop().getName(),
                etaMinutes,
                distance,
                confidence,
                isApproaching
            );
            
            etaList.add(etaResponse);
        }
        
        etaList.sort(Comparator.comparingInt(EtaResponseDTO::getEtaMinutes));
        return etaList;
    }
    
    private double calculateCurrentSpeed(Long busId, Location currentLocation) {
        List<Location> recentLocations = locationCache.getOrDefault(busId, new ArrayList<>());
        
        recentLocations.add(currentLocation);
        if (recentLocations.size() > 5) {
            recentLocations.remove(0);
        }
        locationCache.put(busId, recentLocations);
        
        if (recentLocations.size() < 2) {
            return currentLocation.getSpeed() != null ? currentLocation.getSpeed() : 30.0;
        }
        
        double totalDistance = 0;
        long totalTime = 0;
        
        for (int i = 1; i < recentLocations.size(); i++) {
            Location prev = recentLocations.get(i - 1);
            Location curr = recentLocations.get(i);
            
            double distance = calculateDistance(
                prev.getLatitude(), prev.getLongitude(),
                curr.getLatitude(), curr.getLongitude()
            );
            
            long timeDiff = Math.abs(java.time.Duration.between(prev.getTimestamp(), curr.getTimestamp()).getSeconds());
            
            if (timeDiff > 0) {
                totalDistance += distance;
                totalTime += timeDiff;
            }
        }
        
        if (totalTime == 0) return 30.0;
        
        double averageSpeed = (totalDistance / totalTime) * 3600;
        return Math.min(Math.max(averageSpeed, 5.0), 80.0);
    }
    
    private double calculateConfidence(Long busId, double distance) {
        double confidence = 0.8;
        
        if (distance < 1.0) confidence = 0.95;
        else if (distance < 3.0) confidence = 0.85;
        
        Double cachedSpeed = speedCache.get(busId);
        if (cachedSpeed != null) {
            List<Location> recentLocs = locationCache.getOrDefault(busId, Collections.emptyList());
            if (recentLocs.size() >= 3) {
                confidence *= 0.9 + (0.1 * (1 - calculateSpeedVariance(recentLocs)));
            }
        }
        
        return Math.min(confidence, 0.99);
    }
    
    private double calculateSpeedVariance(List<Location> locations) {
        if (locations.size() < 2) return 0.0;
        
        List<Double> speeds = new ArrayList<>();
        for (int i = 1; i < locations.size(); i++) {
            Location prev = locations.get(i - 1);
            Location curr = locations.get(i);
            
            double distance = calculateDistance(
                prev.getLatitude(), prev.getLongitude(),
                curr.getLatitude(), curr.getLongitude()
            );
            
            long timeDiff = Math.abs(java.time.Duration.between(prev.getTimestamp(), curr.getTimestamp()).getSeconds());
            if (timeDiff > 0) {
                speeds.add((distance / timeDiff) * 3600);
            }
        }
        
        if (speeds.isEmpty()) return 0.0;
        
        double average = speeds.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = speeds.stream().mapToDouble(s -> Math.pow(s - average, 2)).average().orElse(0.0);
        
        return variance / 100;
    }
    
    private boolean isMovingTowardStop(Long busId, BusStop busStop) {
        List<Location> recentLocs = locationCache.getOrDefault(busId, Collections.emptyList());
        if (recentLocs.size() < 2) return false;
        
        Location oldest = recentLocs.get(0);
        Location newest = recentLocs.get(recentLocs.size() - 1);
        
        double initialDistance = calculateDistance(
            oldest.getLatitude(), oldest.getLongitude(),
            busStop.getStop().getLatitude(), busStop.getStop().getLongitude()
        );
        
        double currentDistance = calculateDistance(
            newest.getLatitude(), newest.getLongitude(),
            busStop.getStop().getLatitude(), busStop.getStop().getLongitude()
        );
        
        return currentDistance < initialDistance;
    }
}