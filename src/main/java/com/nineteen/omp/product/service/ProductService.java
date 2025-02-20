package com.nineteen.omp.product.service;

import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.service.dto.ProductCommand;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {

  ProductResponseDto addProduct(ProductCommand command);

  ProductResponseDto getProduct(UUID productId);

  ProductResponseDto updateProduct(ProductCommand command, UUID productId);

  Page<ProductResponseDto> searchProducts(String keyword, String category, Pageable pageable);

  void deleteProduct(UUID productId);

}