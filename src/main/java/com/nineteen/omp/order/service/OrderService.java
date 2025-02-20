package com.nineteen.omp.order.service;

import com.nineteen.omp.order.controller.dto.OrderRequestDto;

public interface OrderService {

  void createOrder(OrderRequestDto orderRequestDto);

  List<OrderResponseDto> getAllOrders();

  OrderResponseDto getOrder(UUID orderId);

  void cancelOrder(UUID orderId);
}