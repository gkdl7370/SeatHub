package com.seathub.product.dto;

import com.seathub.product.domain.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String status
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStatus().name()
        );
    }
}

