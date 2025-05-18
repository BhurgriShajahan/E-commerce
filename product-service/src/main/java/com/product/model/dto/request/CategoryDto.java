package com.product.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDto {

    private Long id;
    @NotNull(message = "category name is required!")
    private String name;

}
