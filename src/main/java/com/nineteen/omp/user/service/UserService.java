package com.nineteen.omp.user.service;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.user.controller.dto.SignupRequestDto;

public interface UserService {

  void signup(SignupRequestDto requestDto);
}
