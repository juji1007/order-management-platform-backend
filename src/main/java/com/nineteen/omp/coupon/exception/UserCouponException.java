package com.nineteen.omp.coupon.exception;

import com.nineteen.omp.global.exception.CustomException;

public class UserCouponException extends CustomException {

  public UserCouponException(UserCouponExceptionCode exceptionCode) {
    super(exceptionCode);
  }
}
