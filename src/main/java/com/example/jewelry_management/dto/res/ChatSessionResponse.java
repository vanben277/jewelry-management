package com.example.jewelry_management.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatSessionResponse {
    private String sessionId;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private int messageCount;
    private Integer accountId;
    private String fullName;
    private String avatar;
}