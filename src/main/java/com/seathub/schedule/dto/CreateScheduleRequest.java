package com.seathub.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateScheduleRequest(
        @NotNull LocalDateTime startAt,
        @NotNull LocalDateTime endAt
) {
}
