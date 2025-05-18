package com.product.mapper;

import com.product.model.dto.request.ProductDto;
import com.product.model.dto.response.ProductResponseDto;
import com.product.model.entities.Product;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto entityToDto(Product product);
    ProductResponseDto entityToProductResponseDto(Product product);
    Product dtoToEntity(ProductDto dto);
    List<ProductDto> toDtoList(List<Product> products);

}
