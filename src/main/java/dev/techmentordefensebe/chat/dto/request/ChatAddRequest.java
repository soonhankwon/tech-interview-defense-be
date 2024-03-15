package dev.techmentordefensebe.chat.dto.request;

import dev.techmentordefensebe.chat.enumtype.Difficulty;

public record ChatAddRequest(
        String topicTech,
        Difficulty mentorDifficulty,
        String mentorTone
) {
}
