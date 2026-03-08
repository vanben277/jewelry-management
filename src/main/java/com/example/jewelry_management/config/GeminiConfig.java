package com.example.jewelry_management.config;

import com.example.jewelry_management.service.ai.DatabaseChatMemoryStore;
import com.example.jewelry_management.service.ai.JewelryAdminAgent;
import com.example.jewelry_management.service.ai.JewelryTools;
import com.example.jewelry_management.service.ai.JewelryUserAgent;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GeminiConfig {
    private final DatabaseChatMemoryStore databaseChatMemoryStore;
    private final JewelryTools jewelryTools;
    @Value("${GEMINI_API_KEY}")
    private String apiKey;
    @Value("${GEMINI_MODEL_NAME}")
    private String modelName;

    @Bean
    public GoogleAiGeminiChatModel geminiChatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .logRequestsAndResponses(true)
                .build();
    }

    @Bean
    public JewelryUserAgent jewelryAgent(GoogleAiGeminiChatModel geminiChatModel) {
        return AiServices.builder(JewelryUserAgent.class)
                .chatLanguageModel(geminiChatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(jewelryTools)
                .build();
    }

    @Bean
    public JewelryAdminAgent jewelryAdminAgent(GoogleAiGeminiChatModel geminiChatModel) {
        return AiServices.builder(JewelryAdminAgent.class)
                .chatLanguageModel(geminiChatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(jewelryTools)
                .build();
    }
}