package com.prashu.busTracking.controller;

import com.prashu.busTracking.model.User;
import com.prashu.busTracking.service.UserService;
import com.prashu.busTracking.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			User user = userService.findByUserId(request.getUserId());
			if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
				// UPDATED: Pass both user ID and role to generateToken
				String token = jwtUtils.generateToken(user.getUserId(), user.getRole().name());

				Map<String, Object> response = new HashMap<>();
				response.put("token", token);
				response.put("userId", user.getUserId());
				response.put("role", user.getRole().name());

				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.badRequest().body("Invalid credentials");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid credentials");
		}
	}

	// ... keep the rest of the AuthController code unchanged ...
	// getCurrentUser() and register() methods remain the same

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
		try {
			// Extract token from "Bearer <token>"
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);

				// Validate token
				if (jwtUtils.validateToken(token)) {
					String userId = jwtUtils.getUserIdFromToken(token);
					User user = userService.findByUserId(userId);

					if (user != null) {
						Map<String, Object> response = new HashMap<>();
						response.put("userId", user.getUserId());
						response.put("role", user.getRole().name());
						return ResponseEntity.ok(response);
					}
				}
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: " + e.getMessage());
		}
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			// Check if user already exists
			if (userService.userExists(request.getUserId())) {
				return ResponseEntity.badRequest().body("User already exists");
			}

			// Encrypt password and determine role
			String encryptedPassword = passwordEncoder.encode(request.getPassword());
			User.Role userRole = User.Role.valueOf(request.getRole().toUpperCase()); // FIXED: toUpperCase()

			// Create user with correct constructor
			User user = new User(request.getUserId(), encryptedPassword, userRole); // FIXED: Correct constructor

			// Save user
			User savedUser = userService.save(user);

			return ResponseEntity.ok(savedUser);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
	}

	// Request DTO classes
	public static class LoginRequest {
		private String userId;
		private String password;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	public static class RegisterRequest {
		private String userId;
		private String password;
		private String role;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
	}
}