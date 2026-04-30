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
    private final ChatMemoryProvider chatMemoryProvider;

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

    @Value("${HF_API_KEY}")
    private String hfApiKey;
    @Value("${HF_MODEL_NAME}")
    private String hfModelName;
    @Value("${HF_BASE_URL}")
    private String hfBaseUrl;

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

    @Bean("hfChatModel")
    public OpenAiChatModel hfChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(hfApiKey)
                .baseUrl(hfBaseUrl)
                .modelName(hfModelName)
                .timeout(Duration.ofSeconds(90))
                .logRequests(true)
                .build();
    }

    @Bean("geminiUserAgent")
    public JewelryUserAgent geminiUserAgent(GoogleAiGeminiChatModel geminiChatModel) {
        return AiServices.builder(JewelryUserAgent.class)
                .chatLanguageModel(geminiChatModel)
                .chatMemoryProvider(chatMemoryProvider::getMemory)
                .tools(jewelryTools)
                .build();
    }

    @Bean("groqUserAgent")
    public JewelryUserAgent groqUserAgent(OpenAiChatModel groqChatModel) {
        return AiServices.builder(JewelryUserAgent.class)
                .chatLanguageModel(groqChatModel)
                .chatMemoryProvider(chatMemoryProvider::getMemory)
                .tools(jewelryTools)
                .build();
    }

    @Bean("hfUserAgent")
    public JewelryUserAgent hfUserAgent(OpenAiChatModel hfChatModel) {
        return AiServices.builder(JewelryUserAgent.class)
                .chatLanguageModel(hfChatModel)
                .chatMemoryProvider(chatMemoryProvider::getMemory)
                .tools(jewelryTools)
                .build();
    }

    @Bean("geminiAdminAgent")
    public JewelryAdminAgent geminiAdminAgent(GoogleAiGeminiChatModel geminiChatModel) {
        return AiServices.builder(JewelryAdminAgent.class)
                .chatLanguageModel(geminiChatModel)
                .chatMemoryProvider(chatMemoryProvider::getMemory)
                .tools(jewelryTools)
                .build();
    }

    @Bean("groqAdminAgent")
    public JewelryAdminAgent groqAdminAgent(OpenAiChatModel groqChatModel) {
        return AiServices.builder(JewelryAdminAgent.class)
                .chatLanguageModel(groqChatModel)
                .chatMemoryProvider(chatMemoryProvider::getMemory)
                .tools(jewelryTools)
                .build();
    }

    @Bean("hfAdminAgent")
    public JewelryAdminAgent hfAdminAgent(OpenAiChatModel hfChatModel) {
        return AiServices.builder(JewelryAdminAgent.class)
                .chatLanguageModel(hfChatModel)
                .chatMemoryProvider(chatMemoryProvider::getMemory)
                .tools(jewelryTools)
                .build();
    }
}