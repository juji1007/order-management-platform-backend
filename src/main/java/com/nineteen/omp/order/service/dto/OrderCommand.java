package com.nineteen.omp.order.service.dto;

import com.nineteen.omp.order.controller.dto.OrderRequestDto;
import com.nineteen.omp.order.controller.dto.OrderRequestDto.OrderProductDto;
import com.nineteen.omp.order.domain.emuns.OrderStatus;
import com.nineteen.omp.order.domain.emuns.OrderType;
import java.util.List;
import java.util.UUID;

public record OrderCommand(
    UUID storeId,
    Long userId,
    int totalPrice,
    List<OrderProductDto> orderProducts,
    String deliveryAddress,
    String specialRequest,
    String paymentMethod,
    OrderType orderType,
    OrderStatus orderStatus,
    UUID couponId
) {

  public static OrderCommand fromOrderRequestDto(OrderRequestDto dto) {
    return new OrderCommand(
        dto.storeId(),
        dto.userId(),
        dto.totalPrice(),
        dto.orderProducts(),
        dto.deliveryAddress(),
        dto.specialRequest(),
        dto.paymentMethod(),
        dto.orderType(),
        dto.orderStatus(),
        dto.couponId()
    );
  }
}