package dev.techmentordefensebe.openai.dto.request;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.openai.dto.ChatCompletionMessageDTO;
import dev.techmentordefensebe.openai.util.PromptGenerator;
import java.util.ArrayList;
import java.util.List;

public record ChatCompletionRequest(
        String model,
        List<ChatCompletionMessageDTO> messages
) {
    public static ChatCompletionRequest ofDefaultSetting(String model, Chat chat, Boolean isQuestionGenerated) {
        String mentorTopic = chat.getTech().getName();
        String mentorTone = chat.getChatMentor().getTone();

        String systemPrompt = PromptGenerator.getMentorSystemPrompt(mentorTopic, mentorTone, isQuestionGenerated);

        ChatCompletionMessageDTO system = ChatCompletionMessageDTO.of("system", systemPrompt);
        List<ChatCompletionMessageDTO> messages = new ArrayList<>();
        messages.add(system);

        return new ChatCompletionRequest(model, messages);
    }
}
