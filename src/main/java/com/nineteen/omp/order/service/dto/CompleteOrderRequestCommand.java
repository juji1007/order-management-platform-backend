package com.nineteen.omp.order.service.dto;

import com.nineteen.omp.order.controller.dto.CompleteOrderRequestDto;
import com.nineteen.omp.order.domain.emuns.OrderType;
import java.util.UUID;

public record CompleteOrderRequestCommand(
    Long userId,
    UUID orderId,
    OrderType orderType,
    String deliveryAddress,
    String deliveryRequestMsg,
    String orderRequestMsg
) {

  public CompleteOrderRequestCommand(
      CompleteOrderRequestDto dto,
      Long userId,
      UUID orderId
  ) {
    this(
        userId,
        orderId,
        dto.orderType(),
        dto.deliveryAddress(),
        dto.deliveryRequestMsg(),
        dto.orderRequestMsg()
    );

  }
}
