package com.nineteen.omp.store.controller.dto;

import com.nineteen.omp.store.domain.ServiceArea;
import java.util.UUID;

public record ServiceAreaResponseDto(
    UUID id,
    UUID areaId,
    UUID storeId
) {

  public static ServiceAreaResponseDto toResponseDto(ServiceArea serviceArea) {
    return new ServiceAreaResponseDto(
        serviceArea.getId(),
        serviceArea.getArea().getId(),
        serviceArea.getStore().getId()
    );
  }
}