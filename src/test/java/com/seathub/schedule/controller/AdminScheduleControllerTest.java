package com.seathub.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seathub.product.dto.CreateProductRequest;
import com.seathub.product.dto.ProductResponse;
import com.seathub.product.service.ProductService;
import com.seathub.schedule.dto.CreateScheduleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AdminScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Test
    @WithMockUser
    void createSchedule() throws Exception {
        ProductResponse product = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));
        CreateScheduleRequest request = new CreateScheduleRequest(
                LocalDateTime.of(2026, 6, 1, 19, 0),
                LocalDateTime.of(2026, 6, 1, 21, 0)
        );

        mockMvc.perform(post("/api/v1/admin/products/{productId}/schedules", product.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.productId").value(product.id()))
                .andExpect(jsonPath("$.data.status").value("OPEN"));
    }

    @Test
    @WithMockUser
    void createScheduleRejectsInvalidTimeRange() throws Exception {
        ProductResponse product = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));
        CreateScheduleRequest request = new CreateScheduleRequest(
                LocalDateTime.of(2026, 6, 1, 21, 0),
                LocalDateTime.of(2026, 6, 1, 19, 0)
        );

        mockMvc.perform(post("/api/v1/admin/products/{productId}/schedules", product.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_SCHEDULE_TIME"));
    }
}
