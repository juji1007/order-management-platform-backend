package com.nineteen.omp.store.controller.dto;

import java.time.LocalTime;
import java.util.UUID;

// controller -> service
public record StoreRequestDto(
    UUID categoryId,
    String name,
    String address,
    String phone,
    LocalTime openHours,
    LocalTime closeHours,
    String closedDays
) {

}
