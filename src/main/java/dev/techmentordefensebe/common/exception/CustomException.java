package dev.techmentordefensebe.common.exception;

import dev.techmentordefensebe.common.enumtype.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;

    public CustomException(HttpStatus status, ErrorCode errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    public CustomException(Exception exception) {
        if (isCustomException(exception)) {
            CustomException customException = (CustomException) exception;
            this.status = customException.getStatus();
            this.errorCode = customException.getErrorCode();
            return;
        }
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    private boolean isCustomException(Exception exception) {
        return exception.getClass() == CustomException.class;
    }
}
