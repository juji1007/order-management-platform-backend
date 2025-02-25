package com.nineteen.omp.order.service;

import static com.nineteen.omp.order.controller.dto.OrderReviewResponseDto.toResponseDto;

import com.nineteen.omp.order.controller.dto.OrderReviewRequestDto;
import com.nineteen.omp.order.controller.dto.OrderReviewResponseDto;
import com.nineteen.omp.order.controller.dto.UpdateOrderReviewRequestDto;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.domain.OrderReview;
import com.nineteen.omp.order.exception.OrderException;
import com.nineteen.omp.order.exception.OrderReviewException;
import com.nineteen.omp.order.exception.OrderReviewExceptionCode;
import com.nineteen.omp.order.repository.OrderRepository;
import com.nineteen.omp.order.repository.OrderReviewRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderReviewServiceImpl implements OrderReviewService {

  private final OrderReviewRepository orderReviewRepository;
  private final OrderRepository orderRepository;


  @Override
  @Transactional
  public OrderReviewResponseDto createOrderReview(OrderReviewRequestDto orderReviewRequestDto) {

    Order order = orderRepository.findById(orderReviewRequestDto.orderId())
        .orElseThrow(() -> new OrderException(OrderReviewExceptionCode.ORDER_IS_NOT_FOUND));

    OrderReview savedOrderReview = orderReviewRepository.save(
        OrderReview.builder()
            .order(order)
            .content(orderReviewRequestDto.content())
            .rating(orderReviewRequestDto.rating())
            .build()
    );

    return toResponseDto(savedOrderReview);
  }

  @Override
  public Page<OrderReviewResponseDto> getOrderReviews(UUID orderId, Pageable pageable) {
    Page<OrderReview> orderReviewPage = orderReviewRepository.findAll(pageable);

    List<OrderReviewResponseDto> content = orderReviewPage.stream()
        .map(orderReview -> new OrderReviewResponseDto(
            orderReview.getId(),
            orderReview.getOrder().getId(),
            orderReview.getContent(),
            orderReview.getRating()
        ))
        .collect(Collectors.toList());

    return new PageImpl<>(content, pageable, orderReviewPage.getTotalElements());
  }

  @Override
  public OrderReviewResponseDto getOrderReview(UUID orderReviewId) {
    return toResponseDto(findOrderReviewOrElseThrow(orderReviewId));
  }

  @Override
  @Transactional
  public OrderReviewResponseDto updateOrderReview(UUID orderReviewId,
      UpdateOrderReviewRequestDto updateOrderReviewRequestDto) {
    OrderReview orderReview = findOrderReviewOrElseThrow(orderReviewId);

    orderReview.changeContent(updateOrderReviewRequestDto.content());
    orderReview.changeRating(updateOrderReviewRequestDto.rating());

    OrderReview updatedOrderReview = orderReviewRepository.save(orderReview);
    return toResponseDto(updatedOrderReview);
  }

  @Override
  @Transactional
  public void deleteOrderReview(UUID orderReviewId) {
    orderReviewRepository.delete(findOrderReviewOrElseThrow(orderReviewId));
  }

  private OrderReview findOrderReviewOrElseThrow(UUID orderReviewId) {
    return orderReviewRepository.findById(orderReviewId)
        .orElseThrow(() -> new OrderReviewException(OrderReviewExceptionCode.ORDER_IS_NOT_FOUND));
  }

}
