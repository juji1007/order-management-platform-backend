package com.nineteen.omp.store.controller.dto;

import jakarta.validation.constraints.NotNull;

public record AreaRequestDto(
    @NotNull(message = "주소 시는 필수 입력값입니다.")
    String si,
    @NotNull(message = "주소 구는 필수 입력값입니다.")
    String gu,
    @NotNull(message = "주소 동은 필수 입력값입니다.")
    String dong
) {

}
