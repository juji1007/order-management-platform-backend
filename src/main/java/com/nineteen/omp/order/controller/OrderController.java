package com.nineteen.omp.order.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.order.controller.dto.OrderRequestDto;
import com.nineteen.omp.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  @Autowired
  private OrderServiceImpl orderService;

  @PostMapping
  public ResponseEntity<ResponseDto<?>> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
    orderService.createOrder(orderRequestDto);
    return ResponseEntity.ok().body(ResponseDto.success("Order created successfully"));
  }

  @GetMapping
  public ResponseEntity<ResponseDto<?>> getAllOrders() {
    List<OrderResponseDto> orders = orderService.getAllOrders();
    return ResponseEntity.ok().body(ResponseDto.success(orders));
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<ResponseDto<?>> getOrder(@PathVariable UUID orderId) {
    OrderResponseDto order = orderService.getOrder(orderId);
    return ResponseEntity.ok().body(ResponseDto.success(order));
  }

  @PatchMapping("/{orderId}")
  public ResponseEntity<ResponseDto<?>> cancelOrder(@PathVariable UUID orderId) {
    orderService.cancelOrder(orderId);
    return ResponseEntity.ok().body(ResponseDto.success());
  }

}
