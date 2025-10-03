package com.prashu.busTracking;

import com.prashu.busTracking.model.User;
import com.prashu.busTracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BusTrackingApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BusTrackingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Create initial super admin user if it doesn't exist
        if (userRepository.findByUserId("superadmin").isEmpty()) {
            User superAdmin = new User("superadmin", 
                                    passwordEncoder.encode("admin123"), 
                                    User.Role.ADMIN);
            userRepository.save(superAdmin);
            System.out.println("==========================================");
            System.out.println("🚀 INITIAL SUPER ADMIN USER CREATED!");
            System.out.println("👉 Username: superadmin");
            System.out.println("👉 Password: admin123");
            System.out.println("👉 Role: ADMIN");
            System.out.println("==========================================");
        }
    }
}