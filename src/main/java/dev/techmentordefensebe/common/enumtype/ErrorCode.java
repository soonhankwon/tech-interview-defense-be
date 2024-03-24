package dev.techmentordefensebe.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(5000, "서버내부 오류입니다."),
    NOT_EXISTS_USER(4000, "유저가 존재하지 않습니다."),
    NOT_EXISTS_USER_EMAIL(4001, "유저 이메일이 존재하지 않습니다."),
    LOGIN_REQUIRED(4002, "로그인이 필요합니다."),
    NOT_EXISTS_TECH_NAME(4003, "기술 이름이 존재하지 않습니다."),
    NOT_EXISTS_CHAT_ID(4004, "채팅 ID가 존재하지 않습니다."),
    EXISTS_DUPLICATED_TECH_NAME(4005, "중복된 기술 이름이 존재합니다."),
    NOT_EXISTS_USER_ID(4006, "유저 ID가 존재하지 않습니다."),
    NOT_EXISTS_CHAT_ID_WITH_USER(4007, "유저 ID의 채팅 ID가 존재하지 않습니다.");

    private final int code;
    private final String message;
}
