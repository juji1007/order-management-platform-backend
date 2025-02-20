package com.nineteen.omp.auth.jwt.enums;

import lombok.Getter;

@Getter
public enum JwtClaims {

  ROLE("role"),
  USER_ID("user_id"),
  ;

  private final String key;

  JwtClaims(String key) {
    this.key = key;
  }
}
