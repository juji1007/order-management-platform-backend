package com.nineteen.omp.user.service.dto;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.user.domain.User;

public record GetUserInfoResponseCommand(
    String username,
    String nickname,
    String email,
    Role role,
    String deliveryAddress
) {

  public GetUserInfoResponseCommand(
      User user
  ) {
    this(
        user.getUsername(),
        user.getNickname(),
        user.getEmail(),
        user.getRole(),
        user.getDelivery_address()
    );
  }

}
