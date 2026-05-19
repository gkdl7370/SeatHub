package com.seathub.auth.token;

import com.seathub.user.domain.User;
import com.seathub.user.domain.UserRole;

import java.util.Set;

public record AuthenticatedUser(
        Long id,
        String email,
        String name,
        Set<UserRole> roles
) {

    public static AuthenticatedUser from(User user) {
        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRoles()
        );
    }
}
