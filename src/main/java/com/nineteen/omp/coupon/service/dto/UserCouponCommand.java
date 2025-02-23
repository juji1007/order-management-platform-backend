package com.nineteen.omp.coupon.service.dto;

import com.nineteen.omp.coupon.controller.dto.UserCouponRequestDto;

public record UserCouponCommand(
    UserCouponRequestDto userCouponRequestDto,
//  User user,
    Long userId
) {

}
