package com.example.jewelry_management.config;

import com.example.jewelry_management.service.ai.JewelryAdminAgent;
import com.example.jewelry_management.service.ai.JewelryTools;
import com.example.jewelry_management.service.ai.JewelryUserAgent;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AiConfig {
    private final JewelryTools jewelryTools;

    @Value("${GEMINI_API_KEY}")
    private String apiKey;
    @Value("${GEMINI_MODEL_NAME}")
    private String modelName;

    @Value("${GROQ_API_KEY}")
    private String groqApiKey;
    @Value("${GROQ_MODEL_NAME}")
    private String groqModelName;
    @Value("${GROQ_BASE_URL}")
    private String groqBaseUrl;

    @Bean
    public GoogleAiGeminiChatModel geminiChatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .logRequestsAndResponses(true)
                .build();
    }

    @Bean
    public OpenAiChatModel groqChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(groqApiKey)
                .baseUrl(groqBaseUrl)
                .modelName(groqModelName)
                .timeout(Duration.ofSeconds(60))
                .logRequests(true)
                .build();
    }

    @Bean("geminiUserAgent")
    public JewelryUserAgent geminiUserAgent(GoogleAiGeminiChatModel geminiChatModel) {
        return AiServices.builder(JewelryUserAgent.class)
                .chatLanguageModel(geminiChatModel)
                .chatMemoryProvider(id -> MessageWindowChatMemory.withMaxMessages(20))
                .tools(jewelryTools)
                .build();
    }

    @Bean("groqUserAgent")
    public JewelryUserAgent groqUserAgent(OpenAiChatModel groqChatModel) {
        return AiServices.builder(JewelryUserAgent.class)
                .chatLanguageModel(groqChatModel)
                .chatMemoryProvider(id -> MessageWindowChatMemory.withMaxMessages(20))
                .tools(jewelryTools)
                .build();
    }

    @Bean("geminiAdminAgent")
    public JewelryAdminAgent geminiAdminAgent(GoogleAiGeminiChatModel geminiChatModel) {
        return AiServices.builder(JewelryAdminAgent.class)
                .chatLanguageModel(geminiChatModel)
                .chatMemoryProvider(id -> MessageWindowChatMemory.withMaxMessages(20))
                .tools(jewelryTools)
                .build();
    }

    @Bean("groqAdminAgent")
    public JewelryAdminAgent groqAdminAgent(OpenAiChatModel groqChatModel) {
        return AiServices.builder(JewelryAdminAgent.class)
                .chatLanguageModel(groqChatModel)
                .chatMemoryProvider(id -> MessageWindowChatMemory.withMaxMessages(20))
                .tools(jewelryTools)
                .build();
    }
}