package com.nineteen.omp.store.service.dto;

import java.time.LocalTime;
import java.util.UUID;

//service -> controller
public record StoreResponseDto(
    UUID id,
    String name,
    String address,
    String phone,
    LocalTime openHours,
    LocalTime closeHours,
    String closedDays
) {

}
