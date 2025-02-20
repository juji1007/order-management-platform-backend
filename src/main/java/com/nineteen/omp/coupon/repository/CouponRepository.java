package com.nineteen.omp.coupon.repository;

import com.nineteen.omp.coupon.domain.Coupon;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, UUID>, CouponRepositoryCustom {

  boolean existsByName(String name);
}
