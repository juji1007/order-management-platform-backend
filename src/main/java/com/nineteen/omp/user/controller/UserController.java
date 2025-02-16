package com.nineteen.omp.user.controller;

import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/users/signup")
  public String signPage() {
    return "get signup success";
  }

  @PostMapping("/users/signup")
  public String signup(@RequestBody @Valid SignupRequestDto requestDto,
      BindingResult bindingResult) {

    // Validation 예외처리
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    if (fieldErrors.size() > 0) {
      for (FieldError fieldError : bindingResult.getFieldErrors()) {
        log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
      }
      return "signup failed";
    }

    userService.signup(requestDto);

    return "post signup success";
  }
}
