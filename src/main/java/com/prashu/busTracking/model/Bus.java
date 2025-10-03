package com.prashu.busTracking.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // ← ADD THIS IMPORT
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buses")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Long busId;
    
    @Column(name = "bus_number", unique = true, length = 10)
    private String busNumber;
    
    private String color;
    
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    @JsonIgnore // ← ADD THIS ANNOTATION (CRITICAL FIX)
    private List<BusStop> busStops = new ArrayList<>();
    
    public Bus() {}
    
    public Bus(String busNumber, String color) {
        this.busNumber = busNumber;
        this.color = color;
    }
    
    // Getters and Setters
    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }
    
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public List<BusStop> getBusStops() { return busStops; }
    public void setBusStops(List<BusStop> busStops) { this.busStops = busStops; }
}