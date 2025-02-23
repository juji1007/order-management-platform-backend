package com.nineteen.omp.coupon.repository.dto;

import com.nineteen.omp.coupon.domain.Coupon;
import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.user.domain.User;

public record UserCouponData(
    User user,
    Coupon coupon,
    boolean status
) {

  public UserCoupon toEntity() {
    return UserCoupon.builder()
        .user(user)
        .coupon(coupon)
        .status(status)
        .build();
  }
}