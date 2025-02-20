package com.nineteen.omp.order.controller.dto;


import com.nineteen.omp.order.domain.emuns.OrderStatus;
import com.nineteen.omp.order.domain.emuns.OrderType;
import java.util.List;
import java.util.UUID;

public record OrderRequestDto(
    UUID storeId,
    Long userId,
    int totalPrice,
    List<OrderProductRequestDto> orderProducts,
    String deliveryAddress,
    String specialRequest,
    String paymentMethod,
    OrderType orderType,
    OrderStatus orderStatus,
    UUID couponId
) {

  public record OrderProductRequestDto(UUID storeProductId, int quantity, int pricePerItem) {

  }

}
