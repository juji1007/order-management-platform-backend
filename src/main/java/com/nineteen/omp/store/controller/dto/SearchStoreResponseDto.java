package com.nineteen.omp.store.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record SearchStoreResponseDto(
    String storeName,
    String categoryName,
    List<String> serviceAreaName,
    int rating,
    String statusName
) {

  @QueryProjection
  public SearchStoreResponseDto {

  }
}
