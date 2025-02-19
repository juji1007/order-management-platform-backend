package com.nineteen.omp.store.exception;

import com.nineteen.omp.global.exception.CustomException;

public class AreaException extends CustomException {

  public AreaException(AreaExceptionCode exceptionCode) {
    super(exceptionCode);
  }
}
