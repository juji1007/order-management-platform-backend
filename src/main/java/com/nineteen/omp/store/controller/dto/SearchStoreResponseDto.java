package com.nineteen.omp.store.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.UUID;

public record SearchStoreResponseDto(
    UUID storeId,
    String storeName,
    String categoryName,
//    List<String> serviceAreaName,
    Double rating,
    String statusName
) {

  @QueryProjection
  public SearchStoreResponseDto {
    if (rating != null) {
      //소수점 첫번째 자리까지 표시
      rating = Double.valueOf(String.format("%.1f", rating));
    }

  }
}
