package com.seathub.auth.dto;

import com.seathub.user.domain.User;
import com.seathub.user.domain.UserRole;

import java.util.Set;

public record SignupResponse(
        Long id,
        String email,
        String name,
        Set<UserRole> roles
) {

    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRoles()
        );
    }
}
