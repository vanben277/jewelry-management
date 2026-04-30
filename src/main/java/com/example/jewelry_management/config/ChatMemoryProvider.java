package com.example.jewelry_management.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * TTL-based Chat Memory Provider
 * Giải quyết memory leak bằng cách:
 * 1. Giới hạn số lượng sessions tối đa (default: 1000)
 * 2. Tự động xóa sessions không hoạt động sau 30 phút
 * 3. Giảm số messages lưu từ 20 → 10
 */
@Component
@Slf4j
public class ChatMemoryProvider {
    
    private final Cache<Object, ChatMemory> memoryCache;
    private final int maxMessages;
    
    public ChatMemoryProvider(
            @Value("${ai.chat.max-messages:10}") int maxMessages,
            @Value("${ai.chat.cache.max-size:1000}") int maxCacheSize,
            @Value("${ai.chat.cache.ttl-minutes:30}") int ttlMinutes
    ) {
        this.maxMessages = maxMessages;
        
        this.memoryCache = Caffeine.newBuilder()
                .maximumSize(maxCacheSize)
                .expireAfterAccess(Duration.ofMinutes(ttlMinutes))
                .removalListener((key, value, cause) -> {
                    log.debug("Chat memory removed for session: {} (cause: {})", key, cause);
                })
                .build();
        
        log.info("ChatMemoryProvider initialized: maxMessages={}, maxCacheSize={}, ttlMinutes={}", 
                maxMessages, maxCacheSize, ttlMinutes);
    }
    
    /**
     * Get or create chat memory for a session
     */
    public ChatMemory getMemory(Object sessionId) {
        return memoryCache.get(sessionId, id -> {
            log.debug("Creating new chat memory for session: {}", id);
            return MessageWindowChatMemory.withMaxMessages(maxMessages);
        });
    }
    
    /**
     * Clear memory for a specific session
     */
    public void clearMemory(Object sessionId) {
        memoryCache.invalidate(sessionId);
        log.info("Chat memory cleared for session: {}", sessionId);
    }
    
    /**
     * Clear all memories (for admin/maintenance)
     */
    public void clearAllMemories() {
        memoryCache.invalidateAll();
        log.info("All chat memories cleared");
    }
    
    /**
     * Get cache statistics
     */
    public String getCacheStats() {
        var stats = memoryCache.stats();
        return String.format(
                "Cache Stats - Size: %d, Hits: %d, Misses: %d, Evictions: %d",
                memoryCache.estimatedSize(),
                stats.hitCount(),
                stats.missCount(),
                stats.evictionCount()
        );
    }
}
