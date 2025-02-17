package com.nineteen.omp.product.service;

import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.Product;
import java.util.UUID;


public interface ProductService {

  Product addProduct(ProductRequestDto requestDto);

  ProductResponseDto getProductById(UUID productId);
}