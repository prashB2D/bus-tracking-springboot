package com.prashu.busTracking.repository;

import com.prashu.busTracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);
    
    // save() method is already provided by JpaRepository
    // No changes needed here
}