package com.product.controller;

import com.customutility.model.CustomResponseEntity;
import com.product.model.dto.request.ProductDto;
import com.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product")
public class ProductController {

    private final ProductService productService;

    //Create product
    @PostMapping("/create")
    CustomResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping("/get-all")
    CustomResponseEntity<?> fetchAllProducts() {
        return productService.getAllProducts();
    }

}
