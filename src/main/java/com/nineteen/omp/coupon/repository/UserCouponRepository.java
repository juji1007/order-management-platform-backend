package com.nineteen.omp.coupon.repository;

import com.nineteen.omp.coupon.domain.UserCoupon;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, UUID> {

}
