package com.tweetapp.tweetapp.controller;

import com.tweetapp.tweetapp.dto.ApiResponse;
import com.tweetapp.tweetapp.dto.LoginRequest;
import com.tweetapp.tweetapp.dto.RegisterRequest;
import com.tweetapp.tweetapp.security.JwtUtil;
import com.tweetapp.tweetapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related operations.
 * Provides endpoints for user registration and login, which return JWT tokens.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthenticationManager authManager;  // For authenticating login credentials
    private final JwtUtil jwtUtil;  // For generating JWT tokens
    private final UserService userService;  // For user management

    /**
     * Registers a new user with detailed profile information.
     * Uses @RequestBody for better security and structure.
     *
     * @param request the registration data in JSON format
     * @return success response or error if username exists
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("Registering user: {}", request.username);
            userService.createUser(request.username, request.password, request.email,
                                 request.firstName, request.lastName, request.dateOfBirth);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
        } catch (IllegalArgumentException e) {
            log.warn("Registration failed (username exists): {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("USERNAME_EXISTS", e.getMessage()));
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("REGISTRATION_FAILED", "Failed to register user"));
        }
    }

    /**
     * Authenticates a user with username and password.
     * Uses @RequestBody for consistency and security.
     *
     * @param request the login credentials in JSON format
     * @return JWT token on success, error on failure
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("Login attempt for user: {}", request.username);
            // Authenticate the user credentials (throws exception if invalid)
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));

            // Fetch the authenticated user to include additional claims
            String token = userService.getUserByUsername(request.username)
                    .map(user -> jwtUtil.generateToken(user.getUsername(), user.getUserId()))
                    .orElseThrow(() -> new IllegalStateException("Authenticated user record not found"));

            log.info("Login successful for user: {}", request.username);
            return ResponseEntity.ok(ApiResponse.success(token, "Login successful"));
        } catch (Exception e) {
            // Authentication failed (wrong credentials, user not found, etc.)
            log.warn("Login failed for user {}: {}", request.username, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("INVALID_CREDENTIALS", "Invalid username or password"));
        }
    }
}