package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.ai.AiResponseDto;
import com.example.jewelry_management.dto.res.ChatHistoryResponse;
import com.example.jewelry_management.dto.res.ChatSessionResponse;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.mapper.ChatHistoryMapper;
import com.example.jewelry_management.model.Account;
import com.example.jewelry_management.model.ChatHistory;
import com.example.jewelry_management.repository.AccountRepository;
import com.example.jewelry_management.repository.ChatHistoryRepository;
import com.example.jewelry_management.service.AiService;
import com.example.jewelry_management.service.ai.AiFallbackService;
import com.example.jewelry_management.service.ai.AiResponseParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiServiceImpl implements AiService {
    private final ChatHistoryRepository chatHistoryRepository;
    private final AccountRepository accountRepository;
    private final ChatHistoryMapper chatHistoryMapper;
    private final AiFallbackService aiFallbackService;
    private final AiResponseParser aiResponseParser;

    @Override
    @Transactional
    public String chat(String message, String sessionId, Integer accountId, String role) {
        if (message == null || message.isBlank()) {
            throw new BusinessException("Nội dung chat không được để trống", null);
        }

        // Log accountId for debugging
        log.info("[AI-CHAT] Session: {}, AccountId: {}, Role: {}", sessionId, accountId, role);

        String messageWithAccountId = "[ACCOUNT_ID: " + accountId + "]\n" + message;

        String aiRawResponse;
        if (AccountRole.ADMIN.name().equals(role)) {
            aiRawResponse = aiFallbackService.chatAdmin(messageWithAccountId);
        } else {
            aiRawResponse = aiFallbackService.chatUser(messageWithAccountId);
        }

        // Parse AI response to structured JSON
        AiResponseDto parsedResponse = aiResponseParser.parse(aiRawResponse);
        String jsonResponse = aiResponseParser.toJson(parsedResponse);

        saveHistory(message, jsonResponse, null, sessionId, accountId, "TEXT");
        return jsonResponse;
    }

    @Override
    public List<ChatHistoryResponse> getChatHistory(Integer accountId, String sessionId) {
        if (sessionId != null && !sessionId.isBlank()) {
            return chatHistoryMapper.toResponseList(
                    chatHistoryRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)
            );
        }
        if (accountId != null) {
            return chatHistoryMapper.toResponseList(
                    chatHistoryRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
            );
        }
        return new ArrayList<>();
    }

    @Override
    public List<ChatSessionResponse> getChatSessions(Integer accountId) {
        if (accountId == null) return new ArrayList<>();
        return chatHistoryMapper.toSessionResponseList(
                chatHistoryRepository.findSessionSummaryByAccountId(accountId)
        );
    }

    @Override
    public List<ChatSessionResponse> getAdminChatSessions() {
        return chatHistoryMapper.toAdminSessionResponseList(
                chatHistoryRepository.findAllSessionSummaryForAdmin()
        );
    }

    private void saveHistory(
            String query,
            String response,
            String imageUrl,
            String sessionId,
            Integer accountId,
            String type
    ) {
        Account account = null;
        if (accountId != null) {
            account = accountRepository.findById(accountId).orElse(null);
        }

        ChatHistory history = ChatHistory.builder()
                .account(account)
                .sessionId(sessionId)
                .chatType(type)
                .userQuery(query != null ? query : "[Hình ảnh]")
                .aiResponse(response)
                .imageUrl(imageUrl)
                .tokensUsed(0)
                .build();

        chatHistoryRepository.save(history);
    }
}
