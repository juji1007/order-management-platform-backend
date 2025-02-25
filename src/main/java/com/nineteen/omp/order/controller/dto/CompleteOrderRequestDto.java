package com.nineteen.omp.order.controller.dto;

import com.nineteen.omp.order.domain.emuns.OrderType;

public record CompleteOrderRequestDto(
    OrderType orderType,
    String deliveryAddress,
    String deliveryRequestMsg,
    String orderRequestMsg
) {
}
