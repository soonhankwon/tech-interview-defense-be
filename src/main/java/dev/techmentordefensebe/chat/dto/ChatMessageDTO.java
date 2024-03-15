package dev.techmentordefensebe.chat.dto;

import dev.techmentordefensebe.chat.domain.ChatMessage;
import java.time.LocalDateTime;

public record ChatMessageDTO(
        String content,
        boolean isUserMessage,
        LocalDateTime createdAt
) {
    public static ChatMessageDTO from(ChatMessage chatMessage) {
        return new ChatMessageDTO(
                chatMessage.getContent(),
                chatMessage.getIsUserMessage(),
                chatMessage.getCreatedAt()
        );
    }
}
