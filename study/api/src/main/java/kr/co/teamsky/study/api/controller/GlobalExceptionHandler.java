package kr.co.teamsky.study.api.controller;

import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import kr.co.teamsky.study.api.dto.response.ErrorResponse;
import kr.co.teamsky.study.app.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        var errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid",
                        (a, b) -> a));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(4000, "입력값이 올바르지 않습니다.", errors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        log.warn("비즈니스 예외 발생 - code={}, message={}", e.getErrorCode().getCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode().getCode(), e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        var errors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> {
                            var path = violation.getPropertyPath().toString();
                            int lastDot = path.lastIndexOf('.');
                            return lastDot >= 0 ? path.substring(lastDot + 1) : path;
                        },
                        violation -> violation.getMessage(),
                        (a, b) -> a));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(4000, "입력값이 올바르지 않습니다.", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("예상하지 못한 서버 오류가 발생했습니다.", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(5000, "서버 내부 오류가 발생했습니다."));
    }
}
