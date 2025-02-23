package com.nineteen.omp.store.repository.dto;

import com.nineteen.omp.store.domain.Area;
import com.nineteen.omp.store.domain.ServiceArea;
import com.nineteen.omp.store.domain.Store;

public record ServiceAreaData(
    Area area,
    Store store
) {

  public ServiceArea toEntity() {
    return ServiceArea.builder()
        .area(area)
        .store(store)
        .build();
  }
}
