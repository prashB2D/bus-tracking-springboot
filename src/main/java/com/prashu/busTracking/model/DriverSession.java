package com.prashu.busTracking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "driver_sessions")
public class DriverSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    // Correct mapping to your User entity. 'user_id' is the PK column in the 'users' table.
    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "user_id")
    private User driver;

    // Correct mapping to your Bus entity.
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus assignedBus;

    private boolean isActive;
    private boolean isSharingLocation;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;

    // Constructors
    public DriverSession() {
    }

    public DriverSession(User driver, Bus assignedBus) {
        this.driver = driver;
        this.assignedBus = assignedBus;
        this.isActive = true;
        this.isSharingLocation = false;
        this.sessionStart = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getSessionId() {
        return sessionId;
    }
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    public User getDriver() {
        return driver;
    }
    public void setDriver(User driver) {
        this.driver = driver;
    }
    public Bus getAssignedBus() {
        return assignedBus;
    }
    public void setAssignedBus(Bus assignedBus) {
        this.assignedBus = assignedBus;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public boolean isSharingLocation() {
        return isSharingLocation;
    }
    public void setSharingLocation(boolean sharingLocation) {
        isSharingLocation = sharingLocation;
    }
    public LocalDateTime getSessionStart() {
        return sessionStart;
    }
    public void setSessionStart(LocalDateTime sessionStart) {
        this.sessionStart = sessionStart;
    }
    public LocalDateTime getSessionEnd() {
        return sessionEnd;
    }
    public void setSessionEnd(LocalDateTime sessionEnd) {
        this.sessionEnd = sessionEnd;
    }
}