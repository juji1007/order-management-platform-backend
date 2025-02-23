package com.nineteen.omp.coupon.repository.dto;

import com.nineteen.omp.coupon.domain.Coupon;
import java.time.LocalDateTime;

public record CouponData(
    String name,
    int discountPrice,
    LocalDateTime expiration
) {

  public Coupon toEntity() {
    return Coupon.builder()
        .name(name)
        .discountPrice(discountPrice)
        .expiration(expiration)
        .build();
  }
}
