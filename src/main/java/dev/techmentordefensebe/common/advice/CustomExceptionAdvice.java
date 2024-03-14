package dev.techmentordefensebe.common.advice;

import dev.techmentordefensebe.common.dto.response.ErrorResponse;
import dev.techmentordefensebe.common.enumtype.ErrorCode;
import dev.techmentordefensebe.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionAdvice {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(
                String.format(
                        "http-status={%d} code={%d} msg={%s}",
                        exception.getStatus().value(), errorCode.getCode(), errorCode.getMessage()
                )
        );
        return ErrorResponse.toResponseEntity(exception);
    }
}
