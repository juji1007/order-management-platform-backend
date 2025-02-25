package com.nineteen.omp.order.service;

import com.nineteen.omp.order.controller.dto.OrderRequestDto;
import com.nineteen.omp.order.controller.dto.OrderResponseDto;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.domain.OrderProduct;
import com.nineteen.omp.order.exception.OrderException;
import com.nineteen.omp.order.exception.OrderExceptionCode;
import com.nineteen.omp.order.repository.OrderProductRepository;
import com.nineteen.omp.order.repository.OrderRepository;
import com.nineteen.omp.order.service.dto.OrderCommand;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.product.repository.ProductRepository;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.repository.StoreRepository;
import com.nineteen.omp.user.domain.User;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {


  private final StoreRepository storeRepository;
  private final OrderRepository orderRepository;
  private final OrderProductRepository orderProductRepository;
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public void createOrder(OrderRequestDto orderRequestDto) {
    OrderCommand orderCommand = OrderCommand.fromOrderRequestDto(orderRequestDto);
    // TODO : 병합 후 storeException 예외 적용
    Store store = storeRepository.findById(orderCommand.storeId())
        .orElseThrow(() -> new RuntimeException("Store not found"));

    // TODO: Authentication 객체로 가져오도록 수정 필요.
    User user = User.builder()
        .id(1L)
        .username("exampleUser")
        .build();

    Order order = Order.builder()
        .store(store)
        .user(user)
        .totalPrice(orderCommand.totalPrice())
        .orderStatus(orderCommand.orderStatus())
        .orderType(orderCommand.orderType())
        .build();

    orderRepository.save(order);

    for (OrderRequestDto.OrderProductRequestDto productDto : orderRequestDto.orderProducts()) {
      // Now you can call findById on the injected productRepository
      StoreProduct storeProduct = productRepository.findById(productDto.storeProductId())
          .orElseThrow(() -> new RuntimeException("Product not found"));

      OrderProduct orderProduct = OrderProduct.builder()
          .order(order)
          .store(store)
          .storeProduct(storeProduct)
          .quantity(productDto.quantity())
          .price(productDto.pricePerItem())
          .build();

      orderProductRepository.save(orderProduct);
    }
  }

  @Override
  public List<OrderResponseDto> getAllOrders() {
    List<Order> orders = orderRepository.findAll();
    return orders.stream()
        .map(order -> new OrderResponseDto(order, orderProductRepository.findByOrder(order)))
        .collect(Collectors.toList());
  }

  @Override
  public OrderResponseDto getOrder(UUID orderId) {
    Order order = findById(orderId);
    return new OrderResponseDto(order, orderProductRepository.findByOrder(order));
  }

  @Override
  @Transactional
  public void cancelOrder(UUID orderId) {
    Order order = findById(orderId);
    Order cancelledOrder = order.cancelOrder();
    orderRepository.save(cancelledOrder);
  }

  @Override
  public Page<OrderResponseDto> getOrderByKeyword(String keyword, Pageable pageable) {
    return orderRepository.searchOrdersByKeyword(keyword, pageable);
  }

  private Order findById(UUID orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderException(OrderExceptionCode.ORDER_NOT_FOUND));
  }
}
