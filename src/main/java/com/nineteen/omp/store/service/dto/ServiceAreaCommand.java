package com.nineteen.omp.store.service.dto;

import com.nineteen.omp.store.controller.dto.ServiceAreaRequestDto;
import com.nineteen.omp.store.domain.Area;
import com.nineteen.omp.store.domain.Store;

public record ServiceAreaCommand(
    ServiceAreaRequestDto serviceAreaRequestDto,
    Area area,
    Store store
) {

}
