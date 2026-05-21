package com.seathub.schedule.dto;

import com.seathub.schedule.domain.Schedule;

import java.time.LocalDateTime;

public record ScheduleResponse(
        Long id,
        Long productId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String status
) {

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getProduct().getId(),
                schedule.getStartAt(),
                schedule.getEndAt(),
                schedule.getStatus().name()
        );
    }
}
