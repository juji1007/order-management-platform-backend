package com.nineteen.omp.order.controller.dto;

import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.domain.OrderProduct;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record OrderResponseDto(
    UUID id,
    String storeName,
    String userName,
    int totalPrice,
    String orderStatus,
    String orderType,
    List<OrderProductResponseDto> orderProducts

) {

  public OrderResponseDto(Order order, List<OrderProduct> orderProducts) {
    this(
        order.getId(),
        order.getStore() != null ? order.getStore().getName() : "No store",
        order.getUser() != null ? order.getUser().getUsername() : "No username",
        order.getTotalPrice(),
        order.getOrderStatus().name(),
        order.getOrderType().name(),
        order.getOrderProducts() != null ? order.getOrderProducts().stream()
            .map(OrderProductResponseDto::new)
            .collect(Collectors.toList()) : Collections.emptyList()
    );
  }
}