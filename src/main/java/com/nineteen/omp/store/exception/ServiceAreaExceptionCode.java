package com.nineteen.omp.store.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServiceAreaExceptionCode implements ExceptionCode {
  SERVICE_AREA_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 등록된 운영 지역 입니다."),
  SERVICE_AREA_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 운영 지역을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
