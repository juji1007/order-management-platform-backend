package com.nineteen.omp.order.service;

import com.nineteen.omp.order.service.dto.CreateOrderRequestCommand;
import com.nineteen.omp.order.controller.dto.OrderResponseDto;
import com.nineteen.omp.order.service.dto.CompleteOrderRequestCommand;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

  UUID createOrder(CreateOrderRequestCommand requestCommand);

  List<OrderResponseDto> getAllOrders();

  OrderResponseDto getOrder(UUID orderId);

  void cancelOrder(UUID orderId);

  Page<OrderResponseDto> getOrderByKeyword(String keyword, Pageable pageable);

  void completeOrder(CompleteOrderRequestCommand requestCommand);
}
