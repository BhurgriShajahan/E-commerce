package com.product.mapper;

import com.product.model.dto.request.CategoryDto;
import com.product.model.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto entityToDto(Category category);
    Category dtoToEntity(CategoryDto categoryDto);
}
