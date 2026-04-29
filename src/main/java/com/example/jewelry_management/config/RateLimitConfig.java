package com.example.jewelry_management.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.rate-limit")
@Getter
@Setter
public class RateLimitConfig {

    /**
     * Số lượng request tối đa cho sensitive endpoints
     */
    private int capacity = 5;

    /**
     * Thời gian refill (phút)
     */
    private int refillMinutes = 1;

    /**
     * Bật/tắt rate limiting
     */
    private boolean enabled = true;
}
