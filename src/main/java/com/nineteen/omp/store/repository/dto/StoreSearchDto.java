package com.nineteen.omp.store.repository.dto;

import java.time.LocalTime;

//controller -> service -> repository
public record StoreSearchDto(
    String name,
    String address,
    String phone,
    LocalTime openHours,
    LocalTime closedHours,
    String closedDays

) {

}
