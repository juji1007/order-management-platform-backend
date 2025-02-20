package com.nineteen.omp.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Nested
  @DisplayName("사용자 정보 조회 테스트")
  class GetUserInfoByUserId {

    @Test
    @DisplayName("사용자 정보 조회 성공")
    void success() {
      // given
      Long userId = 1L;
      User user = User.builder()
          .id(userId)
          .username("test")
          .password("test")
          .nickname("test")
          .role(Role.USER)
          .email("email")
          .is_public(true)
          .delivery_address("address")
          .build();

      when(userRepository.findById(userId)).thenReturn(Optional.of(user));

      // when
      var response = userService.getUserInfo(userId);

      // then
      Assertions.assertAll(
          () -> assertThat(response.username()).isEqualTo(user.getUsername()),
          () -> assertThat(response.nickname()).isEqualTo(user.getNickname()),
          () -> assertThat(response.email()).isEqualTo(user.getEmail()),
          () -> assertThat(response.role()).isEqualTo(user.getRole()),
          () -> assertThat(response.isPublic()).isEqualTo(user.getIs_public()),
          () -> assertThat(response.deliveryAddress()).isEqualTo(user.getDelivery_address())
      );
    }

    @Test
    @DisplayName("사용자 정보 조회 실패")
    void userNotFoundException() {
      // given
      Long userId = 1L;
      when(userRepository.findById(userId)).thenReturn(Optional.empty());

      // when, then
      assertThrows(
          CustomException.class,
          () -> userService.getUserInfo(userId)
      );
    }
  }
}