package com.example.jewelry_management.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@Slf4j
public class AiFallbackService {
    private final JewelryUserAgent geminiUserAgent;
    private final JewelryUserAgent groqUserAgent;
    private final JewelryUserAgent hfUserAgent;

    private final JewelryAdminAgent geminiAdminAgent;
    private final JewelryAdminAgent groqAdminAgent;
    private final JewelryAdminAgent hfAdminAgent;
    
    private final int maxRetries;
    private final Duration retryDelay;

    public AiFallbackService(
            @Qualifier("geminiUserAgent") JewelryUserAgent geminiUserAgent,
            @Qualifier("groqUserAgent") JewelryUserAgent groqUserAgent,
            @Qualifier("hfUserAgent") JewelryUserAgent hfUserAgent,
            @Qualifier("geminiAdminAgent") JewelryAdminAgent geminiAdminAgent,
            @Qualifier("groqAdminAgent") JewelryAdminAgent groqAdminAgent,
            @Qualifier("hfAdminAgent") JewelryAdminAgent hfAdminAgent,
            @Value("${ai.chat.max-retries:2}") int maxRetries) {
        this.geminiUserAgent = geminiUserAgent;
        this.groqUserAgent = groqUserAgent;
        this.hfUserAgent = hfUserAgent;
        this.geminiAdminAgent = geminiAdminAgent;
        this.groqAdminAgent = groqAdminAgent;
        this.hfAdminAgent = hfAdminAgent;
        this.maxRetries = maxRetries;
        this.retryDelay = Duration.ofSeconds(1);
    }

    public String chatUser(String message) {
        return tryAllProvidersWithFallback(
                message,
                "USER",
                () -> geminiUserAgent.chat(message),
                () -> groqUserAgent.chat(message),
                () -> hfUserAgent.chat(message),
                "{\"type\":\"error\",\"message\":\"Xin lỗi, hệ thống AI đang tạm thời gián đoạn. Vui lòng thử lại sau hoặc liên hệ nhân viên cửa hàng để được hỗ trợ.\"}"
        );
    }

    public String chatAdmin(String message) {
        return tryAllProvidersWithFallback(
                message,
                "ADMIN",
                () -> geminiAdminAgent.chat(message),
                () -> groqAdminAgent.chat(message),
                () -> hfAdminAgent.chat(message),
                "{\"type\":\"error\",\"message\":\"Hệ thống AI phân tích đang tạm thời không khả dụng. Vui lòng thử lại sau.\"}"
        );
    }

    /**
     * Try all providers with fallback - Refactored to avoid code duplication
     */
    private String tryAllProvidersWithFallback(
            String message,
            String agentType,
            Supplier<String> geminiProvider,
            Supplier<String> groqProvider,
            Supplier<String> hfProvider,
            String fallbackMessage
    ) {
        // Try Gemini
        String result = tryProviderWithRetry(geminiProvider, "Gemini", maxRetries);
        if (result != null) return result;

        // Try Groq
        result = tryProviderWithRetry(groqProvider, "Groq", maxRetries);
        if (result != null) return result;

        // Try HuggingFace
        result = tryProviderWithRetry(hfProvider, "HuggingFace", maxRetries);
        if (result != null) return result;

        // All providers failed
        log.error("[AI-{}] All AI providers failed for message: {}", agentType, 
                message.length() > 100 ? message.substring(0, 100) + "..." : message);
        return fallbackMessage;
    }

    /**
     * Phase 3 (P2): Retry logic with exponential backoff
     * 
     * Strategy:
     * - Rate limit errors: Skip to next provider immediately (no retry)
     * - Timeout errors: Retry with exponential backoff (2s, 4s, 8s)
     * - Other errors: Fail immediately and skip to next provider
     */
    private String tryProviderWithRetry(
            Supplier<String> provider,
            String providerName,
            int maxRetries
    ) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.info("[AI-{}] Trying {} (attempt {}/{})", 
                        Thread.currentThread().getName(), providerName, attempt, maxRetries);
                return provider.get();
            } catch (Exception e) {
                if (isRateLimitError(e)) {
                    // Rate limit: Skip to next provider immediately
                    // Retrying the same provider won't help when rate limited
                    String waitTime = extractWaitTime(e.getMessage());
                    log.warn("[AI-{}] {} rate limited{}. Skipping to next provider.", 
                            Thread.currentThread().getName(), 
                            providerName,
                            waitTime != null ? " (need to wait " + waitTime + ")" : "");
                    break; // Exit retry loop, move to next provider
                    
                } else if (isTimeoutError(e)) {
                    // Timeout: Retry with exponential backoff
                    int delaySeconds = (int) Math.pow(2, attempt); // 2s, 4s, 8s
                    log.warn("[AI-{}] {} timeout (attempt {}/{}), retrying after {}s...", 
                            Thread.currentThread().getName(), providerName, attempt, maxRetries, delaySeconds);
                    if (attempt < maxRetries) {
                        sleep(Duration.ofSeconds(delaySeconds));
                        continue;
                    }
                    
                } else {
                    // Other errors: Log and skip to next provider
                    log.error("[AI-{}] {} failed with error: ", 
                            Thread.currentThread().getName(), providerName, e);
                    break; // Exit retry loop, move to next provider
                }
            }
        }
        return null;
    }
    
    /**
     * Extract wait time from rate limit error message
     * Example: "Please try again in 19.42s" -> "19.42s"
     */
    private String extractWaitTime(String errorMessage) {
        if (errorMessage == null) return null;
        
        // Pattern: "try again in 19.42s" or "try again in 1m 30s"
        int tryAgainIndex = errorMessage.indexOf("try again in");
        if (tryAgainIndex != -1) {
            int startIndex = tryAgainIndex + "try again in".length();
            int endIndex = errorMessage.indexOf(".", startIndex + 10); // Find next sentence
            if (endIndex == -1) endIndex = errorMessage.length();
            
            String waitPart = errorMessage.substring(startIndex, endIndex).trim();
            // Extract just the time part (e.g., "19.42s")
            String[] words = waitPart.split("\\s+");
            if (words.length > 0) {
                return words[0]; // Return first word which should be the time
            }
        }
        return null;
    }

    private boolean isRateLimitError(Exception e) {
        String message = e.getMessage();
        return message != null && (
                message.contains("rate limit") ||
                message.contains("429") ||
                message.contains("quota exceeded") ||
                message.contains("too many requests")
        );
    }

    private boolean isTimeoutError(Exception e) {
        String message = e.getMessage();
        return message != null && (
                message.contains("timeout") ||
                message.contains("timed out") ||
                e instanceof java.net.SocketTimeoutException
        );
    }

    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Sleep interrupted", e);
        }
    }
}