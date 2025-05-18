package com.product.service.impl;

import com.customutility.model.CustomResponseEntity;
import com.product.mapper.ProductMapper;
import com.product.model.dto.request.ProductDto;
import com.product.model.dto.response.ProductResponseDto;
import com.product.model.entities.Category;
import com.product.model.entities.Product;
import com.product.repository.CategoryRepository;
import com.product.repository.ProductRepository;
import com.product.service.ProductService;
import com.product.util.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AuthenticatedUser authenticatedUser;
    private final CategoryRepository categoryRepository;

    @Override
    public CustomResponseEntity<ProductDto> createProduct(ProductDto productDto) {
        logger.info("Request received to create a new product");

        try {
            if (productDto == null) {
                logger.warn("ProductDto is null");
                return CustomResponseEntity.error("Product data is required!");
            }

            Long authUserId = Long.parseLong(authenticatedUser.getAuthUserId().toString());
            logger.debug("Authenticated User ID: {}", authUserId);

            String categoryName = productDto.getCategory() != null ? productDto.getCategory().getName() : null;
            if (categoryName == null || categoryName.isBlank()) {
                logger.warn("Category name is missing in ProductDto");
                return CustomResponseEntity.error("Category name is required!");
            }

            Optional<Category> categoryOpt = categoryRepository.findByName(categoryName);
            if (categoryOpt.isEmpty()) {
                logger.warn("Category '{}' not found", categoryName);
                return CustomResponseEntity.error("Category not found: " + categoryName);
            }

            productDto.setCategory(categoryOpt.get());
            productDto.setUserId(authUserId);

            Product product = productMapper.dtoToEntity(productDto);
            Product savedProduct = productRepository.save(product);

            ProductDto responseDto = productMapper.entityToDto(savedProduct);
            logger.info("Product created successfully with ID: {}", savedProduct.getId());

            return new CustomResponseEntity<>(responseDto, "Product created successfully");

        } catch (Exception e) {
            logger.error("Error while creating product: {}", e.getMessage(), e);
            return CustomResponseEntity.errorResponse(e);
        }
    }

    @Override
    public CustomResponseEntity<List<ProductResponseDto>> getAllProducts() {
        logger.info("Request received to fetch all products");

        try {
            List<Product> products = productRepository.findAll();

            if (products.isEmpty()) {
                logger.warn("No products found in the database");
                return new CustomResponseEntity<>(List.of(), "No products available");
            }

            List<ProductResponseDto> productResponseDtos = products.stream()
                    .map(productMapper::entityToProductResponseDto)
                    .toList();

            logger.info("Retrieved {} products successfully", productResponseDtos.size());
            return new CustomResponseEntity<>(productResponseDtos, "Products retrieved successfully");

        } catch (Exception e) {
            logger.error("Error while fetching products: {}", e.getMessage(), e);
            return CustomResponseEntity.errorResponse(e);
        }
    }
}
