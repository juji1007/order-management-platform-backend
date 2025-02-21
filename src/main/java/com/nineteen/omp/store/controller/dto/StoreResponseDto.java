package com.nineteen.omp.store.controller.dto;

import java.time.LocalTime;
import java.util.UUID;

//service -> controller
public record StoreResponseDto(
    UUID id,
    Long userId,
    String categoryName,
    String name,
    String address,
    String phone,
    LocalTime openHours,
    LocalTime closeHours,
    String closedDays
) {

}
