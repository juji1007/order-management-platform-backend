package com.nineteen.omp.order.service;

import com.nineteen.omp.order.domain.Order;

public interface OrderService {

  Order getOrder(String orderId);
}
