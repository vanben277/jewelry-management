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
import com.example.jewelry_management.service.FileStorageService;
import com.example.jewelry_management.service.ai.JewelryAdminAgent;
import com.example.jewelry_management.service.ai.JewelryUserAgent;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
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
    private final JewelryUserAgent jewelryAgent;
    private final ChatHistoryRepository chatHistoryRepository;
    private final AccountRepository accountRepository;
    private final FileStorageService fileStorageService;
    private final JewelryAdminAgent jewelryAdminAgent;
    private final GoogleAiGeminiChatModel geminiChatModel;
    private final ChatHistoryMapper chatHistoryMapper;

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
            aiResponse = jewelryAdminAgent.chat(message);
        } else {
            aiResponse = jewelryAgent.chat(messageWithContext);
        }
        saveHistory(message, aiResponse, null, sessionId, accountId, "TEXT");
        return aiResponse;
    }

//    @Override
//    @Transactional
//    public String chatWithImage(String message, MultipartFile image, String sessionId, Integer accountId) {
//        try {
//            String imageUrl = fileStorageService.storeImage(image, "chat_images");
//
//            // Build history context
//            List<ChatHistory> histories = chatHistoryRepository
//                    .findTop10BySessionIdOrderByCreatedAtDesc(sessionId);
//            StringBuilder contextBuilder = new StringBuilder();
//            if (histories != null && !histories.isEmpty()) {
//                List<ChatHistory> reversed = new ArrayList<>(histories);
//                Collections.reverse(reversed);
//                contextBuilder.append("[LỊCH SỬ CUỘC TRÒ CHUYỆN]\n");
//                for (ChatHistory h : reversed) {
//                    if (h.getUserQuery() != null && h.getAiResponse() != null) {
//                        contextBuilder.append("Khách: ").append(h.getUserQuery()).append("\n");
//                        contextBuilder.append("AI: ").append(h.getAiResponse()).append("\n");
//                    }
//                }
//                contextBuilder.append("[KẾT THÚC LỊCH SỬ]\n\n");
//            }
//
//            // System message
//            dev.langchain4j.data.message.SystemMessage systemMessage =
//                    dev.langchain4j.data.message.SystemMessage.from(
//                            "Bạn là trợ lý tư vấn trang sức của cửa hàng Jewelry Management. " +
//                                    "Nhiệm vụ của bạn là phân tích hình ảnh trang sức mà khách gửi lên. " +
//                                    "Hãy nhận diện đặc điểm (kiểu dáng, loại đá, chất liệu, màu sắc) và tư vấn sản phẩm tương đương trong cửa hàng. " +
//                                    "KHÔNG TỰ BỊA sản phẩm không có thật. " +
//                                    "Luôn lịch sự, chuyên nghiệp, phản hồi bằng tiếng Việt. " +
//                                    "Chỉ trả lời các câu hỏi liên quan đến trang sức. " +
//                                    "TUYỆT ĐỐI không tiết lộ thông tin nội bộ hay dữ liệu kinh doanh."
//                    );
//
//            // Build UserMessage với ảnh + context
//            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
//
//            dev.langchain4j.data.message.UserMessage userMessage =
//                    dev.langchain4j.data.message.UserMessage.from(
//                            dev.langchain4j.data.message.TextContent.from(
//                                    contextBuilder + "Khách gửi ảnh kèm lời nhắn: " +
//                                            (message != null ? message : "Hãy phân tích ảnh trang sức này")
//                            ),
//                            dev.langchain4j.data.message.ImageContent.from(base64Image, Objects.requireNonNull(image.getContentType()))
//                    );
//
//            // Gọi thẳng geminiChatModel với systemMessage + userMessage
//            var response = geminiChatModel.generate(List.of(systemMessage, userMessage));
//            String aiResponse = response.content().text();
//
//            saveHistory(
//                    message != null ? message : "[Hình ảnh]",
//                    aiResponse, imageUrl, sessionId, accountId, "IMAGE"
//            );
//            return aiResponse;
//
//        } catch (Exception e) {
//            log.error("Lỗi khi xử lý hình ảnh", e);
//            return "Xin lỗi, tôi không thể phân tích hình ảnh này. Vui lòng thử lại sau!";
//        }
//    }

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
