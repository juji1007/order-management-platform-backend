package com.nineteen.omp.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequestDto(
    Long id,  // UUID

    @NotBlank
    @Pattern(
        regexp = "^[a-z0-9]{4,10}$",
        message = "아이디는 영문 소문자와 숫자 4~10자리여야 합니다."
    )
    String username, // 아이디

    @NotBlank
    @Pattern(
        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,15}",
        message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 15자의 비밀번호여야 합니다."
    )
    String password, // 비밀번호

    @NotBlank
    String nickname, // 닉네임

    @NotBlank
    String role, // 역할

    @NotBlank
    @Email
    String email, // 이메일

    Boolean is_public, // 공개여부

    @NotBlank
    String delivery_address // 배달주소
) {

  // role 기본값 설정 ("USER")
  public SignupRequestDto {
    if (role == null) {
      role = "USER";
    }

    // is_public 기본값 설정(true)
    if (is_public == null) {
      is_public = true;  // 기본값 true
    }
  }
}

