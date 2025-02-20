package com.nineteen.omp.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
    @NotBlank
    @Size(max = 30)
    String nickname,

    @NotBlank
    @Email
    String email,

    @NotNull
    Boolean is_public,

    @NotBlank
    @Size(max = 30)
    String delivery_address
) {

}
