package com.nineteen.omp.product.service;

import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.service.dto.ProductCommand;
import java.util.UUID;


public interface ProductService {

  ProductResponseDto addProduct(ProductCommand command);

  ProductResponseDto getProduct(UUID productId);

  ProductResponseDto updateProduct(ProductCommand command, UUID productId);

  void deleteProduct(UUID productId);

/*  Page<ProductResponseDto> searchProducts(String keyword, String category, int i, int size,
      String sortBy, boolean isAsc);*/
}