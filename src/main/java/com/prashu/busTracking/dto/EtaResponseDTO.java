// FILE: src/main/java/com/prashu/busTracking/dto/EtaResponseDTO.java
//DTO (Data Transfer Object):
package com.prashu.busTracking.dto;

public class EtaResponseDTO {
    private Long busId;
    private String busNumber;
    private Long stopId;
    private String stopName;
    private int etaMinutes;
    private double distanceKm;
    private double confidence; // Confidence level in prediction (0-1)
    private boolean isApproaching; // If bus is approaching this stop

    public EtaResponseDTO() {}

    public EtaResponseDTO(Long busId, String busNumber, Long stopId, String stopName, 
                         int etaMinutes, double distanceKm, double confidence, boolean isApproaching) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.stopId = stopId;
        this.stopName = stopName;
        this.etaMinutes = etaMinutes;
        this.distanceKm = distanceKm;
        this.confidence = confidence;
        this.isApproaching = isApproaching;
    }

    // Getters and setters
    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public Long getStopId() { return stopId; }
    public void setStopId(Long stopId) { this.stopId = stopId; }

    public String getStopName() { return stopName; }
    public void setStopName(String stopName) { this.stopName = stopName; }

    public int getEtaMinutes() { return etaMinutes; }
    public void setEtaMinutes(int etaMinutes) { this.etaMinutes = etaMinutes; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public boolean isApproaching() { return isApproaching; }
    public void setApproaching(boolean approaching) { isApproaching = approaching; }
}