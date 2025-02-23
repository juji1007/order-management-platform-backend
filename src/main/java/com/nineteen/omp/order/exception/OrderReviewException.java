package com.nineteen.omp.order.exception;

import com.nineteen.omp.global.exception.CustomException;

public class OrderReviewException extends CustomException {

  public OrderReviewException(OrderReviewExceptionCode exceptionCode) {
    super(exceptionCode);
  }
}
