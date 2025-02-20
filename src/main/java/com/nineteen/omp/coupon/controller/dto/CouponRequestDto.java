package com.nineteen.omp.coupon.controller.dto;

import java.time.LocalDateTime;

public record CouponRequestDto (
    String name,
    Integer discountPrice,
    LocalDateTime expiration
){
}
