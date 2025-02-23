package com.nineteen.omp.order.controller.dto;

import com.nineteen.omp.order.domain.OrderReview;
import java.util.UUID;

public record OrderReviewResponseDto(
    UUID id,
    UUID orderId,
    String content,
    Integer rating
) {

  public static OrderReviewResponseDto toResponseDto(OrderReview orderReview) {
    return new OrderReviewResponseDto(
        orderReview.getId(),
        orderReview.getOrder().getId(),
        orderReview.getContent(),
        orderReview.getRating()
    );
  }
}
