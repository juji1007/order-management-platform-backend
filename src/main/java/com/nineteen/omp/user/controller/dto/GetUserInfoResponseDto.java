package com.nineteen.omp.user.controller.dto;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.user.service.dto.GetUserInfoResponseCommand;

public record GetUserInfoResponseDto(
    String username,
    String nickname,
    String email,
    Role role,
    boolean isPublic,
    String deliveryAddress
) {

  public GetUserInfoResponseDto(
      GetUserInfoResponseCommand command
  ) {
    this(
        command.username(),
        command.nickname(),
        command.email(),
        command.role(),
        command.isPublic(),
        command.deliveryAddress()
    );
  }
}
