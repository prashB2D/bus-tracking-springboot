package com.prashu.busTracking.controller;

import com.prashu.busTracking.model.Location;
import com.prashu.busTracking.model.WebSocketLocationDTO;
import com.prashu.busTracking.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class WebSocketLocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Handles real-time location updates from drivers via WebSocket
     */
    @MessageMapping("/location.update")
    // REMOVE THIS: @PreAuthorize("hasRole('DRIVER')")
    public void handleLocationUpdate(@Payload WebSocketLocationDTO locationDTO) {
        try {
            System.out.println("RAW WEBSOCKET MESSAGE RECEIVED: " + locationDTO.toString());
            
            if (!"location_update".equals(locationDTO.getType())) {
                System.err.println("Invalid message type: " + locationDTO.getType());
                return;
            }

            Location location = locationService.updateLocation(
                locationDTO.getBusId(),
                locationDTO.getLatitude(),
                locationDTO.getLongitude(),
                locationDTO.getSpeed() != null ? locationDTO.getSpeed() : 0.0f
            );

            System.out.println("WebSocket location update processed for bus: " + locationDTO.getBusId() +
                    ", Location: " + locationDTO.getLatitude() + ", " + locationDTO.getLongitude());

            // Broadcast update to all subscribers
            messagingTemplate.convertAndSend("/topic/locations", location);

        } catch (Exception e) {
            System.err.println("Error processing WebSocket location update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @MessageMapping("/trip.start")
    // REMOVE THIS: @PreAuthorize("hasRole('DRIVER')")
    public void handleTripStart(@Payload Map<String, Object> tripData) {
        try {
            Long busId = Long.valueOf(tripData.get("busId").toString());
            Long driverId = Long.valueOf(tripData.get("driverId").toString());

            System.out.println("Trip started - Bus: " + busId + ", Driver: " + driverId);

            // Broadcast trip start event
            messagingTemplate.convertAndSend("/topic/trips", tripData);

        } catch (Exception e) {
            System.err.println("Error processing trip start: " + e.getMessage());
        }
    }

    @MessageMapping("/trip.end")
    // REMOVE THIS: @PreAuthorize("hasRole('DRIVER')")
    public void handleTripEnd(@Payload Map<String, Object> tripData) {
        try {
            Long busId = Long.valueOf(tripData.get("busId").toString());
            Long driverId = Long.valueOf(tripData.get("driverId").toString());

            System.out.println("Trip ended - Bus: " + busId + ", Driver: " + driverId);

            // Broadcast trip end event
            messagingTemplate.convertAndSend("/topic/trips", tripData);

        } catch (Exception e) {
            System.err.println("Error processing trip end: " + e.getMessage());
        }
    }
}