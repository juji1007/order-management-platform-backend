package com.nineteen.omp.product.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ProductExceptionCode implements ExceptionCode {
  STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "스토어를 찾을 수 없습니다."),
  PRODUCT_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "상품 저장에 실패했습니다."),
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다.");


  private final HttpStatus httpStatus;
  private final String message;
}