package com.nineteen.omp.coupon.repository;

import com.nineteen.omp.coupon.controller.dto.CouponResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {

  Page<CouponResponseDto> searchCoupons(String keyword, Pageable pageable);
}
