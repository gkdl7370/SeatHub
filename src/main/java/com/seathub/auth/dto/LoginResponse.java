package com.seathub.auth.dto;

public record LoginResponse(
        String tokenType,
        String accessToken,
        long expiresIn
) {

    public static LoginResponse bearer(String accessToken, long expiresIn) {
        return new LoginResponse("Bearer", accessToken, expiresIn);
    }
}
