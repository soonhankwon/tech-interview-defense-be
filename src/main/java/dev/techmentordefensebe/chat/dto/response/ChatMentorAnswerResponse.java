package dev.techmentordefensebe.chat.dto.response;

import dev.techmentordefensebe.openai.dto.ChatCompletionMessageDTO;
import dev.techmentordefensebe.openai.dto.response.ChatCompletionResponse;
import dev.techmentordefensebe.openai.dto.response.ChatCompletionResponse.Choice;
import java.time.LocalDateTime;

public record ChatMentorAnswerResponse(
        String answer,
        LocalDateTime createdAt
) {
    public static ChatMentorAnswerResponse from(ChatCompletionResponse response) {
        Choice choice = response.choices().getFirst();
        ChatCompletionMessageDTO dto = choice.message();

        return new ChatMentorAnswerResponse(
                dto.content(),
                LocalDateTime.now()
        );
    }
}
