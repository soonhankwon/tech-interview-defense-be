package dev.techmentordefensebe.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_EXISTS_USER(4000, "유저가 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(5000, "서버내부 오류입니다.");

    private final int code;
    private final String message;
}
