package com.nineteen.omp.coupon.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserCouponExceptionCode implements ExceptionCode {

  USER_COUPON_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 등록된 사용자 쿠폰 입니다."),
  USER_COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자 쿠폰을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
