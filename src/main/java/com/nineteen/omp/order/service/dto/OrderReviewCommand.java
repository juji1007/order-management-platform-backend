package com.nineteen.omp.order.service.dto;

import com.nineteen.omp.order.controller.dto.OrderReviewRequestDto;
import com.nineteen.omp.order.domain.Order;

public record OrderReviewCommand(
    OrderReviewRequestDto orderReviewRequestDto,
    Order order
) {

}
