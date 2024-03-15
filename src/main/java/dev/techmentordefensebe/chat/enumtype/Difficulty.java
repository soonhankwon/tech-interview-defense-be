package dev.techmentordefensebe.chat.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Difficulty {
    EASY("쉬움"),
    INTERMEDIATE("중간"),
    HARD("어려움"),
    EXTREME("매우어려움");

    private final String value;
}
