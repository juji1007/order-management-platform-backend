package com.nineteen.omp.order.service;

import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.order.controller.dto.OrderProductRequestDto;
import com.nineteen.omp.order.controller.dto.OrderResponseDto;
import com.nineteen.omp.order.domain.Delivery;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.domain.OrderProduct;
import com.nineteen.omp.order.domain.emuns.OrderStatus;
import com.nineteen.omp.order.domain.emuns.OrderType;
import com.nineteen.omp.order.exception.OrderException;
import com.nineteen.omp.order.exception.OrderExceptionCode;
import com.nineteen.omp.order.repository.DeliveryRepository;
import com.nineteen.omp.order.repository.OrderProductRepository;
import com.nineteen.omp.order.repository.OrderRepository;
import com.nineteen.omp.order.service.dto.CompleteOrderRequestCommand;
import com.nineteen.omp.order.service.dto.CreateOrderRequestCommand;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.product.exception.ProductExceptionCode;
import com.nineteen.omp.product.repository.ProductRepository;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.exception.StoreExceptionCode;
import com.nineteen.omp.store.repository.StoreRepository;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.repository.UserRepository;
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
  private final DeliveryRepository deliveryRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public UUID createOrder(CreateOrderRequestCommand requestCommand) {
    Store store = storeRepository.findById(requestCommand.storeId())
        .orElseThrow(() -> new CustomException(StoreExceptionCode.STORE_NOT_FOUND));
    User user = userRepository.findById(requestCommand.userId())
        .orElseThrow(() -> new CustomException(StoreExceptionCode.STORE_NOT_FOUND));

    Order order = Order.builder()
        .store(store)
        .user(user)
        .totalPrice(calculateTotalPrice(requestCommand.orderProducts()))
        .orderStatus(OrderStatus.CREATED)
        .build();

    orderRepository.save(order);

    List<OrderProduct> products = requestCommand.orderProducts().stream()
        .map(orderProductRequestDto -> {
          StoreProduct findProduct =
              productRepository.findById(orderProductRequestDto.storeProductId())
                  .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_NOT_FOUND));
          return OrderProduct.builder()
              .order(order)
              .storeProduct(findProduct)
              .quantity(orderProductRequestDto.quantity())
              .price(orderProductRequestDto.pricePerItem())
              .build();
        })
        .toList();

    orderProductRepository.saveAll(products);

    return order.getId();
  }

  private int calculateTotalPrice(List<OrderProductRequestDto> orderProductRequestDtos) {
    int totalPrice = 0;
    for (OrderProductRequestDto orderProductRequestDto : orderProductRequestDtos) {
      totalPrice += orderProductRequestDto.pricePerItem() * orderProductRequestDto.quantity();
    }
    return totalPrice;
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

  @Override
  @Transactional
  public void completeOrder(CompleteOrderRequestCommand requestCommand) {
    Order order = findById(requestCommand.orderId());
    order.completeOrder(requestCommand.orderType(), requestCommand.orderRequestMsg());

    if (requestCommand.orderType().equals(OrderType.DELIVERY)) {
      Delivery delivery = Delivery.builder()
          .order(order)
          .reqstMsg(requestCommand.deliveryRequestMsg())
          .adress(requestCommand.deliveryAddress())
          .build();
      deliveryRepository.save(delivery);
    }
  }

  private Order findById(UUID orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderException(OrderExceptionCode.ORDER_NOT_FOUND));
  }
}
