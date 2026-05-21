package com.seathub.product.service;

import com.seathub.product.dto.CreateProductRequest;
import com.seathub.product.dto.ProductResponse;
import com.seathub.product.repository.ProductRepository;
import com.seathub.common.api.ErrorCode;
import com.seathub.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void getProductsReturnsLatestProductFirst() {
        ProductResponse first = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));
        ProductResponse second = productService.createProduct(new CreateProductRequest("클래스 B", "원데이 클래스"));

        List<ProductResponse> responses = productService.getProducts();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(second.id());
        assertThat(responses.get(1).id()).isEqualTo(first.id());
    }

    @Test
    void getProductReturnsProduct() {
        ProductResponse created = productService.createProduct(new CreateProductRequest("뮤지컬 A", "주말 공연 상품"));

        ProductResponse response = productService.getProduct(created.id());

        assertThat(response.id()).isEqualTo(created.id());
        assertThat(response.name()).isEqualTo("뮤지컬 A");
        assertThat(response.description()).isEqualTo("주말 공연 상품");
        assertThat(response.status()).isEqualTo("ACTIVE");
    }

    @Test
    void getProductRejectsUnknownProduct() {
        assertThatThrownBy(() -> productService.getProduct(999L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
