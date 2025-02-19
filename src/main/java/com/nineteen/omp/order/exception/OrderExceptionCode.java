package com.nineteen.omp.order.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCode implements ExceptionCode {

  ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
