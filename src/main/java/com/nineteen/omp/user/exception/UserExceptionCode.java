package com.nineteen.omp.user.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ExceptionCode {

  DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "중복된 사용자가 존재합니다."),
  DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일이 존재합니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
