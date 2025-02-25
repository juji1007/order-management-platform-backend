package com.nineteen.omp.coupon.controller;

import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.coupon.controller.dto.UserCouponRequestDto;
import com.nineteen.omp.coupon.controller.dto.UserCouponResponseDto;
import com.nineteen.omp.coupon.service.UserCouponService;
import com.nineteen.omp.coupon.service.dto.UserCouponCommand;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/v1/user-coupons")
public class UserCouponController {

  private final UserCouponService userCouponService;

  @PostMapping
  public ResponseEntity<ResponseDto<UserCouponResponseDto>> createUserCoupon(
      @Valid @RequestBody UserCouponRequestDto userCouponRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    Long userId = userDetails.getUserId();

    UserCouponResponseDto userCouponResponseDto = userCouponService.createUserCoupon(
        new UserCouponCommand(userCouponRequestDto, userId));

    return ResponseEntity.ok(ResponseDto.success(userCouponResponseDto));
  }

  //검색
  @GetMapping
  public ResponseEntity<ResponseDto<Page<UserCouponResponseDto>>> getMyCoupons(
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    Pageable validatedPageable = PageableUtils.validatePageable(pageable);

    Page<UserCouponResponseDto> searchedCoupons =
        userCouponService.getUserCoupons(userDetails.getUserId(), validatedPageable);

    return ResponseEntity.ok(ResponseDto.success(searchedCoupons));
  }

  // 의미가 있나?
  @GetMapping("/{userCouponId}")
  public ResponseEntity<ResponseDto<UserCouponResponseDto>> getUserCoupon(
      @PathVariable UUID userCouponId
  ) {
    UserCouponResponseDto userCouponResponseDto = userCouponService.getUserCoupon(userCouponId);
    return ResponseEntity.ok(ResponseDto.success(userCouponResponseDto));
  }

  //쿠폰 사용/사용불가 업데이트 / 상태불가는 -> is_deleted = true 로 처리
  @PreAuthorize("hasRole('MASTER')")
  @PatchMapping("/{userCouponId}")
  public ResponseEntity<ResponseDto<UserCouponResponseDto>> updateUserCoupon(
      @PathVariable UUID userCouponId,
      @Valid @RequestBody UserCouponRequestDto userCouponRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    Long userId = userDetails.getUserId();
    UserCouponResponseDto userCouponResponseDto = userCouponService.updateUserCoupon(userCouponId,
        userCouponRequestDto);
    return ResponseEntity.ok(ResponseDto.success(userCouponResponseDto));
  }

  @PatchMapping("/{userCouponId}/delete")
  public ResponseEntity<ResponseDto<?>> deleteUserCoupon(
      @PathVariable UUID userCouponId
  ) {
    userCouponService.deleteUserCoupon(userCouponId);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @GetMapping("/search")
  public ResponseEntity<ResponseDto<?>> searchUserCoupon(
      @RequestParam(
          name = "userId",
          required = false,
          defaultValue = "0L"
      ) Long userId,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    Pageable validatedPageable = PageableUtils.validatePageable(pageable);

    Page<UserCouponResponseDto> searchedCoupons =
        userCouponService.searchUserCoupons(userId, validatedPageable);

    return ResponseEntity.ok(ResponseDto.success(searchedCoupons));
  }
}