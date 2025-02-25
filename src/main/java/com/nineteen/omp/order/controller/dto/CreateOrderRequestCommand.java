package com.nineteen.omp.order.controller.dto;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestCommand(
    UUID storeId,
    Long userId,
    List<OrderProductRequestDto> orderProducts
) {

  public CreateOrderRequestCommand(CreateOrderRequestDto createOrderRequestDto, Long userId) {
    this(
        createOrderRequestDto.storeId(),
        userId,
        createOrderRequestDto.orderProducts()
    );
  }
}
