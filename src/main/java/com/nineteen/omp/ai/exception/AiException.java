package com.nineteen.omp.ai.exception;

import com.nineteen.omp.global.exception.CustomException;

public class AiException extends CustomException {

  public AiException(AiExceptionCode exceptionCode) {
    super(exceptionCode);
  }
}
