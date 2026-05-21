package com.seathub.schedule.service;

import com.seathub.common.api.ErrorCode;
import com.seathub.common.exception.BusinessException;
import com.seathub.product.dto.CreateProductRequest;
import com.seathub.product.dto.ProductResponse;
import com.seathub.product.service.ProductService;
import com.seathub.schedule.dto.CreateScheduleRequest;
import com.seathub.schedule.dto.ScheduleResponse;
import com.seathub.schedule.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ScheduleServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void createSchedule() {
        ProductResponse product = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));
        CreateScheduleRequest request = new CreateScheduleRequest(
                LocalDateTime.of(2026, 6, 1, 19, 0),
                LocalDateTime.of(2026, 6, 1, 21, 0)
        );

        ScheduleResponse response = scheduleService.createSchedule(product.id(), request);

        assertThat(response.id()).isNotNull();
        assertThat(response.productId()).isEqualTo(product.id());
        assertThat(response.startAt()).isEqualTo(request.startAt());
        assertThat(response.endAt()).isEqualTo(request.endAt());
        assertThat(response.status()).isEqualTo("OPEN");
        assertThat(scheduleRepository.findById(response.id())).isPresent();
    }

    @Test
    void getSchedulesReturnsEarliestScheduleFirst() {
        ProductResponse product = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));
        ScheduleResponse late = scheduleService.createSchedule(product.id(), new CreateScheduleRequest(
                LocalDateTime.of(2026, 6, 2, 19, 0),
                LocalDateTime.of(2026, 6, 2, 21, 0)
        ));
        ScheduleResponse early = scheduleService.createSchedule(product.id(), new CreateScheduleRequest(
                LocalDateTime.of(2026, 6, 1, 19, 0),
                LocalDateTime.of(2026, 6, 1, 21, 0)
        ));

        List<ScheduleResponse> responses = scheduleService.getSchedules(product.id());

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(early.id());
        assertThat(responses.get(1).id()).isEqualTo(late.id());
    }

    @Test
    void createScheduleRejectsUnknownProduct() {
        CreateScheduleRequest request = new CreateScheduleRequest(
                LocalDateTime.of(2026, 6, 1, 19, 0),
                LocalDateTime.of(2026, 6, 1, 21, 0)
        );

        assertThatThrownBy(() -> scheduleService.createSchedule(999L, request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
    }

    @Test
    void createScheduleRejectsInvalidTimeRange() {
        ProductResponse product = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));
        CreateScheduleRequest request = new CreateScheduleRequest(
                LocalDateTime.of(2026, 6, 1, 21, 0),
                LocalDateTime.of(2026, 6, 1, 19, 0)
        );

        assertThatThrownBy(() -> scheduleService.createSchedule(product.id(), request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SCHEDULE_TIME);
    }

    @Test
    void getSchedulesRejectsUnknownProduct() {
        assertThatThrownBy(() -> scheduleService.getSchedules(999L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
