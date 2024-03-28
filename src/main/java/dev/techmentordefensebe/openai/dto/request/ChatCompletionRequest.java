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
    public static ChatCompletionRequest ofDefaultSetting(String model, Chat chat, boolean isDefenseMode) {
        String mentorTopic = chat.getTech().getName();
        String mentorTone = chat.getChatMentor().getTone();
        // isDefenseMode 여부에 따라 프롬프트가 많이 달라짐
        String systemPrompt = PromptGenerator.getMentorSystemPrompt(mentorTopic, mentorTone, isDefenseMode);

        ChatCompletionMessageDTO system = ChatCompletionMessageDTO.of("system", systemPrompt);
        List<ChatCompletionMessageDTO> messages = new ArrayList<>();
        messages.add(system);

        return new ChatCompletionRequest(model, messages);
    }
}
