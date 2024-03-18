package dev.techmentordefensebe.chat.dto.request;

import dev.techmentordefensebe.chat.enumtype.MentoringLevel;

public record ChatAddRequest(
        String topicTech,
        MentoringLevel mentoringLevel,
        String mentorTone,
        Boolean isDefenseMode
) {
}
