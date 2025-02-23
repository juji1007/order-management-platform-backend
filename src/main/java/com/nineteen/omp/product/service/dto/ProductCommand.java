package com.nineteen.omp.product.service.dto;

import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import java.util.UUID;

public record ProductCommand(
    UUID storeId,
    String name,
    int price,
    String image,
    String description
) {

  public static ProductCommand fromProductRequestDto(ProductRequestDto dto) {
    return new ProductCommand(
        dto.storeId(),
        dto.name(),
        dto.price(),
        dto.image(),
        dto.description()
    );
  }
}
