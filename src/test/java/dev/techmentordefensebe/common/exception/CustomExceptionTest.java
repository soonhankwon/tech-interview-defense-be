package dev.techmentordefensebe.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import dev.techmentordefensebe.common.enumtype.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CustomExceptionTest {

    @Test
    void getStatus() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.NOT_EXISTS_USER;

        CustomException customException = new CustomException(badRequest, errorCode);
        assertThat(customException.getStatus()).isSameAs(badRequest);
    }

    @Test
    void getErrorCode() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.NOT_EXISTS_USER;

        CustomException customException = new CustomException(badRequest, errorCode);
        assertThat(customException.getErrorCode()).isSameAs(errorCode);
    }
}