package com.product.model.dto.response;

import lombok.Data;

@Data
public class ProductResponseDto {

    private Long id;
    private String name;
    private String categoryName;
    private Long userId;
}
