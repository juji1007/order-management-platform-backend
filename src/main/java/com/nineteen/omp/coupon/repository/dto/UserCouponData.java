package com.nineteen.omp.coupon.repository.dto;

import com.nineteen.omp.coupon.domain.Coupon;
import com.nineteen.omp.coupon.domain.UserCoupon;

public record UserCouponData(
    Long userId,
    Coupon coupon,
    boolean status
) {
  public UserCoupon toEntity() {
    return UserCoupon.builder()
        .userId(userId)
        .coupon(coupon)
        .status(status)
        .build();
  }
}
