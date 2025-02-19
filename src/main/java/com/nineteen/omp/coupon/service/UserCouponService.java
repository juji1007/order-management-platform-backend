package com.nineteen.omp.coupon.service;

import com.nineteen.omp.coupon.domain.UserCoupon;
import java.util.UUID;

public interface UserCouponService {

  UserCoupon getUserCoupon(UUID userCouponId);
}
