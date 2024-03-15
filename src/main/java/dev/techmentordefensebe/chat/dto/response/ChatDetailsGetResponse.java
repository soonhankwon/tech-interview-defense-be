package dev.techmentordefensebe.chat.dto.response;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.chat.dto.ChatMessageDTO;
import java.util.List;

public record ChatDetailsGetResponse(
        Long chatId,
        Long userId,
        List<ChatMessageDTO> chatMessages

) {
    public static ChatDetailsGetResponse of(Chat chat, List<ChatMessageDTO> chatMessages) {
        return new ChatDetailsGetResponse(
                chat.getId(),
                chat.getUser().getId(),
                chatMessages
        );
    }
}
