package com.nineteen.omp.product.controller.dto;


import com.nineteen.omp.product.domain.StoreProduct;
import java.util.UUID;

public record ProductResponseDto(

    UUID productId,
    String name,
    int price,
    String image,
    String description
) {

  // Product 객체를 받아서 ProductResponseDto 생성
  public ProductResponseDto(StoreProduct storeProduct) {
    this(
        storeProduct.getId(),
        storeProduct.getName(),
        storeProduct.getPrice(),
        storeProduct.getImage(),
        storeProduct.getDescription()
    );
  }
}