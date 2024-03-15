package dev.techmentordefensebe.chat.domain;

import dev.techmentordefensebe.chat.dto.request.ChatAddRequest;
import dev.techmentordefensebe.chat.enumtype.Difficulty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMentor {

    @Column(name = "difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "tone", nullable = false)
    private String tone;

    private ChatMentor(Difficulty difficulty, String tone) {
        this.difficulty = difficulty;
        this.tone = tone;
    }

    public static ChatMentor from(ChatAddRequest request) {
        return new ChatMentor(request.mentorDifficulty(), request.topicTech());
    }
}
