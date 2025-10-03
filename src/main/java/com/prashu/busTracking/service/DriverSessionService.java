package com.prashu.busTracking.service;

import com.prashu.busTracking.model.DriverSession;
import com.prashu.busTracking.model.User;
import com.prashu.busTracking.model.Bus;
import com.prashu.busTracking.repository.DriverSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DriverSessionService {

    @Autowired
    private DriverSessionRepository driverSessionRepository;

    public DriverSession startSession(User driver, Bus bus) {
        // 1. Check if driver already has an active session
        Optional<DriverSession> existingDriverSession = driverSessionRepository.findActiveSessionByDriver(driver);
        if (existingDriverSession.isPresent()) {
            throw new RuntimeException("Driver already has an active session.");
        }

        // 2. Check if the bus is already assigned to another driver
        Optional<DriverSession> existingBusSession = driverSessionRepository.findActiveSessionByBus(bus);
        if (existingBusSession.isPresent()) {
            throw new RuntimeException("Bus is already assigned to another driver.");
        }

        // 3. Create and save the new session
        DriverSession newSession = new DriverSession();
        newSession.setDriver(driver);
        newSession.setAssignedBus(bus);
        newSession.setActive(true);
        newSession.setSharingLocation(false);
        newSession.setSessionStart(LocalDateTime.now());
        
        return driverSessionRepository.save(newSession);
    }

    public void toggleLocationSharing(User driver, boolean sharingStatus) {
        // Find the driver's active session
        Optional<DriverSession> sessionOpt = driverSessionRepository.findActiveSessionByDriver(driver);
        if (sessionOpt.isEmpty()) {
            throw new RuntimeException("No active session found for this driver.");
        }

        // Update the sharing status
        DriverSession session = sessionOpt.get();
        session.setSharingLocation(sharingStatus);
        driverSessionRepository.save(session);
    }

    public void endSession(User driver) {
        // Find the driver's active session
        Optional<DriverSession> sessionOpt = driverSessionRepository.findActiveSessionByDriver(driver);
        if (sessionOpt.isPresent()) {
            DriverSession session = sessionOpt.get();
            // Mark session as inactive
            session.setActive(false);
            session.setSharingLocation(false);
            session.setSessionEnd(LocalDateTime.now());
            driverSessionRepository.save(session);
        }
    }

    public Optional<DriverSession> getActiveSessionByDriver(User driver) {
        return driverSessionRepository.findActiveSessionByDriver(driver);
    }

    public boolean isBusAssigned(Bus bus) {
        return driverSessionRepository.findActiveSessionByBus(bus).isPresent();
    }
}