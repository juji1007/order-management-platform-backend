package com.nineteen.omp.coupon.service.dto;

import com.nineteen.omp.coupon.controller.dto.UserCouponRequestDto;
import com.nineteen.omp.user.domain.User;

public record UserCouponCommand(
    UserCouponRequestDto userCouponRequestDto,
    User user
) {

}
