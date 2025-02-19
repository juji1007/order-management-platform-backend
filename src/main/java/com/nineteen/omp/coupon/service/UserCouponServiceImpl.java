package com.nineteen.omp.coupon.service;

import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.coupon.exception.UserCouponExceptionCode;
import com.nineteen.omp.coupon.repository.UserCouponRepository;
import com.nineteen.omp.global.exception.CustomException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

  private final UserCouponRepository userCouponRepository;

  @Override
  public UserCoupon getUserCoupon(UUID userCouponId) {
    return userCouponRepository.findById(userCouponId)
        .orElseThrow(() -> new CustomException(UserCouponExceptionCode.USER_COUPON_NOT_FOUND));
  }
}
