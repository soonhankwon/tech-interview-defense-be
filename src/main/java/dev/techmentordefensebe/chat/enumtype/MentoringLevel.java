package dev.techmentordefensebe.chat.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MentoringLevel {
    BEGINNER("비기너"),
    INTERMEDIATE("미드레벨"),
    PROFESSIONAL("프로페셔널");

    private final String value;
}
