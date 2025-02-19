package com.nineteen.omp.user.service;

import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.domain.UserExceptionCode;
import com.nineteen.omp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  public void signup(SignupRequestDto requestDto) {

    String encodedPassword = passwordEncoder.encode(requestDto.password());

    // 회원 중복 확인
    boolean checkUsername = userRepository.existsByUsername(requestDto.username());
    if (checkUsername) {
      throw new CustomException(UserExceptionCode.DUPLICATE_USERNAME);
    }
    // 사용자 등록
    User user = User.builder()
        .username(requestDto.username())
        .password(encodedPassword)
        .nickname(requestDto.nickname())
        .role(requestDto.role())
        .email(requestDto.email())
        .is_public(requestDto.is_public())
        .delivery_address(requestDto.delivery_address())
        .build();

    userRepository.save(user);

  }

}
