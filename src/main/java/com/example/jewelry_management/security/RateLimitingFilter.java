package com.example.jewelry_management.security;

import com.example.jewelry_management.config.RateLimitConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final RateLimitConfig rateLimitConfig;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        // Nếu rate limiting bị tắt, bỏ qua
        if (!rateLimitConfig.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        // Rate limit cho sensitive endpoints
        if (isSensitiveEndpoint(path)) {
            String key = getClientIP(request) + ":" + path;
            Bucket bucket = resolveBucket(key);

            if (bucket.tryConsume(1)) {
                log.debug("Rate limit check passed for IP: {} on path: {}", getClientIP(request), path);
                filterChain.doFilter(request, response);
            } else {
                log.warn("Rate limit exceeded for IP: {} on path: {}", getClientIP(request), path);
                response.setStatus(429); // Too Many Requests
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"message\":\"Quá nhiều yêu cầu. Vui lòng thử lại sau.\",\"data\":null,\"errorCode\":\"RATE_LIMIT_EXCEEDED\"}");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isSensitiveEndpoint(String path) {
        return path.matches(".*/login") ||
                path.matches(".*/forgot-password") ||
                path.matches(".*/reset-password");
    }

    private Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> {
            // Sử dụng config từ application.yml
            Bandwidth limit = Bandwidth.classic(
                    rateLimitConfig.getCapacity(),
                    Refill.intervally(
                            rateLimitConfig.getCapacity(),
                            Duration.ofMinutes(rateLimitConfig.getRefillMinutes())
                    )
            );
            return Bucket.builder().addLimit(limit).build();
        });
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        // X-Forwarded-For có thể chứa nhiều IP, lấy IP đầu tiên
        return xfHeader.split(",")[0].trim();
    }
}
