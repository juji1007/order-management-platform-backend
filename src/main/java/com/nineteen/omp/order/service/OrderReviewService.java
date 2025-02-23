package com.nineteen.omp.order.service;

import com.nineteen.omp.order.controller.dto.OrderReviewResponseDto;
import com.nineteen.omp.order.controller.dto.UpdateOrderReviewRequestDto;
import com.nineteen.omp.order.service.dto.OrderReviewCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderReviewService {

  OrderReviewResponseDto createOrderReview(OrderReviewCommand orderReviewCommand);

  Page<OrderReviewResponseDto> getOrderReviews(UUID orderId, Pageable validatedPageable);

  OrderReviewResponseDto getOrderReview(UUID orderReviewId);

  OrderReviewResponseDto updateOrderReview(UUID orderReviewId,
      @Valid UpdateOrderReviewRequestDto updateOrderReviewRequestDto);

  void deleteOrderReview(UUID orderReviewId);
}
