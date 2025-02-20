package com.nineteen.omp.coupon.service;

import com.nineteen.omp.coupon.controller.dto.UserCouponRequestDto;
import com.nineteen.omp.coupon.controller.dto.UserCouponResponseDto;
import com.nineteen.omp.coupon.service.dto.UserCouponCommand;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCouponService {

  UserCouponResponseDto createUserCoupon(UserCouponCommand userCouponCommand);

  Page<UserCouponResponseDto> searchUserCoupon(Pageable pageable);

  UserCouponResponseDto getUserCoupon(UUID userCouponId);

  UserCouponResponseDto updateUserCoupon(UUID userCouponId,
      UserCouponRequestDto userCouponRequestDto);

  void deleteUserCoupon(UUID userCouponId);
}
