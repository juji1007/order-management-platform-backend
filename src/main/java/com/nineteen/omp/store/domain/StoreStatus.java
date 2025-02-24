package com.nineteen.omp.store.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreStatus {
  PENDING("등록 대기중"),
  OPEN("영업중"),
  CLOSED("폐업");

  private final String description;

}
