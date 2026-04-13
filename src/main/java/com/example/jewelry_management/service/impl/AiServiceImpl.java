package com.example.jewelry_management.service.impl;

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

    @Override
    @Transactional
    public String chat(String message, String sessionId, Integer accountId, String role) {
        if (message == null || message.isBlank()) {
            throw new BusinessException("Nội dung chat không được để trống", null);
        }

        List<ChatHistory> histories = chatHistoryRepository
                .findTop10BySessionIdOrderByCreatedAtDesc(sessionId);

        StringBuilder contextBuilder = new StringBuilder();
        if (histories != null && !histories.isEmpty()) {
            List<ChatHistory> reversed = new ArrayList<>(histories);
            Collections.reverse(reversed);
            contextBuilder.append("[LỊCH SỬ CUỘC TRÒ CHUYỆN]\n");
            for (ChatHistory h : reversed) {
                if (h.getUserQuery() != null && h.getAiResponse() != null) {
                    contextBuilder.append("Khách: ").append(h.getUserQuery()).append("\n");
                    contextBuilder.append("AI: ").append(h.getAiResponse()).append("\n");
                }
            }
            contextBuilder.append("[KẾT THÚC LỊCH SỬ]\n\n");
        }

        String messageWithContext = contextBuilder + "Khách: " + message;

        String aiResponse;
        if (AccountRole.ADMIN.name().equals(role)) {
            aiResponse = aiFallbackService.chatAdmin(message);
        } else {
            aiResponse = aiFallbackService.chatUser(messageWithContext);
        }

        saveHistory(message, aiResponse, null, sessionId, accountId, "TEXT");
        return aiResponse;
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
