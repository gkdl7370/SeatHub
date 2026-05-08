package com.seathub.common.web;

import com.seathub.common.api.ApiResponse;
import com.seathub.common.trace.TraceIdHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ApiResponse<HealthResponse> health() {
        return ApiResponse.success(new HealthResponse("UP", Instant.now()), TraceIdHolder.get());
    }

    public record HealthResponse(String status, Instant checkedAt) {
    }
}

