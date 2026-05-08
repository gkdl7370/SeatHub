package com.seathub.common.exception;

import com.seathub.common.api.ErrorCode;
import com.seathub.common.api.ErrorResponse;
import com.seathub.common.trace.TraceIdHolder;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, exception.getMessage(), TraceIdHolder.get()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST, message, TraceIdHolder.get()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST, exception.getMessage(), TraceIdHolder.get()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, TraceIdHolder.get()));
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}

