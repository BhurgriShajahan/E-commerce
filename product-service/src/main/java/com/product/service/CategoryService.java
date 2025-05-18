package com.product.service;

import com.customutility.model.CustomResponseEntity;
import com.product.model.dto.request.CategoryDto;

public interface CategoryService {

    CustomResponseEntity<?> createCategory(CategoryDto categoryDto);
}
