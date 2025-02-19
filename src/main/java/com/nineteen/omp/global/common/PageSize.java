package com.nineteen.omp.global.common;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PageSize {
  DEFAULT(10),
  THIRTY(30),
  FIFTY(50)
  ;

  private final int size;

  PageSize(int size) {
    this.size = size;
  }

  public static boolean isValidSize(int size) {
    return Arrays.stream(values())
        .anyMatch(p -> p.getSize() == size);
  }
}
