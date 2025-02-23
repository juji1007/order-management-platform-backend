package com.nineteen.omp.store.service.dto;

import com.nineteen.omp.store.controller.dto.StoreRequestDto;

public record StoreCommand(
    Long userId,
    StoreRequestDto storeRequestDto
//    User user,
) {

}
