package com.nineteen.omp.user.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users/signup")
  public ResponseEntity<ResponseDto<?>> signup(
      @RequestBody @Valid SignupRequestDto requestDto
  ) {

    userService.signup(requestDto);

    return ResponseEntity.ok().body(ResponseDto.success());

  }
  
}
