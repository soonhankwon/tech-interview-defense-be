package dev.techmentordefensebe.chat.dto.response;

import dev.techmentordefensebe.chat.domain.Chat;
import java.time.LocalDateTime;

public record ChatGetResponse(
        Long chatId,
        String mentorTopic,
        LocalDateTime createdAt

) {
    public static ChatGetResponse from(Chat chat) {
        return new ChatGetResponse(
                chat.getId(),
                chat.getTech().getName(),
                chat.getCreatedAt()
        );
    }
}
