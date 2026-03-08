package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.res.ChatHistoryResponse;
import com.example.jewelry_management.dto.res.ChatSessionResponse;

import java.util.List;

public interface AiService {
    String chat(String message, String sessionId, Integer accountId, String role);

//    String chatWithImage(String message, MultipartFile image, String sessionId, Integer accountId);

    List<ChatHistoryResponse> getChatHistory(Integer accountId, String sessionId);

    List<ChatSessionResponse> getChatSessions(Integer accountId);

    List<ChatSessionResponse> getAdminChatSessions();
}
