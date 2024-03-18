package dev.techmentordefensebe.chat.domain;

import dev.techmentordefensebe.chat.dto.request.ChatAddRequest;
import dev.techmentordefensebe.chat.enumtype.MentoringLevel;
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

    @Column(name = "mentoring_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private MentoringLevel mentoringLevel;

    @Column(name = "tone", nullable = false)
    private String tone;

    private ChatMentor(MentoringLevel mentoringLevel, String tone) {
        this.mentoringLevel = mentoringLevel;
        this.tone = tone;
    }

    public static ChatMentor from(ChatAddRequest request) {
        return new ChatMentor(request.mentoringLevel(), request.mentorTone());
    }
}
