package com.tweetapp.tweetapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 * This class is responsible for generating, parsing, and validating JWT tokens
 * used for user authentication in the application.
 */
@Component
public class JwtUtil {

    // Secret key for signing JWT tokens - should be stored securely (e.g., in environment variables)
    @Value("${jwt.secret:default-secret-key-change-in-production}")
    private String SECRET_KEY;

    // Token expiration time in milliseconds (24 hours)
    private static final long EXPIRATION_TIME = 86400000L;
    private static final String USER_ID_CLAIM = "userId";

    // Get the secret key for HMAC signing
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Generates a JWT token for the given username.
     * The token includes the username as subject, issue time, and expiration time.
     *
     * @param username the username to include in the token
     * @return the generated JWT token as a string
     */
    public String generateToken(String username) {
        return generateToken(username, null);
    }

    /**
     * Generates a JWT token for the given username and user ID.
     * The token includes the username as subject, issue time, expiration time, and optionally the user ID claim.
     *
     * @param username the username to include in the token
     * @param userId the user ID to include as a claim (optional)
     * @return the generated JWT token as a string
     */
    public String generateToken(String username, String userId) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey());

        if (userId != null) {
            builder.claim(USER_ID_CLAIM, userId);
        }

        return builder.compact();
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username contained in the token
     * @throws ExpiredJwtException if the token is expired
     * @throws MalformedJwtException if the token is malformed
     * @throws SignatureException if the signature is invalid
     */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extracts the user ID claim from the JWT token, if present.
     *
     * @param token the JWT token
     * @return an Optional containing the user ID, or empty if not present
     */
    public Optional<String> extractUserId(String token) {
        return Optional.ofNullable(parseClaims(token).get(USER_ID_CLAIM, String.class));
    }

    /**
     * Validates if the JWT token is valid for the given username.
     * Checks if the token is not expired and the username matches.
     *
     * @param token the JWT token
     * @param username the username to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid (expired, malformed, or tampered with)
            return false;
        }
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        try {
            Date expiration = parseClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;  // Token is expired
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}