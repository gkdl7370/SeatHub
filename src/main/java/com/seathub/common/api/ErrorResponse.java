package com.seathub.common.api;

public record ErrorResponse(
        boolean success,
        String code,
        String message,
        String traceId
) {

    public static ErrorResponse of(ErrorCode errorCode, String traceId) {
        return new ErrorResponse(false, errorCode.name(), errorCode.getMessage(), traceId);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, String traceId) {
        return new ErrorResponse(false, errorCode.name(), message, traceId);
    }
}

