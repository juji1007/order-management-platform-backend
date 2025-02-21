package com.nineteen.omp.order.controller.dto;

import com.nineteen.omp.order.domain.OrderProduct;
import java.util.UUID;

public record OrderProductResponseDto(
    UUID storeProductId,
    String productName,
    int price,
    int quantity
) {

  public OrderProductResponseDto(OrderProduct orderProduct) {
    this(
        orderProduct.getStoreProduct().getId(),
        orderProduct.getStoreProduct().getName(),
        orderProduct.getQuantity(),
        orderProduct.getPrice()
    );
  }
}