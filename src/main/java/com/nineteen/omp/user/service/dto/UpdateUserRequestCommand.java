package com.nineteen.omp.user.service.dto;

import com.nineteen.omp.user.controller.dto.UpdateUserRequestDto;

public record UpdateUserRequestCommand(
    String nickname,
    String email,
    Boolean is_public,
    String delivery_address
) {

  public UpdateUserRequestCommand(
      UpdateUserRequestDto requestDto
  ) {
    this(
        requestDto.nickname(),
        requestDto.email(),
        requestDto.is_public(),
        requestDto.delivery_address()
    );
  }
}
