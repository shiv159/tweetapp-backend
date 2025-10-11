package com.tweetapp.tweetapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter that intercepts incoming requests to validate JWT tokens.
 * This filter runs once per request and checks for a valid JWT in the Authorization header.
 * If valid, it sets the authentication in the SecurityContext for the current request.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Constructor injection of dependencies
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters each incoming request to check for JWT authentication.
     * Looks for "Authorization: Bearer <token>" header and validates the token.
     * If valid, sets the user as authenticated for this request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Get the Authorization header
        final String authHeader = request.getHeader("Authorization");

        // Check if header exists and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);  // Extract token after "Bearer "

            try {
                // Extract username from token
                String username = jwtUtil.extractUsername(token);

                // If username exists and no authentication is set yet
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Load user details from database
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Validate the token
                    if (jwtUtil.isTokenValid(token, username)) {
                        // Create authentication token with user details and authorities
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        // Set authentication in SecurityContext (makes user "logged in" for this request)
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // If token is invalid, expired, or any other issue, authentication is not set
                // Spring Security will handle unauthorized access
                logger.warn("Invalid JWT token: " + e.getMessage());
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }
}