package com.nineteen.omp.store.exception;

import com.nineteen.omp.global.exception.CustomException;

public class StoreException extends CustomException {

  public StoreException(StoreExceptionCode exceptionCode) {
    super(exceptionCode);
  }
}
