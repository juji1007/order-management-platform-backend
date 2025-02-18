package com.nineteen.omp.store.controller.dto;

import com.nineteen.omp.store.domain.StoreCategory;
import java.time.LocalTime;

// controller -> service
public record StoreRequestDto(
    Integer categoryCode,
    String name,
    String address,
    String phone,
    LocalTime openHours,
    LocalTime closeHours,
    String closedDays
) {

}
