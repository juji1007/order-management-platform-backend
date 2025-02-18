package com.nineteen.omp.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonExceptionCode implements ExceptionCode {

  INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 매개변수입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 문제가 발생되었습니다."),
  PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 페이지를 찾을 수 없습니다."),
  DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "중복된 사용자가 존재합니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

  ;

  private final HttpStatus httpStatus;
  private final String message;
}
