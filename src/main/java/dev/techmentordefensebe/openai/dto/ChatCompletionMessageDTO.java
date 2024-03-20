package dev.techmentordefensebe.openai.dto;

public record ChatCompletionMessageDTO(
        String role,
        String content
) {
    public static ChatCompletionMessageDTO of(String role, String content) {
        return new ChatCompletionMessageDTO(role, content);
    }
}
