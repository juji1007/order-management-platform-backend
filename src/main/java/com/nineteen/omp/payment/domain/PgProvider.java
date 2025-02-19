package com.nineteen.omp.payment.domain;

import lombok.Getter;

@Getter
public enum PgProvider {
  MOCK_PAY(0),
  ;

  private final int code;

  PgProvider(int code) {
    this.code = code;
  }
}
