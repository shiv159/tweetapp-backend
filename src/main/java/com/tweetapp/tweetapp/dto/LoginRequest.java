package com.tweetapp.tweetapp.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for user login requests.
 * Contains validation annotations for login credentials.
 */
public class LoginRequest {

    @NotBlank(message = "Username is required")
    public String username;

    @NotBlank(message = "Password is required")
    public String password;

    // Default constructor for Jackson deserialization
    public LoginRequest() {}

    // Constructor for easy testing
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}