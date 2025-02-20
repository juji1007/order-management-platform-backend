package com.nineteen.omp.user.service;

import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.service.dto.GetUserInfoResponseCommand;

public interface UserService {

  void signup(SignupRequestDto requestDto);

  GetUserInfoResponseCommand getUserInfo(Long userId);
}
