package com.tweetapp.tweetapp.security;

import com.tweetapp.tweetapp.model.User;
import com.tweetapp.tweetapp.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of UserDetailsService for Spring Security.
 * This service loads user details from the database for authentication purposes.
 * It bridges the gap between your User model and Spring Security's UserDetails.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    // Constructor injection of UserService
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Loads user details by username for authentication.
     * This method is called by Spring Security during login and JWT validation.
     *
     * @param username the username to load
     * @return UserDetails object containing user information
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from database using UserService
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convert your User model to Spring Security's UserDetails
        // The password is already hashed in the database
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())  // Set username
                .password(user.getPassword())  // Set hashed password
                .roles("USER")  // Assign basic USER role (can be expanded for role-based access)
                .build();
    }
}