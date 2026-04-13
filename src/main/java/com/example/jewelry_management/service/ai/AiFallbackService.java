package com.example.jewelry_management.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiFallbackService {

    private final JewelryUserAgent geminiUserAgent;
    private final JewelryUserAgent groqUserAgent;
    private final JewelryAdminAgent geminiAdminAgent;
    private final JewelryAdminAgent groqAdminAgent;

    public AiFallbackService(
            @Qualifier("geminiUserAgent") JewelryUserAgent geminiUserAgent,
            @Qualifier("groqUserAgent") JewelryUserAgent groqUserAgent,
            @Qualifier("geminiAdminAgent") JewelryAdminAgent geminiAdminAgent,
            @Qualifier("groqAdminAgent") JewelryAdminAgent groqAdminAgent) {
        this.geminiUserAgent = geminiUserAgent;
        this.groqUserAgent = groqUserAgent;
        this.geminiAdminAgent = geminiAdminAgent;
        this.groqAdminAgent = groqAdminAgent;
    }

    public String chatUser(String message) {
        try {
            log.info("[AI] Trying Gemini for user chat...");
            return geminiUserAgent.chat(message);
        } catch (Exception e) {
            log.warn("[AI] Gemini failed ({}), falling back to Groq...", e.getMessage());
            return groqUserAgent.chat(message);
        }
    }

    public String chatAdmin(String message) {
        try {
            log.info("[AI] Trying Gemini for admin chat...");
            return geminiAdminAgent.chat(message);
        } catch (Exception e) {
            log.warn("[AI] Gemini failed ({}), falling back to Groq...", e.getMessage());
            return groqAdminAgent.chat(message);
        }
    }
}