package com.nineteen.omp.coupon.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponseDto(
    UUID id,
    String name,
    Integer discountPrice,
    LocalDateTime expiration
) {

}
