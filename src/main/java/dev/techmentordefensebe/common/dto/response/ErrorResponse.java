package dev.techmentordefensebe.common.dto.response;

import dev.techmentordefensebe.common.enumtype.ErrorCode;
import dev.techmentordefensebe.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int code,
        String message
) {
    public static ErrorResponse from(Exception exception) {
        ErrorCode errorCode;
        if (exception instanceof CustomException customException) {
            errorCode = customException.getErrorCode();
            return new ErrorResponse(
                    errorCode.getCode(),
                    errorCode.getMessage()
            );
        }
        if (exception instanceof RuntimeException runtimeException) {
            return new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    runtimeException.getMessage()
            );
        }
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage()
        );
    }
}
