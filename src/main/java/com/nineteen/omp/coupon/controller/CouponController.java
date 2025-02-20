package com.nineteen.omp.coupon.controller;

import com.nineteen.omp.coupon.controller.dto.CouponRequestDto;
import com.nineteen.omp.coupon.controller.dto.CouponResponseDto;
import com.nineteen.omp.coupon.service.CouponService;
import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

  private final CouponService couponService;

  //관리자
  @PostMapping
  public ResponseEntity<ResponseDto<CouponResponseDto>> createCoupon(
      @Valid @RequestBody CouponRequestDto couponRequestDto) {

    CouponResponseDto couponResponseDto = couponService.createCoupon(couponRequestDto);

    return ResponseEntity.ok(ResponseDto.success(couponResponseDto));
  }

  //검색
  @GetMapping
  public ResponseEntity<ResponseDto<Page<CouponResponseDto>>> searchCoupon(
      @RequestParam(
          name = "keyword",
          defaultValue = "",
          required = false
      ) String keyword,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable) {
    Pageable validatedPageable = PageableUtils.validatePageable(pageable);

    Page<CouponResponseDto> searchedCoupons = couponService.searchCoupons(keyword,
        validatedPageable);
    return ResponseEntity.ok(ResponseDto.success(searchedCoupons));
  }

  @GetMapping("/{couponId}")
  public ResponseEntity<ResponseDto<CouponResponseDto>> getCoupon(@PathVariable UUID couponId) {

    CouponResponseDto couponResponseDto = couponService.getCoupon(couponId);

    return ResponseEntity.ok(ResponseDto.success(couponResponseDto));
  }

  @PatchMapping("/{couponId}")
  public ResponseEntity<ResponseDto<CouponResponseDto>> updateCoupon(
      @PathVariable UUID couponId,
      @RequestBody CouponRequestDto couponRequestDto
  ) {

    CouponResponseDto couponResponseDto = couponService.updateCoupon(couponId, couponRequestDto);

    return ResponseEntity.ok(ResponseDto.success(couponResponseDto));
  }

  @DeleteMapping("/{couponId}")
  public ResponseEntity<ResponseDto<?>> deleteCoupon(@PathVariable UUID couponId) {

    couponService.deleteCoupon(couponId);

    return ResponseEntity.ok(ResponseDto.success());
  }


}
