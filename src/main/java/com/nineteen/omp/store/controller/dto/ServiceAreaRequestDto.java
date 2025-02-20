package com.nineteen.omp.store.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ServiceAreaRequestDto(
    @NotNull(message = "지역 id는 필수 입력값입니다.")
    UUID areaId,
    @NotNull(message = "가게 id는 필수 입력값입니다.")
    UUID storeId
) {

}
