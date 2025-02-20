package com.nineteen.omp.user.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.user.controller.dto.GetUserInfoResponseDto;
import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public void signup(
      @RequestBody @Valid SignupRequestDto requestDto
  ) {

    userService.signup(requestDto);

  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<ResponseDto<?>> getUserInfo(
      @PathVariable(name = "userId") Long userId
  ) {
    var responseCommand = userService.getUserInfo(userId);
    var response = new GetUserInfoResponseDto(responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }
}
