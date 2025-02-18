package com.nineteen.omp.store.service.dto;

import com.nineteen.omp.store.domain.StoreCategory;
import com.nineteen.omp.user.domain.User;
import java.time.LocalTime;

//service -> repository
public record StoreServiceRequestDto(

    User user,
    Integer categoryCode,
    String name,
    String address,
    String phone,
    LocalTime openHours,
    LocalTime closeHours,
    String closedDays
) {

}
