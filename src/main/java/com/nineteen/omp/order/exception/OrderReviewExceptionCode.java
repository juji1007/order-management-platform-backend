package com.nineteen.omp.order.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderReviewExceptionCode implements ExceptionCode {

  ORDER_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
  ORDER_REVIEW_CONTENT_IS_NULL(HttpStatus.BAD_REQUEST, "주문 리뷰 내용이 null 입니다."),
  ORDER_REVIEW_RATING_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "별점은 0부터 5 사이의 값이어야 합니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
