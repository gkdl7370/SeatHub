package com.seathub.auth.controller;

import com.seathub.auth.dto.LoginRequest;
import com.seathub.auth.dto.LoginResponse;
import com.seathub.auth.dto.MeResponse;
import com.seathub.auth.dto.SignupRequest;
import com.seathub.auth.dto.SignupResponse;
import com.seathub.auth.service.AuthService;
import com.seathub.auth.token.AuthenticatedUser;
import com.seathub.common.api.ApiResponse;
import com.seathub.common.trace.TraceIdHolder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.signup(request), TraceIdHolder.get()));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request), TraceIdHolder.get());
    }

    @GetMapping("/me")
    public ApiResponse<MeResponse> me(Authentication authentication) {
        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();

        return ApiResponse.success(authService.me(currentUser.id()), TraceIdHolder.get());
    }
}
