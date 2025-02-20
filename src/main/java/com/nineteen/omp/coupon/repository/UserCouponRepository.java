package com.nineteen.omp.coupon.repository;

import com.nineteen.omp.coupon.controller.dto.UserCouponResponseDto;
import com.nineteen.omp.coupon.domain.UserCoupon;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, UUID> {

  boolean existsByUserIdAndCouponId(Long userId, UUID couponId);

  Page<UserCoupon> findAll(Pageable pageable);
}
