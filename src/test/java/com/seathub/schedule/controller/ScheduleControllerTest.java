package com.seathub.schedule.controller;

import com.seathub.product.dto.CreateProductRequest;
import com.seathub.product.dto.ProductResponse;
import com.seathub.product.service.ProductService;
import com.seathub.schedule.dto.CreateScheduleRequest;
import com.seathub.schedule.dto.ScheduleResponse;
import com.seathub.schedule.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ScheduleService scheduleService;

    @Test
    void getSchedules() throws Exception {
        ProductResponse product = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));
        ScheduleResponse schedule = scheduleService.createSchedule(product.id(), new CreateScheduleRequest(
                LocalDateTime.of(2026, 6, 1, 19, 0),
                LocalDateTime.of(2026, 6, 1, 21, 0)
        ));

        mockMvc.perform(get("/api/v1/products/{productId}/schedules", product.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(schedule.id()))
                .andExpect(jsonPath("$.data[0].productId").value(product.id()))
                .andExpect(jsonPath("$.data[0].status").value("OPEN"));
    }

    @Test
    void getSchedulesRejectsUnknownProduct() throws Exception {
        mockMvc.perform(get("/api/v1/products/{productId}/schedules", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }
}
