package com.prashu.busTracking.service;

import com.prashu.busTracking.model.User;
import com.prashu.busTracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Check if user exists - uses your repository's existsByUserId
    public boolean userExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    // Save user - uses JpaRepository's save method
    public User save(User user) {
        return userRepository.save(user);
    }

    // Find user by userId - returns User object (not Optional) to match your controller code
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
    // THE CRITICAL FIX: This method loads user details for Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + username));

        // Convert the user's role from enum to Spring Security GrantedAuthority
        // Example: User.Role.DRIVER -> "ROLE_DRIVER"
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        List<GrantedAuthority> authorities = Arrays.asList(authority);

        // Return Spring Security User object with authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),       // username
                user.getPassword(),     // password (encoded)
                authorities             // list of roles/authorities
        );
    }
}