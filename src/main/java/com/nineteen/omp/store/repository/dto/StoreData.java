package com.nineteen.omp.store.repository.dto;

import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreCategory;
import com.nineteen.omp.store.service.dto.StoreCommand;
import com.nineteen.omp.user.domain.User;

public record StoreData(
    StoreCommand storeCommand,
    StoreCategory storeCategory,
    User user
) {

  public Store toEntity() {
    return Store.builder()
        .user(user)
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
