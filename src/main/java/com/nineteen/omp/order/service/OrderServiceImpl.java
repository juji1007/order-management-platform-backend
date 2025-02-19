package com.nineteen.omp.order.service;

import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.exception.OrderExceptionCode;
import com.nineteen.omp.order.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  @Override
  public Order getOrder(String orderId) {
    return orderRepository.findById(UUID.fromString(orderId))
        .orElseThrow(() -> new CustomException(OrderExceptionCode.ORDER_NOT_FOUND));
  }
}
