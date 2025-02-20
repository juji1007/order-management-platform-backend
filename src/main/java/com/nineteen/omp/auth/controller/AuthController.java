package com.nineteen.omp.auth.controller;

import com.nineteen.omp.auth.service.AuthService;
import com.nineteen.omp.global.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/logout")
  public ResponseEntity<ResponseDto<?>> logout(
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    log.info("Logout");
    authService.logout(request, response);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @PostMapping("/reissue")
  public ResponseEntity<ResponseDto<?>> reissue(
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    log.info("Reissue");
    authService.reissue(request, response);
    return ResponseEntity.ok(ResponseDto.success());
  }

}
