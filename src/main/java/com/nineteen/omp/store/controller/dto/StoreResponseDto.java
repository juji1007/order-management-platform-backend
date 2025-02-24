package com.nineteen.omp.store.controller.dto;

import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreStatus;
import java.time.LocalTime;
import java.util.UUID;

//service -> controller
public record StoreResponseDto(
    UUID id,
    String categoryName,
    String name,
    String address,
    String phone,
    LocalTime openHours,
    LocalTime closeHours,
    String closedDays,
    StoreStatus storeStatus
) {

  //toResponse
  public static StoreResponseDto toResponseDto(Store store) {
    return new StoreResponseDto(
        store.getId(),
        store.getStoreCategory().getCategoryName(),
        store.getName(),
        store.getAddress(),
        store.getPhone(),
        store.getOpenHours(),
        store.getCloseHours(),
        store.getClosedDays(),
        store.getStatus()
    );
  }

}
