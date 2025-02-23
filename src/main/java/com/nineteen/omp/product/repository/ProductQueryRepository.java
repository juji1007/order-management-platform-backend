package com.nineteen.omp.product.repository;

import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQueryRepository {

  Page<ProductResponseDto> searchProducts(String keyword, String category, Pageable pageable);
}