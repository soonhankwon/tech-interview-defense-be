package dev.techmentordefensebe.common.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import dev.techmentordefensebe.common.enumtype.ErrorCode;
import dev.techmentordefensebe.common.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ErrorResponseTest {

    @Test
    void toResponseEntity() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.NOT_EXISTS_USER;
        CustomException customException = new CustomException(badRequest, errorCode);

        ResponseEntity<ErrorResponse> responseEntity = ErrorResponse.toResponseEntity(customException);

        assertThat(responseEntity.getStatusCode()).isSameAs(badRequest);
    }

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