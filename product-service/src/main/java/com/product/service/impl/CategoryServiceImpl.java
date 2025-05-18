package com.product.service.impl;

import com.customutility.model.CustomResponseEntity;
import com.product.mapper.CategoryMapper;
import com.product.model.dto.request.CategoryDto;
import com.product.model.entities.Category;
import com.product.repository.CategoryRepository;
import com.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CustomResponseEntity<?> createCategory(CategoryDto categoryDto) {
        logger.info("Received request to create category");

        try {
            if (categoryDto == null || categoryDto.getName() == null || categoryDto.getName().isBlank()) {
                logger.warn("Category name is missing or invalid in request");
                return CustomResponseEntity.error("Category name is required.");
            }

            boolean exists = categoryRepository.findByName(categoryDto.getName()).isPresent();
            if (exists) {
                logger.warn("Category '{}' already exists", categoryDto.getName());
                return CustomResponseEntity.error("Category already exists.");
            }

            Category category = categoryMapper.dtoToEntity(categoryDto);
            category = categoryRepository.save(category);

            CategoryDto savedDto = categoryMapper.entityToDto(category);

            logger.info("Category '{}' created successfully with ID {}", category.getName(), category.getId());
            return new CustomResponseEntity<>(savedDto, "Category created successfully.");

        } catch (Exception e) {
            logger.error("Error occurred while creating category: {}", e.getMessage(), e);
            return CustomResponseEntity.errorResponse(e);
        }
    }
}
