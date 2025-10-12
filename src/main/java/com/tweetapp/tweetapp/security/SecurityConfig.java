package com.tweetapp.tweetapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security.
 * This sets up JWT-based authentication, disables CSRF for stateless API,
 * and configures which endpoints require authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    // Constructor injection of the JWT filter
    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures the security filter chain.
     * - Disables CSRF since we're using JWT (stateless)
     * - Allows public access to authentication endpoints (/api/auth/**)
     * - Requires authentication for all other requests
     * - Adds the JWT filter before the username/password filter
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // Disable CSRF for stateless JWT authentication
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()  // Allow login and register without auth
                .anyRequest().authenticated()  // All other requests need authentication
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter
        return http.build();
    }

    /**
     * Provides the AuthenticationManager bean for handling authentication.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}