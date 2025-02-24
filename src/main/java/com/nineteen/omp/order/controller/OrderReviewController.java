package com.nineteen.omp.order.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.order.controller.dto.OrderReviewRequestDto;
import com.nineteen.omp.order.controller.dto.OrderReviewResponseDto;
import com.nineteen.omp.order.controller.dto.UpdateOrderReviewRequestDto;
import com.nineteen.omp.order.service.OrderReviewService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-reviews")
public class OrderReviewController {

  private final OrderReviewService orderReviewService;

  @PostMapping
  public ResponseEntity<ResponseDto<OrderReviewResponseDto>> createOrderReview(
      @Valid @RequestBody OrderReviewRequestDto orderReviewRequestDto) {

    OrderReviewResponseDto orderReviewResponseDto = orderReviewService.createOrderReview(
        orderReviewRequestDto);

    return ResponseEntity.ok(ResponseDto.success(orderReviewResponseDto));
  }

  //주문 id로 한 주문에 여러개 리뷰 조회, 페이징 처리
  @GetMapping("order/{orderId}/reviews")
  public ResponseEntity<ResponseDto<Page<OrderReviewResponseDto>>> getOrderReviews(
      @PathVariable UUID orderId,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable) {
    Pageable validatedPageable = PageableUtils.validatePageable(pageable);
    Page<OrderReviewResponseDto> orderReviews = orderReviewService.getOrderReviews(orderId,
        validatedPageable);

    return ResponseEntity.ok(ResponseDto.success(orderReviews));
  }

  //주문리뷰 아이디로 1개 조회
  @GetMapping("/{orderReviewId}")
  public ResponseEntity<ResponseDto<OrderReviewResponseDto>> getOrderReview(
      @PathVariable UUID orderReviewId) {
    OrderReviewResponseDto orderReviewResponseDto = orderReviewService.getOrderReview(
        orderReviewId);

    return ResponseEntity.ok(ResponseDto.success(orderReviewResponseDto));
  }

  @PatchMapping("/{orderReviewId}")
  public ResponseEntity<ResponseDto<OrderReviewResponseDto>> updateOrderReview(
      @PathVariable UUID orderReviewId,
      @RequestBody UpdateOrderReviewRequestDto updateOrderReviewRequestDto) {

    OrderReviewResponseDto orderReviewResponseDto = orderReviewService.updateOrderReview(
        orderReviewId, updateOrderReviewRequestDto);

    return ResponseEntity.ok(ResponseDto.success(orderReviewResponseDto));
  }

  @DeleteMapping("/{orderReviewId}")
  public ResponseEntity<ResponseDto<?>> deleteOrderReview(@PathVariable UUID orderReviewId) {
    orderReviewService.deleteOrderReview(orderReviewId);
    return ResponseEntity.ok(ResponseDto.success());
  }
}
