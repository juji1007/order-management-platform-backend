package com.nineteen.omp.order.controller.dto;

import java.util.UUID;

public record OrderReviewResponseDto(
    UUID id,
    UUID orderId,
    String content,
    Integer rating
) {

}
