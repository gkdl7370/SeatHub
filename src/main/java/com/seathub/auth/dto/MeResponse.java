package com.seathub.auth.dto;

import com.seathub.user.domain.User;
import com.seathub.user.domain.UserRole;

import java.util.Set;

public record MeResponse(
        Long id,
        String email,
        String name,
        Set<UserRole> roles
) {

    public static MeResponse from(User user) {
        return new MeResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRoles()
        );
    }
}
