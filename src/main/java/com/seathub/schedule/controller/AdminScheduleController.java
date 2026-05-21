package com.seathub.schedule.controller;

import com.seathub.common.api.ApiResponse;
import com.seathub.common.trace.TraceIdHolder;
import com.seathub.schedule.dto.CreateScheduleRequest;
import com.seathub.schedule.dto.ScheduleResponse;
import com.seathub.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/products/{productId}/schedules")
public class AdminScheduleController {

    private final ScheduleService scheduleService;

    public AdminScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleResponse>> createSchedule(
            @PathVariable Long productId,
            @Valid @RequestBody CreateScheduleRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(scheduleService.createSchedule(productId, request), TraceIdHolder.get()));
    }
}
