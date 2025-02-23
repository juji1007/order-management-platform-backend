package com.nineteen.omp.coupon.service;

import com.nineteen.omp.coupon.controller.dto.CouponRequestDto;
import com.nineteen.omp.coupon.controller.dto.CouponResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {


  CouponResponseDto createCoupon(CouponRequestDto couponRequestDto);

  Page<CouponResponseDto> searchCoupons(String keyword, Pageable validatedPageable);

  CouponResponseDto getCoupon(UUID couponId);

  CouponResponseDto updateCoupon(UUID couponId, CouponRequestDto couponRequestDto);

  void deleteCoupon(UUID couponId);
}
