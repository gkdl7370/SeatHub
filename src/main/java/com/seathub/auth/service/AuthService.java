package com.seathub.auth.service;

import com.seathub.auth.dto.LoginRequest;
import com.seathub.auth.dto.LoginResponse;
import com.seathub.auth.dto.MeResponse;
import com.seathub.auth.dto.SignupRequest;
import com.seathub.auth.dto.SignupResponse;
import com.seathub.auth.token.JwtTokenProvider;
import com.seathub.common.api.ErrorCode;
import com.seathub.common.exception.BusinessException;
import com.seathub.user.domain.User;
import com.seathub.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.create(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name()
        );

        return SignupResponse.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_LOGIN));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_LOGIN);
        }

        return LoginResponse.bearer(
                jwtTokenProvider.createAccessToken(user),
                jwtTokenProvider.getAccessTokenExpirationSeconds()
        );
    }

    @Transactional(readOnly = true)
    public MeResponse me(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        return MeResponse.from(user);
    }
}
