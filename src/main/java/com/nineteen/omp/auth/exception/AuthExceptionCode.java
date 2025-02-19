package com.nineteen.omp.auth.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {

  NOF_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh 토큰이 존재하지 않습니다."),
  GENERATE_TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 생성 중 오류가 발생하였습니다."),
  NOF_FOUND_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "Access 토큰이 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
