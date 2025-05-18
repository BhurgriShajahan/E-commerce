package com.product.service;

import com.customutility.model.CustomResponseEntity;
import com.product.model.dto.request.ProductDto;
import com.product.model.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {

    CustomResponseEntity<ProductDto> createProduct(ProductDto productDto);

    CustomResponseEntity<List<ProductResponseDto>> getAllProducts();

}
