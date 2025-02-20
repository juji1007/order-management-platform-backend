package com.nineteen.omp.coupon.service;

import com.nineteen.omp.coupon.controller.dto.UserCouponRequestDto;
import com.nineteen.omp.coupon.controller.dto.UserCouponResponseDto;
import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.coupon.exception.UserCouponException;
import com.nineteen.omp.coupon.exception.UserCouponExceptionCode;
import com.nineteen.omp.coupon.repository.UserCouponRepository;
import com.nineteen.omp.coupon.repository.dto.UserCouponData;
import com.nineteen.omp.coupon.service.dto.UserCouponCommand;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

  private final UserCouponRepository userCouponRepository;

  @Override
  @Transactional
  public UserCouponResponseDto createUserCoupon(UserCouponCommand userCouponCommand) {
    boolean isExist = userCouponRepository.existsByUserIdAndCouponId(userCouponCommand.userId(),
        userCouponCommand.coupon().getId());
    if (isExist) {
      throw new UserCouponException(UserCouponExceptionCode.USER_COUPON_IS_DUPLICATED);
    }

    UserCouponData userCouponData = new UserCouponData(
        userCouponCommand.userId(),
        userCouponCommand.coupon(),
        userCouponCommand.userCouponRequestDto().status());

    UserCoupon savedUserCoupon = userCouponRepository.save(userCouponData.toEntity());

    return toResponseDto(savedUserCoupon);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserCouponResponseDto> searchUserCoupon(Pageable pageable) {
    Page<UserCoupon> userCouponPage = userCouponRepository.findAll(pageable);
    List<UserCouponResponseDto> content = userCouponPage.stream()
        .map(userCoupon -> new UserCouponResponseDto(
            userCoupon.getId(),
            userCoupon.getUserId(),
            userCoupon.getCoupon().getId(),
            userCoupon.isStatus()
        ))
        .collect(Collectors.toList());
    return new PageImpl<>(content, pageable, userCouponPage.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
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

  private UserCouponResponseDto toResponseDto(UserCoupon savedUserCoupon) {
    return new UserCouponResponseDto(
        savedUserCoupon.getId(),
        savedUserCoupon.getUserId(),
        savedUserCoupon.getCoupon().getId(),
        savedUserCoupon.isStatus()
    );
  }
}
