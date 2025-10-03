package com.prashu.busTracking.model;

import java.util.Map;

public class WebSocketLocationDTO {
    private String type;
    private Long busId;
    private String driverId;
    private Double latitude;
    private Double longitude;
    private Float speed;
    private Double heading;
    private Double accuracy;
    private String timestamp;
    
    // Default constructor (REQUIRED for Spring)
    public WebSocketLocationDTO() {
    }
    
    // Constructor from Map (for backward compatibility)
    public WebSocketLocationDTO(Map<String, Object> map) {
        this.type = (String) map.get("type");
        this.busId = map.get("busId") != null ? Long.valueOf(map.get("busId").toString()) : null;
        this.driverId = (String) map.get("driverId");
        this.latitude = map.get("latitude") != null ? Double.valueOf(map.get("latitude").toString()) : null;
        this.longitude = map.get("longitude") != null ? Double.valueOf(map.get("longitude").toString()) : null;
        this.speed = map.get("speed") != null ? Float.valueOf(map.get("speed").toString()) : 0.0f;
        this.heading = map.get("heading") != null ? Double.valueOf(map.get("heading").toString()) : 0.0;
        this.accuracy = map.get("accuracy") != null ? Double.valueOf(map.get("accuracy").toString()) : 0.0;
        this.timestamp = (String) map.get("timestamp");
    }
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getBusId() {
        return busId;
    }
    
    public void setBusId(Long busId) {
        this.busId = busId;
    }
    
    public String getDriverId() {
        return driverId;
    }
    
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public Float getSpeed() {
        return speed;
    }
    
    public void setSpeed(Float speed) {
        this.speed = speed;
    }
    
    public Double getHeading() {
        return heading;
    }
    
    public void setHeading(Double heading) {
        this.heading = heading;
    }
    
    public Double getAccuracy() {
        return accuracy;
    }
    
    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "WebSocketLocationDTO{" +
                "type='" + type + '\'' +
                ", busId=" + busId +
                ", driverId='" + driverId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", heading=" + heading +
                ", accuracy=" + accuracy +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}