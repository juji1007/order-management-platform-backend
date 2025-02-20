package com.nineteen.omp.coupon.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CouponExceptionCode implements ExceptionCode {
  COUPON_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 쿠폰입니다."),
  COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."),
  COUPON_NAME_IS_NULL(HttpStatus.BAD_REQUEST, "쿠폰 이름은 필수 입력값입니다."),
  COUPON_DISCOUNTPRICE_IS_NULL(HttpStatus.BAD_REQUEST, "할인 가격은 필수 입력값입니다."),
  COUPON_EXPIRATION_IS_NULL(HttpStatus.BAD_REQUEST, "쿠폰 만료 날짜는 필수 입력값입니다."),
  COUPON_DISCOUNTPRICE_INVALID(HttpStatus.BAD_REQUEST, "쿠폰 할인 가격은 0원 보다 커야합니다."),
  COUPON_EXPIRATION_INVALID(HttpStatus.BAD_REQUEST, "쿠폰 만료 날짜는 현재 시간 이후여야 합니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
