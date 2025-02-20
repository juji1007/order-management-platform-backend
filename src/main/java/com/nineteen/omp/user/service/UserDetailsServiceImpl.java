package com.nineteen.omp.user.service;

import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.domain.UserExceptionCode;
import com.nineteen.omp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("로그인 시도 - username: {}", username);

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(UserExceptionCode.USER_NOT_FOUND));

    return new UserDetailsImpl(user);
  }
}
