package dev.techmentordefensebe.openai.dto.request;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.openai.dto.ChatCompletionMessageDTO;
import java.util.ArrayList;
import java.util.List;

public record ChatCompletionRequest(
        String model,
        List<ChatCompletionMessageDTO> messages
) {
    public static ChatCompletionRequest ofDefaultSetting(String model, Chat chat) {
        String mentorTopic = chat.getTech().getName();
        String mentorTone = chat.getChatMentor().getTone();
        String systemPrompt = "당신은 " + mentorTopic + "전문가입니다." + mentorTone + "하게 답변해주세요.";
        ChatCompletionMessageDTO system = ChatCompletionMessageDTO.of("system", systemPrompt);

        List<ChatCompletionMessageDTO> messages = new ArrayList<>();
        messages.add(system);

        return new ChatCompletionRequest(model, messages);
    }
}
