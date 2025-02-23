package com.nineteen.omp.store.repository.dto;

import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreCategory;
import com.nineteen.omp.store.service.dto.StoreCommand;

public record StoreData(
    StoreCommand storeCommand,
    StoreCategory storeCategory
) {

  public Store toEntity() {
    return Store.builder()
//        .user
        .userId(storeCommand.userId())
        .storeCategory(storeCategory)
        .name(storeCommand.storeRequestDto().name())
        .address(storeCommand.storeRequestDto().address())
        .phone(storeCommand.storeRequestDto().phone())
        .openHours(storeCommand.storeRequestDto().openHours())
        .closeHours(storeCommand.storeRequestDto().closeHours())
        .closedDays(storeCommand.storeRequestDto().closedDays())
        .build();
  }
}
