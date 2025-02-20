package com.nineteen.omp.coupon.service;

import com.nineteen.omp.coupon.controller.dto.CouponRequestDto;
import com.nineteen.omp.coupon.controller.dto.CouponResponseDto;
import com.nineteen.omp.coupon.domain.Coupon;
import com.nineteen.omp.coupon.exception.CouponException;
import com.nineteen.omp.coupon.exception.CouponExceptionCode;
import com.nineteen.omp.coupon.repository.CouponRepository;
import com.nineteen.omp.coupon.repository.dto.CouponData;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

  private final CouponRepository couponRepository;

  @Transactional
  public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto) {
    boolean isExist = couponRepository.existsByName(couponRequestDto.name());
    if (isExist) {
      throw new CouponException(CouponExceptionCode.COUPON_IS_DUPLICATED);
    }

    if (couponRequestDto.discountPrice() <= 0) {
      throw new CouponException(CouponExceptionCode.COUPON_DISCOUNTPRICE_INVALID);
    }

    if (couponRequestDto.expiration().isBefore(LocalDateTime.now())) {
      throw new CouponException(CouponExceptionCode.COUPON_EXPIRATION_INVALID);
    }

    Coupon savedCoupon = couponRepository.save(new CouponData(
        couponRequestDto.name(),
        couponRequestDto.discountPrice(),
        couponRequestDto.expiration()
    ).toEntity());

    return toResponse(savedCoupon);
  }

  public Page<CouponResponseDto> searchCoupons(String keyword, Pageable pageable) {
    return couponRepository.searchCoupons(keyword, pageable);
  }

  public CouponResponseDto getCoupon(UUID couponId) {
    return toResponse(findCouponOrThrow(couponId));
  }

  @Transactional
  public CouponResponseDto updateCoupon(UUID couponId, CouponRequestDto couponRequestDto) {
    Coupon coupon = findCouponOrThrow(couponId);

    coupon.changeName(couponRequestDto.name());
    coupon.changeDiscountPrice(couponRequestDto.discountPrice());
    coupon.changeExpiration(couponRequestDto.expiration());

    Coupon updatedCoupon = couponRepository.save(coupon);
    return toResponse(updatedCoupon);
  }

  @Transactional
  public void deleteCoupon(UUID couponId) {
    couponRepository.delete(findCouponOrThrow(couponId));
  }

  private Coupon findCouponOrThrow(UUID couponId) {
    return couponRepository.findById(couponId)
        .orElseThrow(() -> new CouponException(CouponExceptionCode.COUPON_NOT_FOUND));
  }

  private CouponResponseDto toResponse(Coupon coupon) {
    return new CouponResponseDto(
        coupon.getId(),
        coupon.getName(),
        coupon.getDiscountPrice(),
        coupon.getExpiration()
    );
  }
}
