package com.nineteen.omp.coupon.controller.dto;

import com.nineteen.omp.coupon.domain.Coupon;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponseDto(
    UUID id,
    String name,
    int discountPrice,
    LocalDateTime expiration
) {

  public static CouponResponseDto toResponse(Coupon coupon) {
    return new CouponResponseDto(
        coupon.getId(),
        coupon.getName(),
        coupon.getDiscountPrice(),
        coupon.getExpiration()
    );
  }
}
