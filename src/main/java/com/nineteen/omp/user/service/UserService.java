package com.nineteen.omp.user.service;

import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.service.dto.GetUserInfoPageResponseCommand;
import com.nineteen.omp.user.service.dto.GetUserInfoResponseCommand;
import com.nineteen.omp.user.service.dto.UpdateUserRequestCommand;
import org.springframework.data.domain.Pageable;

public interface UserService {

  void signup(SignupRequestDto requestDto);

  GetUserInfoResponseCommand getUserInfo(Long userId);

  GetUserInfoPageResponseCommand getUsers(Pageable pageable);

  void updateUser(Long userId, UpdateUserRequestCommand requestCommand);

  void deleteUser(Long userId);

  GetUserInfoPageResponseCommand searchUser(String nickname, Pageable pageable);

  void updateUserRole(Long userId);
}
