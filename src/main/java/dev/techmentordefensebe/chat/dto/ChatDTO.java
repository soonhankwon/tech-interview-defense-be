package dev.techmentordefensebe.chat.dto;

import dev.techmentordefensebe.chat.domain.Chat;
import java.time.LocalDateTime;

public record ChatDTO(
        Long chatId,
        String mentorTopic,
        LocalDateTime createdAt
) {
    public static ChatDTO from(Chat chat) {
        return new ChatDTO(
                chat.getId(),
                chat.getTech().getName(),
                chat.getCreatedAt()
        );
    }
}
