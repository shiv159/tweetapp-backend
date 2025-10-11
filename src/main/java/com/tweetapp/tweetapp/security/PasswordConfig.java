package com.tweetapp.tweetapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for password encoding.
 * This separates password encoding from security configuration to avoid circular dependencies.
 */
@Configuration
public class PasswordConfig {

    /**
     * Provides the password encoder bean for hashing passwords.
     * Uses BCrypt for secure password storage.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Strong hashing algorithm for passwords
    }
}