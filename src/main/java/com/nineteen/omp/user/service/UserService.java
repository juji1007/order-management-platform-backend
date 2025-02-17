package com.nineteen.omp.user.service;

import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public void signup(SignupRequestDto requestDto) {

    // 회원 중복 확인
    Optional<User> checkUsername = userRepository.findByUsername(requestDto.username());
    if (checkUsername.isPresent()) {
      throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
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

  }

}
