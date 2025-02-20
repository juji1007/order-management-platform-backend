package com.nineteen.omp.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.repository.UserRepository;
import com.nineteen.omp.user.service.dto.UpdateUserRequestCommand;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

  @Nested
  @DisplayName("전체 사용자 조회 테스트")
  class GetUsersAll {

    @Test
    @DisplayName("전체 사용자 조회 성공")
    void success() {
      // given
      var pageable = Pageable.unpaged();
      var user = User.builder()
          .id(1L)
          .username("test")
          .password("test")
          .nickname("test")
          .role(Role.USER)
          .email("email")
          .is_public(true)
          .delivery_address("address")
          .build();
      var userPage = new PageImpl<>(List.of(user), pageable, 1);

      when(userRepository.findAll(pageable)).thenReturn(userPage);

      // when
      var response = userService.getUsers(pageable);

      // then
      assertThat(response.getUserPageResponseCommandPage()).hasSize(userPage.getSize());
    }

    @Test
    @DisplayName("사용자가 없을 때")
    void noUserException() {
      // given
      var pageable = Pageable.unpaged();
      when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

      // when
      var response = userService.getUsers(pageable);

      // then
      assertThat(response.getUserPageResponseCommandPage()).hasSize(0);
    }
  }

  @Nested
  @DisplayName("사용자 정보 수정 테스트")
  class UpdateUser {

    @Test
    @DisplayName("사용자 정보 수정 성공")
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
      var requestCommand = new UpdateUserRequestCommand("newNickname", "newEmail", false,
          "newAddress");
      userService.updateUser(userId, requestCommand);

      // then
      Assertions.assertAll(
          () -> assertThat(user.getNickname()).isEqualTo(requestCommand.nickname()),
          () -> assertThat(user.getEmail()).isEqualTo(requestCommand.email()),
          () -> assertThat(user.getIs_public()).isEqualTo(requestCommand.is_public()),
          () -> assertThat(user.getDelivery_address()).isEqualTo(requestCommand.delivery_address())
      );
    }

    @Test
    @DisplayName("사용자 정보 수정 실패")
    void userNotFoundException() {
      // given
      Long userId = 1L;
      when(userRepository.findById(userId)).thenReturn(Optional.empty());

      // when, then
      assertThrows(
          CustomException.class,
          () -> userService.updateUser(userId, mock(UpdateUserRequestCommand.class))
      );
    }
  }

  @Nested
  @DisplayName("사용자 정보 삭제 테스트")
  class DeleteUser {

    @Test
    @DisplayName("사용자 정보 삭제 성공")
    void success() {
      // given
      Long userId = 1L;
      when(userRepository.existsById(userId)).thenReturn(true);

      // when
      userService.deleteUser(userId);

      // then
      verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("잘못된 사용자 정보")
    void userNotFoundException() {
      // given
      Long userId = 1L;
      when(userRepository.existsById(userId)).thenReturn(false);

      // when, then
      assertThrows(
          CustomException.class,
          () -> userService.deleteUser(userId)
      );
    }
  }
}