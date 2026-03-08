package com.example.jewelry_management.mapper;

import com.example.jewelry_management.dto.res.ChatHistoryResponse;
import com.example.jewelry_management.dto.res.ChatSessionResponse;
import com.example.jewelry_management.model.ChatHistory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatHistoryMapper {

    public ChatHistoryResponse toResponse(ChatHistory history) {
        if (history == null) return null;
        return ChatHistoryResponse.builder()
                .id(history.getId())
                .userQuery(history.getUserQuery())
                .aiResponse(history.getAiResponse())
                .chatType(history.getChatType())
                .imageUrl(history.getImageUrl())
                .sessionId(history.getSessionId())
                .createdAt(history.getCreatedAt())
                .build();
    }

    public List<ChatHistoryResponse> toResponseList(List<ChatHistory> histories) {
        if (histories == null) return List.of();
        return histories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ChatSessionResponse toSessionResponse(Object[] row) {
        return ChatSessionResponse.builder()
                .sessionId((String) row[0])
                .lastMessage((String) row[1])
                .lastMessageAt(((java.sql.Timestamp) row[2]).toLocalDateTime())
                .messageCount(((Long) row[3]).intValue())
                .build();
    }

    public List<ChatSessionResponse> toSessionResponseList(List<Object[]> rows) {
        if (rows == null) return List.of();
        return rows.stream()
                .map(this::toSessionResponse)
                .collect(Collectors.toList());
    }

    public ChatSessionResponse toAdminSessionResponse(Object[] row) {
        return ChatSessionResponse.builder()
                .sessionId((String) row[0])
                .lastMessage((String) row[1])
                .lastMessageAt(((java.sql.Timestamp) row[2]).toLocalDateTime())
                .messageCount(((Long) row[3]).intValue())
                .accountId(((Number) row[4]).intValue())
                .fullName((String) row[5])
                .avatar((String) row[6])
                .build();
    }

    public List<ChatSessionResponse> toAdminSessionResponseList(List<Object[]> rows) {
        if (rows == null) return List.of();
        return rows.stream()
                .map(this::toAdminSessionResponse)
                .collect(Collectors.toList());
    }
}