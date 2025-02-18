package com.nineteen.omp.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequestDto(
    @NotBlank
    @Pattern(
        regexp = "^[a-z0-9]{4,10}$",
        message = "아이디는 영문 소문자와 숫자 4~10자리여야 합니다."
    )
    String username,

    @NotBlank
    @Pattern(
        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,15}",
        message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 15자의 비밀번호여야 합니다."
    )
    String password
) {

}
