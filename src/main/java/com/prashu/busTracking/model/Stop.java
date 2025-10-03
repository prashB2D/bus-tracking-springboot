package com.prashu.busTracking.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stops")
public class Stop {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long stopId;
	    
	    private String name;
	    
	    @Column(columnDefinition = "DECIMAL(10, 8)")  // ← FIXED HERE
	    private Double latitude;
	    
	    @Column(columnDefinition = "DECIMAL(11, 8)")  // ← FIXED HERE
	    private Double longitude;
	    
	    @OneToMany(mappedBy = "stop", cascade = CascadeType.ALL)
	    private List<BusStop> busStops = new ArrayList<>();
    
    public Stop() {}
    
    public Stop(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters and Setters
    public Long getStopId() { return stopId; }
    public void setStopId(Long stopId) { this.stopId = stopId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public List<BusStop> getBusStops() { return busStops; }
    public void setBusStops(List<BusStop> busStops) { this.busStops = busStops; }
}