package com.example.jewelry_management.controller;

import com.example.jewelry_management.dto.ApiResponse;
import com.example.jewelry_management.dto.res.ChatHistoryResponse;
import com.example.jewelry_management.dto.res.ChatSessionResponse;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.model.Account;
import com.example.jewelry_management.service.AiService;
import com.example.jewelry_management.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/ai")
public class AiController {
    private final AiService aiService;
    private final AccountValidator accountValidator;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse> chat(@RequestParam String message, @RequestParam String sessionId) {
        Integer accountId = getCurrentAccountId();
        String role = getCurrentRole();
        String response = aiService.chat(message, sessionId, accountId, role);
        return ResponseEntity.ok(new ApiResponse("Thành công", response));
    }

//    @PostMapping(value = "/chat-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse> chatWithImage(
//            @RequestParam(required = false) String message,
//            @RequestPart("image") MultipartFile image,
//            @RequestParam String sessionId
//    ) {
//        Integer accountId = getCurrentAccountId();
//        String response = aiService.chatWithImage(message, image, sessionId, accountId);
//        return ResponseEntity.ok(new ApiResponse("Phân tích hình ảnh thành công", response));
//    }

    @GetMapping("/history/sessions")
    public ResponseEntity<ApiResponse> getSessions() {
        Integer accountId = getCurrentAccountId();
        List<ChatSessionResponse> sessions = aiService.getChatSessions(accountId);
        return ResponseEntity.ok(new ApiResponse("Thành công", sessions));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getHistory(
            @RequestParam(required = false) String sessionId
    ) {
        Integer accountId = getCurrentAccountId();
        List<ChatHistoryResponse> histories = aiService.getChatHistory(accountId, sessionId);
        return ResponseEntity.ok(new ApiResponse("Lấy lịch sử thành công", histories));
    }

    @PostMapping("/admin/analytics")
    public ResponseEntity<ApiResponse> adminAnalytics(
            @RequestParam String query,
            @RequestParam String sessionId
    ) {
        Account currentAccount = accountValidator.getCurrentAccount();
        if (!currentAccount.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Bạn không có quyền truy cập", null));
        }
        String response = aiService.chat("[ADMIN ANALYTICS MODE]: " + query, sessionId, currentAccount.getId(), AccountRole.ADMIN.name());
        return ResponseEntity.ok(new ApiResponse("Phân tích dữ liệu thành công", response));
    }

    @GetMapping("/admin/history/sessions")
    public ResponseEntity<ApiResponse> getAdminSessions() {
        List<ChatSessionResponse> sessions = aiService.getAdminChatSessions();
        return ResponseEntity.ok(new ApiResponse("Thành công", sessions));
    }

    private Integer getCurrentAccountId() {
        try {
            return accountValidator.getCurrentAccount().getId();
        } catch (Exception e) {
            return null;
        }
    }

    private String getCurrentRole() {
        try {
            return accountValidator.getCurrentAccount().getRole().name();
        } catch (Exception e) {
            return AccountRole.USER.name();
        }
    }
}
