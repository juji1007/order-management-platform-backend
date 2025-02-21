package com.nineteen.omp.order.exception;

import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.global.exception.ExceptionCode;

public class OrderException extends CustomException {

  public OrderException(ExceptionCode code) {
    super(code);
  }
}
