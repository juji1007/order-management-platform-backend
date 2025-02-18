package com.nineteen.omp.user.service;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.user.controller.dto.LoginRequestDto;
import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

  ResponseEntity<ResponseDto<?>> signup(SignupRequestDto requestDto);

  ResponseEntity<ResponseDto<?>> login(LoginRequestDto requestDto);
}
