package com.nineteen.omp.coupon.service;

import static com.nineteen.omp.coupon.controller.dto.UserCouponResponseDto.toResponseDto;

import com.nineteen.omp.coupon.controller.dto.UserCouponRequestDto;
import com.nineteen.omp.coupon.controller.dto.UserCouponResponseDto;
import com.nineteen.omp.coupon.domain.Coupon;
import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.coupon.exception.CouponException;
import com.nineteen.omp.coupon.exception.CouponExceptionCode;
import com.nineteen.omp.coupon.exception.UserCouponException;
import com.nineteen.omp.coupon.exception.UserCouponExceptionCode;
import com.nineteen.omp.coupon.repository.CouponRepository;
import com.nineteen.omp.coupon.repository.UserCouponRepository;
import com.nineteen.omp.coupon.repository.dto.UserCouponData;
import com.nineteen.omp.coupon.service.dto.UserCouponCommand;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.exception.UserExceptionCode;
import com.nineteen.omp.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCouponServiceImpl implements UserCouponService {

  private final UserCouponRepository userCouponRepository;
  private final CouponRepository couponRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserCouponResponseDto createUserCoupon(UserCouponCommand userCouponCommand) {
    User user = userRepository.findById(userCouponCommand.userId())
        .orElseThrow(() -> new CustomException(UserExceptionCode.USER_NOT_FOUND));

    Coupon coupon = couponRepository.findById(userCouponCommand.userCouponRequestDto().couponId())
        .orElseThrow(() -> new CouponException(CouponExceptionCode.COUPON_NOT_FOUND));

    boolean isExist = userCouponRepository.existsByUserIdAndCouponId(userCouponCommand.userId(),
        coupon.getId());
    if (isExist) {
      throw new UserCouponException(UserCouponExceptionCode.USER_COUPON_IS_DUPLICATED);
    }

    UserCouponData userCouponData = new UserCouponData(
        user,
        coupon,
        userCouponCommand.userCouponRequestDto().status());

    UserCoupon savedUserCoupon = userCouponRepository.save(userCouponData.toEntity());

    return toResponseDto(savedUserCoupon);
  }

  @Override
  public Page<UserCouponResponseDto> searchUserCoupon(Pageable pageable) {
    Page<UserCoupon> userCouponPage = userCouponRepository.findAll(pageable);
    List<UserCouponResponseDto> content = userCouponPage.stream()
        .map(userCoupon -> new UserCouponResponseDto(
            userCoupon.getId(),
            userCoupon.getCoupon().getId(),
            userCoupon.isStatus()
        ))
        .collect(Collectors.toList());
    return new PageImpl<>(content, pageable, userCouponPage.getTotalElements());
  }

  @Override
  public UserCouponResponseDto getUserCoupon(UUID userCouponId) {
    return toResponseDto(findUserCouponOrThrow(userCouponId));
  }

  @Override
  @Transactional
  public UserCouponResponseDto updateUserCoupon(UUID userCouponId,
      UserCouponRequestDto userCouponRequestDto) {
    UserCoupon userCoupon = findUserCouponOrThrow(userCouponId);

    userCoupon.changeStatus(userCouponRequestDto.status());

    UserCoupon updatedUserCoupon = userCouponRepository.save(userCoupon);
    return toResponseDto(updatedUserCoupon);
  }

  @Override
  @Transactional
  public void deleteUserCoupon(UUID userCouponId) {
    userCouponRepository.delete(findUserCouponOrThrow(userCouponId));
  }

  private UserCoupon findUserCouponOrThrow(UUID userCouponId) {
    return userCouponRepository.findById(userCouponId)
        .orElseThrow(() -> new UserCouponException(UserCouponExceptionCode.USER_COUPON_NOT_FOUND));
  }
}
