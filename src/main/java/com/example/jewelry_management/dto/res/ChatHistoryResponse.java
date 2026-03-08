package com.example.jewelry_management.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatHistoryResponse {
    private Long id;
    private String userQuery;
    private String aiResponse;
    private String chatType;
    private String imageUrl;
    private String sessionId;
    private LocalDateTime createdAt;
}