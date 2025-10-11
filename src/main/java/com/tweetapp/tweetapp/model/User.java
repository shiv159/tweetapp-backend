package com.tweetapp.tweetapp.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Container(containerName = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @org.springframework.data.annotation.Id
    @GeneratedValue
    private String id;

    @PartitionKey
    private String username;

    // Password field for JWT authentication - stored as hashed value for security
    private String password;

    // Additional user profile fields
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;  // Store as String in YYYY-MM-DD format

    // Constructor for creating user with username only (used in existing code)
    public User(String username) {
        this.username = username;
    }

    // Constructor for creating user with username and password (for registration)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor for full user registration with all details
    public User(String username, String password, String email, String firstName, String lastName, String dateOfBirth) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
}
