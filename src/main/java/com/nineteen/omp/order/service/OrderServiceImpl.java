package com.nineteen.omp.order.service;

import com.nineteen.omp.order.controller.dto.OrderRequestDto;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.repository.OrderProductRepository;
import com.nineteen.omp.order.repository.OrderRepository;
import com.nineteen.omp.order.service.dto.OrderCommand;
import com.nineteen.omp.product.repository.ProductRepository;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.repository.StoreRepository;
import com.nineteen.omp.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {


  private final StoreRepository storeRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final OrderProductRepository orderProductRepository;

  public void createOrder(OrderRequestDto orderRequestDto) {
    OrderCommand orderCommand = OrderCommand.fromOrderRequestDto(orderRequestDto);
    // TODO : 예외처리 적용
    Store store = storeRepository.findById(orderCommand.storeId())
        .orElseThrow(() -> new RuntimeException("Store not found"));

    // TODO: userId를 하드코딩된 값(1L) 대신 userRepository에서 조회해야 함.
    User user = User.builder()
        .id(1L)  // 랜덤 Long 값 설정
        .username("exampleUser")                      // 필드에 값 설정
        .build();
    // User user = userRepository.findById(orderCommand.userId()).orElseThrow(() -> new RuntimeException("User not found"));

    Order order = Order.builder()
        .store(store)
        .user(user)
        .totalPrice(orderCommand.totalPrice())
        .orderStatus(orderCommand.orderStatus())
        .orderType(orderCommand.orderType())
        .build();

    orderRepository.save(order);
  }

}