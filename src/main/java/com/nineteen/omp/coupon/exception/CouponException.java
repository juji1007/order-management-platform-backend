package com.nineteen.omp.coupon.exception;

import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.global.exception.ExceptionCode;

public class CouponException extends CustomException {

  public CouponException(ExceptionCode exceptionCode) {
    super(exceptionCode);
  }
}
