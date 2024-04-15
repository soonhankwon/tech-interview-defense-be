package dev.techmentordefensebe.chat.dto.request;

import dev.techmentordefensebe.chat.enumtype.MentoringLevel;
import jakarta.validation.constraints.NotBlank;

public record ChatAddRequest(
        @NotBlank(message = "기술 토픽은 null 또는 empty 일 수 없습니다.")
        String topicTech,
        MentoringLevel mentoringLevel,
        @NotBlank(message = "멘토링 톤은 null 또는 empty 일 수 없습니다.")
        String mentorTone
) {
}
