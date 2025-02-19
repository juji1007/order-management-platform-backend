package com.nineteen.omp.user.domain;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ExceptionCode {

  DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "중복된 사용자가 존재합니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
