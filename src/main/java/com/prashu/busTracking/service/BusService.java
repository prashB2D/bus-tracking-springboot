package com.prashu.busTracking.service;

import com.prashu.busTracking.model.Bus;
import com.prashu.busTracking.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BusService {
    @Autowired
    private BusRepository busRepository;
    
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
    
    public Bus createBus(String busNumber, String color) {
        if (busRepository.existsByBusNumber(busNumber)) {
            throw new RuntimeException("Bus already exists");
        }
        
        Bus bus = new Bus(busNumber, color);
        return busRepository.save(bus);
    }
    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }
    public List<Bus> getAllBuses1() {
        return busRepository.findAll();
    }

    public void deleteBus1(Long busId) {
        busRepository.deleteById(busId);
    }
    public Optional<Bus> getBusById(Long busId) {
        return busRepository.findById(busId);
    }
    
    public Optional<Bus> getBusByNumber(String busNumber) {
        return busRepository.findByBusNumber(busNumber);
    }
    
    public void deleteBus(Long busId) {
        busRepository.deleteById(busId);
    }
}