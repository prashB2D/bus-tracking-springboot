package com.prashu.busTracking.controller;

import com.prashu.busTracking.model.Bus;
import com.prashu.busTracking.model.User;
import com.prashu.busTracking.service.BusService;
import com.prashu.busTracking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BusService busService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // === USER MANAGEMENT ===
    
    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Create DRIVER account
    @PostMapping("/drivers")
    public ResponseEntity<?> createDriver(@RequestBody CreateUserRequest request) {
        try {
            if (userService.userExists(request.userId)) {
                return ResponseEntity.badRequest().body("User already exists");
            }

            String encryptedPassword = passwordEncoder.encode(request.password);
            User driver = new User(request.userId, encryptedPassword, User.Role.DRIVER);
            User savedUser = userService.save(driver);

            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Create STUDENT account
    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody CreateUserRequest request) {
        try {
            if (userService.userExists(request.userId)) {
                return ResponseEntity.badRequest().body("User already exists");
            }

            String encryptedPassword = passwordEncoder.encode(request.password);
            User student = new User(request.userId, encryptedPassword, User.Role.STUDENT);
            User savedUser = userService.save(student);

            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try {
            if (!userService.userExists(userId)) {
                return ResponseEntity.notFound().build();
            }
            
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Update user
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UpdateUserRequest request) {
        try {
            Optional<User> user = Optional.ofNullable(userService.findByUserId(userId));//%%%%%%%%%%%%%%%%%% i amde  nullable 
            if (user.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User existingUser = user.get();
            if (request.password != null && !request.password.isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(request.password));
            }
            
            User updatedUser = userService.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // === BUS MANAGEMENT ===

    // Get all buses
    @GetMapping("/buses")
    public ResponseEntity<List<Bus>> getAllBuses() {
        try {
            List<Bus> buses = busService.getAllBuses();
            return ResponseEntity.ok(buses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Get bus by ID
    @GetMapping("/buses/{busId}")
    public ResponseEntity<?> getBus(@PathVariable Long busId) {
        try {
            Optional<Bus> bus = busService.getBusById(busId);
            if (bus.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(bus.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Create BUS
    @PostMapping("/buses")
    public ResponseEntity<?> createBus(@RequestBody CreateBusRequest request) {
        try {
            Bus bus = new Bus();
            bus.setBusNumber(request.busNumber);
            bus.setColor(request.color);
            
            Bus savedBus = busService.saveBus(bus);
            return ResponseEntity.ok(savedBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Update BUS
    @PutMapping("/buses/{busId}")
    public ResponseEntity<?> updateBus(@PathVariable Long busId, @RequestBody CreateBusRequest request) {
        try {
            Optional<Bus> bus = busService.getBusById(busId);
            if (bus.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Bus existingBus = bus.get();
            existingBus.setBusNumber(request.busNumber);
            existingBus.setColor(request.color);
            
            Bus updatedBus = busService.saveBus(existingBus);
            return ResponseEntity.ok(updatedBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete BUS
    @DeleteMapping("/buses/{busId}")
    public ResponseEntity<?> deleteBus(@PathVariable Long busId) {
        try {
            Optional<Bus> bus = busService.getBusById(busId);
            if (bus.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            busService.deleteBus(busId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // === REQUEST DTO CLASSES ===

    public static class CreateUserRequest {
        public String userId;
        public String password;
        public String name;
    }

    public static class UpdateUserRequest {
        public String password;
    }

    public static class CreateBusRequest {
        public String busNumber;
        public String color;
    }
}