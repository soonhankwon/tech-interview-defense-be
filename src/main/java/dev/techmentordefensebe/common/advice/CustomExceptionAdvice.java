package dev.techmentordefensebe.common.advice;

import dev.techmentordefensebe.common.dto.response.ErrorResponse;
import dev.techmentordefensebe.common.enumtype.ErrorCode;
import dev.techmentordefensebe.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionAdvice {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(
                String.format(
                        "http-status={%d} code={%d} msg={%s}",
                        exception.getStatus().value(), errorCode.getCode(), errorCode.getMessage()
                )
        );
        ErrorResponse res = ErrorResponse.from(exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        ErrorResponse res = ErrorResponse.from(exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse res = ErrorResponse.from(exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
