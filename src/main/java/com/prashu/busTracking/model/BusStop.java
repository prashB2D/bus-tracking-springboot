package com.prashu.busTracking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bus_stops")
public class BusStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
    
    @ManyToOne
    @JoinColumn(name = "stop_id")
    private Stop stop;
    
    private Integer stopOrder;
    
    public BusStop() {}
    
    public BusStop(Bus bus, Stop stop, Integer stopOrder) {
        this.bus = bus;
        this.stop = stop;
        this.stopOrder = stopOrder;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }
    
    public Stop getStop() { return stop; }
    public void setStop(Stop stop) { this.stop = stop; }
    
    public Integer getStopOrder() { return stopOrder; }
    public void setStopOrder(Integer stopOrder) { this.stopOrder = stopOrder; }
}