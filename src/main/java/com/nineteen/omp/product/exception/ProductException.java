package com.nineteen.omp.product.exception;

import com.nineteen.omp.global.exception.CustomException;

public class ProductException extends CustomException {

  public ProductException(ProductExceptionCode exceptionCode) {
    super(exceptionCode);

  }
}