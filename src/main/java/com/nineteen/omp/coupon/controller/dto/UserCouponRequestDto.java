package com.nineteen.omp.coupon.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserCouponRequestDto(
    @NotNull(message = "쿠폰 ID는 필수 입력값입니다.")
    UUID couponId,

    //null 불가능 default = false
    boolean status
) {

}
