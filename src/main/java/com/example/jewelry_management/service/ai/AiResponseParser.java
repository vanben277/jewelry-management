package com.example.jewelry_management.service.ai;

import com.example.jewelry_management.dto.ai.AiResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Parser để xử lý AI response và convert sang structured JSON
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AiResponseParser {
    private final ObjectMapper objectMapper;
    
    /**
     * Parse AI response string thành AiResponseDto
     * Nếu AI trả về JSON hợp lệ → parse trực tiếp
     * Nếu AI trả về text → wrap vào format JSON
     */
    public AiResponseDto parse(String aiResponse) {
        if (aiResponse == null || aiResponse.isBlank()) {
            return createErrorResponse("AI không có phản hồi");
        }
        
        // Try parse as JSON first
        if (aiResponse.trim().startsWith("{")) {
            try {
                return objectMapper.readValue(aiResponse, AiResponseDto.class);
            } catch (JsonProcessingException e) {
                log.warn("AI response is not valid JSON, wrapping as text: {}", e.getMessage());
            }
        }
        
        // Fallback: wrap as text response
        return AiResponseDto.builder()
                .type("text")
                .message(aiResponse)
                .build();
    }
    
    /**
     * Convert AiResponseDto thành JSON string
     */
    public String toJson(AiResponseDto response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize AI response to JSON", e);
            return "{\"type\":\"error\",\"message\":\"Lỗi xử lý phản hồi\"}";
        }
    }
    
    /**
     * Tạo error response
     */
    public AiResponseDto createErrorResponse(String errorMessage) {
        return AiResponseDto.builder()
                .type("error")
                .message(errorMessage)
                .build();
    }
    
    /**
     * Tạo text response
     */
    public AiResponseDto createTextResponse(String message) {
        return AiResponseDto.builder()
                .type("text")
                .message(message)
                .build();
    }
}
