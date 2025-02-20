package com.nineteen.omp.coupon.controller.dto;

import java.util.UUID;

public record UserCouponResponseDto(
    UUID id,
    Long userId,
    UUID couponId,
    boolean status
) {

}
