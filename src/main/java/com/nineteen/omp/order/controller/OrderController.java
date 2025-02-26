package com.nineteen.omp.order.controller;

import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.order.controller.dto.CompleteOrderRequestDto;
import com.nineteen.omp.order.service.dto.CreateOrderRequestCommand;
import com.nineteen.omp.order.controller.dto.CreateOrderRequestDto;
import com.nineteen.omp.order.controller.dto.OrderResponseDto;
import com.nineteen.omp.order.service.OrderService;
import com.nineteen.omp.order.service.dto.CompleteOrderRequestCommand;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<ResponseDto<?>> createOrder(
      @RequestBody @Valid CreateOrderRequestDto createOrderRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    CreateOrderRequestCommand requestCommand =
        new CreateOrderRequestCommand(createOrderRequestDto, userDetails.getUserId());
    UUID orderId = orderService.createOrder(requestCommand);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ResponseDto.success(orderId));
  }

  @PatchMapping("/{orderId}/complete")
  public ResponseEntity<ResponseDto<?>> completeOrder(
      @PathVariable UUID orderId,
      @RequestBody @Valid CompleteOrderRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    CompleteOrderRequestCommand requestCommand = new CompleteOrderRequestCommand(
        requestDto,
        userDetails.getUserId(),
        orderId
    );
    orderService.completeOrder(requestCommand);
    return ResponseEntity.ok().body(ResponseDto.success());
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

  @PreAuthorize("hasRole('MASTER')")
  @GetMapping("/search")
  public ResponseEntity<ResponseDto<?>> search(
      @RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    PageableUtils.validatePageable(pageable);
    Page<OrderResponseDto> searchOrders = orderService.getOrderByKeyword(keyword, pageable);
    return ResponseEntity.ok(ResponseDto.success(searchOrders.getContent()));
  }
}
