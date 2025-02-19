package com.nineteen.omp.store.exception;

import com.nineteen.omp.global.exception.CustomException;

public class ServiceAreaException extends CustomException {

  public ServiceAreaException(ServiceAreaExceptionCode exceptionCode) {
    super(exceptionCode);
  }
}
