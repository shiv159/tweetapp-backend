package com.tweetapp.tweetapp.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Simple holder for information extracted from a JWT token and attached to the Spring Security context.
 */
@Getter
@ToString
@RequiredArgsConstructor
public class JwtAuthenticationDetails {

    private final String username;
    private final String userId;
}
