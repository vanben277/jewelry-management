package com.example.jewelry_management.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiFallbackService {
    private final JewelryUserAgent geminiUserAgent;
    private final JewelryUserAgent groqUserAgent;
    private final JewelryUserAgent hfUserAgent;

    private final JewelryAdminAgent geminiAdminAgent;
    private final JewelryAdminAgent groqAdminAgent;
    private final JewelryAdminAgent hfAdminAgent;

    public AiFallbackService(
            @Qualifier("geminiUserAgent") JewelryUserAgent geminiUserAgent,
            @Qualifier("groqUserAgent") JewelryUserAgent groqUserAgent,
            @Qualifier("hfUserAgent") JewelryUserAgent hfUserAgent,
            @Qualifier("geminiAdminAgent") JewelryAdminAgent geminiAdminAgent,
            @Qualifier("groqAdminAgent") JewelryAdminAgent groqAdminAgent,
            @Qualifier("hfAdminAgent") JewelryAdminAgent hfAdminAgent) {
        this.geminiUserAgent = geminiUserAgent;
        this.groqUserAgent = groqUserAgent;
        this.hfUserAgent = hfUserAgent;
        this.geminiAdminAgent = geminiAdminAgent;
        this.groqAdminAgent = groqAdminAgent;
        this.hfAdminAgent = hfAdminAgent;
    }

    public String chatUser(String message) {
        try {
            log.info("[AI-USER] Trying Gemini...");
            return geminiUserAgent.chat(message);
        } catch (Exception e) {
            log.warn("[AI-USER] Gemini failed: {}", e.getMessage());
        }

        try {
            log.info("[AI-USER] Trying Groq...");
            return groqUserAgent.chat(message);
        } catch (Exception e) {
            log.warn("[AI-USER] Groq failed: {}", e.getMessage());
        }

        try {
            log.info("[AI-USER] Trying HuggingFace...");
            return hfUserAgent.chat(message);
        } catch (Exception e) {
            log.error("[AI-USER] All AI providers failed!", e);
        }

        return "Xin lỗi, hệ thống AI đang tạm thời gián đoạn. Vui lòng thử lại sau hoặc liên hệ nhân viên cửa hàng để được hỗ trợ.";
    }

    public String chatAdmin(String message) {
        try {
            log.info("[AI-ADMIN] Trying Gemini...");
            return geminiAdminAgent.chat(message);
        } catch (Exception e) {
            log.warn("[AI-ADMIN] Gemini failed: {}", e.getMessage());
        }

        try {
            log.info("[AI-ADMIN] Trying Groq...");
            return groqAdminAgent.chat(message);
        } catch (Exception e) {
            log.warn("[AI-ADMIN] Groq failed: {}", e.getMessage());
        }

        try {
            log.info("[AI-ADMIN] Trying HuggingFace...");
            return hfAdminAgent.chat(message);
        } catch (Exception e) {
            log.error("[AI-ADMIN] All AI providers failed!", e);
        }

        return "Hệ thống AI phân tích đang tạm thời không khả dụng. Vui lòng thử lại sau.";
    }
}