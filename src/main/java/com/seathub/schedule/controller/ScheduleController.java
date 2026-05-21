package com.seathub.schedule.controller;

import com.seathub.common.api.ApiResponse;
import com.seathub.common.trace.TraceIdHolder;
import com.seathub.schedule.dto.ScheduleResponse;
import com.seathub.schedule.service.ScheduleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ApiResponse<List<ScheduleResponse>> getSchedules(@PathVariable Long productId) {
        return ApiResponse.success(scheduleService.getSchedules(productId), TraceIdHolder.get());
    }
}
