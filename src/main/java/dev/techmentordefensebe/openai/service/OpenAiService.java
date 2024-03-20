package dev.techmentordefensebe.openai.service;

import dev.techmentordefensebe.openai.dto.request.ChatCompletionRequest;
import dev.techmentordefensebe.openai.dto.response.ChatCompletionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenAiService {

    @Value("${open-ai.api-key}")
    private String apiKey;

    @Value("${open-ai.uri}")
    private String apiUri;

    public ChatCompletionResponse getChatCompletion(ChatCompletionRequest request) {
        return WebClient.create()
                .post()
                .uri(apiUri)
                .bodyValue(request)
                .headers(header -> {
                    header.setBearerAuth(apiKey);
                    header.setContentType(MediaType.APPLICATION_JSON);
                })
                .retrieve()
                .bodyToMono(ChatCompletionResponse.class)
                .block();
    }
}
