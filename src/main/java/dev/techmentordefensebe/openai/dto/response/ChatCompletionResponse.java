package dev.techmentordefensebe.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.techmentordefensebe.openai.dto.ChatCompletionMessageDTO;
import java.util.List;

public record ChatCompletionResponse(
        String id,
        String object,
        Long created,
        String model,
        @JsonProperty(value = "choices")
        List<Choice> choices,
        Usage usage
) {

    private record Usage(
            @JsonProperty(value = "prompt_tokens")
            int promptTokens,
            @JsonProperty(value = "completion_tokens")
            int completionTokens,
            @JsonProperty(value = "total_tokens")
            int totalTokens
    ) {
    }

    public record Choice(
            Long index,
            ChatCompletionMessageDTO message,
            @JsonProperty(value = "finish_reason")
            String finishReason
    ) {
    }
}
