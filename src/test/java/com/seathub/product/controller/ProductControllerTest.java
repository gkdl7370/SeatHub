package com.seathub.product.controller;

import com.seathub.product.dto.CreateProductRequest;
import com.seathub.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Test
    void getProducts() throws Exception {
        productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));
        productService.createProduct(new CreateProductRequest("클래스 B", "원데이 클래스"));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("클래스 B"))
                .andExpect(jsonPath("$.data[1].name").value("뮤지컬 A"));
    }

    @Test
    void getProduct() throws Exception {
        var product = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));

        mockMvc.perform(get("/api/v1/products/{productId}", product.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(product.id()))
                .andExpect(jsonPath("$.data.name").value("뮤지컬 A"))
                .andExpect(jsonPath("$.data.description").value("주말 공연 상품"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    void getProductReturnsNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/products/{productId}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }
}

