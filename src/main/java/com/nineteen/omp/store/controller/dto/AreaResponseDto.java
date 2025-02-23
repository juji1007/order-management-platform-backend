package com.nineteen.omp.store.controller.dto;

import java.util.UUID;

public record AreaResponseDto(
    UUID id,
    String si,
    String gu,
    String dong
) {

}
