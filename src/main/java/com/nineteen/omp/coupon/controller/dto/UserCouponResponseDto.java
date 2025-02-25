package com.nineteen.omp.coupon.controller.dto;

import com.nineteen.omp.coupon.domain.UserCoupon;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserCouponResponseDto(
    UUID id,
    UUID couponId,
    String couponName,
    int discountPrice,
    LocalDateTime expiration,
    boolean status
) {

  public static UserCouponResponseDto toResponseDto(UserCoupon savedUserCoupon) {
    return new UserCouponResponseDto(
        savedUserCoupon.getId(),
        savedUserCoupon.getCoupon().getId(),
        savedUserCoupon.getCoupon().getName(),
        savedUserCoupon.getCoupon().getDiscountPrice(),
        savedUserCoupon.getCoupon().getExpiration(),
        savedUserCoupon.isStatus()
    );
  }
}