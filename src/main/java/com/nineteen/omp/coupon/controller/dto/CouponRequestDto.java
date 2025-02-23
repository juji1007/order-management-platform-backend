package com.nineteen.omp.coupon.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CouponRequestDto (
    @NotNull(message = "쿠폰 이름은 필수 입력값입니다.")
    String name,
    @NotNull(message = "할인 가격은 필수 입력값입니다.")
    int discountPrice,
    @NotNull(message = "쿠폰 만료일은 필수 입력값입니다.")
    LocalDateTime expiration
){
}
