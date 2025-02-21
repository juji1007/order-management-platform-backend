package com.nineteen.omp.product.controller.dto;


import com.nineteen.omp.product.domain.StoreProduct;
import java.util.UUID;

public record ProductResponseDto(
    UUID productId,
    String name,
    int price,
    String image,
    String description,
    Boolean isDeleted
) {

  public ProductResponseDto(StoreProduct product) {
    this(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getImage(),
        product.getDescription(),
        product.getIsDeleted()
    );
  }
}