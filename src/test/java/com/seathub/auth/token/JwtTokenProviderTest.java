package com.seathub.auth.token;

import com.seathub.auth.dto.SignupRequest;
import com.seathub.auth.dto.SignupResponse;
import com.seathub.auth.service.AuthService;
import com.seathub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JwtTokenProviderTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createdTokenCanBeValidatedAndParsed() {
        SignupResponse signupResponse = authService.signup(
                new SignupRequest("token@example.com", "password123", "Token User")
        );

        String token = jwtTokenProvider.createAccessToken(
                userRepository.findById(signupResponse.id()).orElseThrow()
        );

        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        assertThat(jwtTokenProvider.getUserId(token)).contains(signupResponse.id());
    }

    @Test
    void malformedTokenIsInvalid() {
        assertThat(jwtTokenProvider.validateToken("not-a-jwt")).isFalse();
        assertThat(jwtTokenProvider.getUserId("not-a-jwt")).isEmpty();
    }
}
