package com.nineteen.omp.order.controller.dto;

public record UpdateOrderReviewRequestDto(
    String content,
    Integer rating
) {

}
