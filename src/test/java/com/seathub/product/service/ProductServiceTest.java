package com.seathub.product.service;

import com.seathub.product.dto.CreateProductRequest;
import com.seathub.product.dto.ProductResponse;
import com.seathub.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createProduct() {
        CreateProductRequest request = new CreateProductRequest("뮤지컬 A", "주말 공연 상품");

        ProductResponse response = productService.createProduct(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("뮤지컬 A");
        assertThat(response.description()).isEqualTo("주말 공연 상품");
        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(productRepository.findById(response.id())).isPresent();
    }
}

