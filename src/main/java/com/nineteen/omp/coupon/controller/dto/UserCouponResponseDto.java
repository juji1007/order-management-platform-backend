package com.nineteen.omp.coupon.controller.dto;

import com.nineteen.omp.coupon.domain.UserCoupon;
import java.util.UUID;

public record UserCouponResponseDto(
    UUID id,
    UUID couponId,
    boolean status
) {
  public static UserCouponResponseDto toResponseDto(UserCoupon savedUserCoupon) {
    return new UserCouponResponseDto(
        savedUserCoupon.getId(),
        savedUserCoupon.getCoupon().getId(),
        savedUserCoupon.isStatus()
    );
  }
}
