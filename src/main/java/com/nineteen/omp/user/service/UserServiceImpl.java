package com.nineteen.omp.user.service;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.user.controller.dto.LoginRequestDto;
import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public ResponseEntity<ResponseDto<?>> signup(SignupRequestDto requestDto) {

    // 회원 중복 확인
    boolean checkUsername = userRepository.existsByUsername(requestDto.username());
    if (checkUsername) {
      throw new CustomException(CommonExceptionCode.DUPLICATE_USERNAME);
    }
    // 사용자 등록
    User user = User.builder()
        .username(requestDto.username())
        .password(requestDto.password())
        .nickname(requestDto.nickname())
        .role(requestDto.role())  // 교체 필요
        .email(requestDto.email())
        .is_public(requestDto.is_public())
        .delivery_address(requestDto.delivery_address())
        .build();

    userRepository.save(user);

    return ResponseEntity.ok().body(ResponseDto.success());
  }

  @Override
  public ResponseEntity<ResponseDto<?>> login(LoginRequestDto requestDto) {

    User user = userRepository.findByUsername(requestDto.username())
        .orElseThrow(() -> new CustomException(CommonExceptionCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
      throw new CustomException(CommonExceptionCode.INVALID_PASSWORD);
    }
    return ResponseEntity.ok().body(ResponseDto.success());
  }
}
