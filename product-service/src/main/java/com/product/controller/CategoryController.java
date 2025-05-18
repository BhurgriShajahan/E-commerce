package com.product.controller;

import com.customutility.model.CustomResponseEntity;
import com.product.model.dto.request.CategoryDto;
import com.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public CustomResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        return categoryService.createCategory(categoryDto);
    }

}
