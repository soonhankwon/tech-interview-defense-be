package dev.techmentordefensebe.common.dto.response;

import dev.techmentordefensebe.common.enumtype.ErrorCode;
import dev.techmentordefensebe.common.exception.CustomException;
import org.springframework.http.ResponseEntity;

public record ErrorResponse(
        int code,
        String message
) {
    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(exception.getStatus())
                .body(
                        new ErrorResponse(
                                errorCode.getCode(),
                                errorCode.getMessage())
                );
    }

    public static ErrorResponse from(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }
}
