package dev.techmentordefensebe.chat.dto.response;

import dev.techmentordefensebe.chat.domain.Chat;
import dev.techmentordefensebe.tech.domain.Tech;
import dev.techmentordefensebe.user.domain.User;

public record ChatAddResponse(
        Long chatId,
        String techTopic,
        String mentoringLevel,
        String mentorTone,
        boolean isDefenseMode,
        Long userId
) {
    public static ChatAddResponse of(User user, Tech tech, Chat chat) {
        return new ChatAddResponse(
                chat.getId(),
                tech.getName(),
                chat.getChatMentor().getMentoringLevel().getValue(),
                chat.getChatMentor().getTone(),
                chat.getIsDefenseMode(),
                user.getId()
        );
    }
}
