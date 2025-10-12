package com.tweetapp.tweetapp.config;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Adds a correlation ID to MDC for each request, so logs can be tied together.
 * Propagates an incoming X-Correlation-Id/X-Request-Id if present; otherwise generates a UUID.
 * Also adds the correlation ID to the response header.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String ALT_REQUEST_ID_HEADER = "X-Request-Id";
    public static final String MDC_CORRELATION_ID = "correlationId";
    public static final String MDC_HTTP_METHOD = "httpMethod";
    public static final String MDC_HTTP_PATH = "httpPath";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId = Optional.ofNullable(request.getHeader(CORRELATION_ID_HEADER))
                .or(() -> Optional.ofNullable(request.getHeader(ALT_REQUEST_ID_HEADER)))
                .orElseGet(() -> UUID.randomUUID().toString());

        // Populate MDC for logging
        MDC.put(MDC_CORRELATION_ID, correlationId);
        MDC.put(MDC_HTTP_METHOD, request.getMethod());
        MDC.put(MDC_HTTP_PATH, request.getRequestURI());

        // Expose header for client-side correlation
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clean up MDC to avoid leaking between requests/threads
            MDC.remove(MDC_CORRELATION_ID);
            MDC.remove(MDC_HTTP_METHOD);
            MDC.remove(MDC_HTTP_PATH);
            MDC.remove("username");
            MDC.remove("userId");
        }
    }
}
