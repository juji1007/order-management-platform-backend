package com.nineteen.omp.order.service.dto;

import com.nineteen.omp.order.controller.dto.CreateOrderRequestDto;
import com.nineteen.omp.order.controller.dto.OrderProductRequestDto;
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
