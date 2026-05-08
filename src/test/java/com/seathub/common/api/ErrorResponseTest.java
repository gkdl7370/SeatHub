package com.seathub.common.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void createsErrorResponseFromErrorCode() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.RESERVATION_ALREADY_OCCUPIED, "trace-id");

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("RESERVATION_ALREADY_OCCUPIED");
        assertThat(response.message()).isEqualTo("이미 예약된 좌석입니다.");
        assertThat(response.traceId()).isEqualTo("trace-id");
    }
}

