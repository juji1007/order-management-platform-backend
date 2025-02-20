package com.nineteen.omp.store.controller.dto;

import java.util.UUID;

public record ServiceAreaResponseDto(
    UUID id,
    UUID areaId,
    UUID storeId
) {

}
