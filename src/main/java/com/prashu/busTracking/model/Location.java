package com.prashu.busTracking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
public class Location {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long locationId;
	    
	    @ManyToOne
	    @JoinColumn(name = "bus_id")
	    private Bus bus;
	    
	    @Column(columnDefinition = "DECIMAL(10, 8)")  // ← FIXED HERE
	    private Double latitude;
	    
	    @Column(columnDefinition = "DECIMAL(11, 8)")  // ← FIXED HERE
	    private Double longitude;
	    
	    private Float speed;
	    
	    private LocalDateTime timestamp;
    
    public Location() {
        this.timestamp = LocalDateTime.now();
    }
    
    public Location(Bus bus, Double latitude, Double longitude, Float speed) {
        this.bus = bus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    
    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Float getSpeed() { return speed; }
    public void setSpeed(Float speed) { this.speed = speed; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}