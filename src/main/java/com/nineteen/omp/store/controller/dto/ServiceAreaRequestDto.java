package com.nineteen.omp.store.controller.dto;

import java.util.UUID;

public record ServiceAreaRequestDto(
    UUID areaId,
    UUID storeId
) {

}
