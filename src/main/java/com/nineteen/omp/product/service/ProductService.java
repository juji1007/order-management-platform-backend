package com.nineteen.omp.product.service;

import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.StoreProduct;
import java.util.UUID;


public interface ProductService {

  StoreProduct addProduct(ProductRequestDto requestDto);

  ProductResponseDto getProduct(UUID productId);

  ProductResponseDto updateProduct(ProductRequestDto requestDto, UUID productId);


}