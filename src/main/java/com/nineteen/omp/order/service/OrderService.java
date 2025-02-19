package com.nineteen.omp.order.service;

import com.nineteen.omp.order.domain.Order;
import java.util.UUID;

public interface OrderService {

  Order getOrder(UUID orderId);
}
