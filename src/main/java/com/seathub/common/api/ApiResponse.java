package com.seathub.common.api;

public record ApiResponse<T>(
        boolean success,
        T data,
        String traceId
) {

    public static <T> ApiResponse<T> success(T data, String traceId) {
        return new ApiResponse<>(true, data, traceId);
    }
}

