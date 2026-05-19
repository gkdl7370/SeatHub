package com.seathub.auth.service;

import com.seathub.auth.dto.LoginRequest;
import com.seathub.auth.dto.LoginResponse;
import com.seathub.auth.dto.SignupRequest;
import com.seathub.auth.dto.SignupResponse;
import com.seathub.common.api.ErrorCode;
import com.seathub.common.exception.BusinessException;
import com.seathub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void signupCreatesUserWithEncodedPassword() {
        SignupRequest request = new SignupRequest("user@example.com", "password123", "테스트회원");

        SignupResponse response = authService.signup(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.email()).isEqualTo("user@example.com");
        assertThat(response.name()).isEqualTo("테스트회원");

        assertThat(userRepository.findById(response.id()))
                .get()
                .satisfies(user -> {
                    assertThat(user.getPassword()).isNotEqualTo("password123");
                    assertThat(user.getRoles()).extracting(Enum::name).containsExactly("USER");
                });
    }

    @Test
    void signupRejectsDuplicatedEmail() {
        SignupRequest request = new SignupRequest("user@example.com", "password123", "테스트회원");
        authService.signup(request);

        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    void loginReturnsAccessToken() {
        authService.signup(new SignupRequest("user@example.com", "password123", "테스트회원"));

        LoginResponse response = authService.login(new LoginRequest("user@example.com", "password123"));

        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.accessToken()).contains(".");
        assertThat(response.expiresIn()).isEqualTo(3600);
    }

    @Test
    void loginRejectsUnknownEmail() {
        LoginRequest request = new LoginRequest("unknown@example.com", "password123");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_LOGIN);
    }

    @Test
    void loginRejectsWrongPassword() {
        authService.signup(new SignupRequest("user@example.com", "password123", "테스트회원"));

        LoginRequest request = new LoginRequest("user@example.com", "wrong-password");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_LOGIN);
    }
}
