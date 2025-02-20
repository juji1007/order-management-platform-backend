package com.nineteen.omp.coupon.service.dto;

import com.nineteen.omp.coupon.controller.dto.UserCouponRequestDto;
import com.nineteen.omp.coupon.domain.Coupon;

public record UserCouponCommand(
    UserCouponRequestDto userCouponRequestDto,
//  User user,
    Long userId,
    Coupon coupon
) {

}
