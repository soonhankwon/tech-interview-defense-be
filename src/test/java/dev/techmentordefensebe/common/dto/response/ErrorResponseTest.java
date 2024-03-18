package dev.techmentordefensebe.common.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import dev.techmentordefensebe.common.enumtype.ErrorCode;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void code() {
        ErrorCode errorCode = ErrorCode.NOT_EXISTS_USER;
        int errorCodeNumber = errorCode.getCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCodeNumber, errorCode.getMessage());

        assertThat(errorResponse.code()).isEqualTo(errorCodeNumber);
    }

    @Test
    void message() {
        ErrorCode errorCode = ErrorCode.NOT_EXISTS_USER;
        String errorCodeMessage = errorCode.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCodeMessage);

        assertThat(errorResponse.message()).isEqualTo(errorCodeMessage);
    }
}