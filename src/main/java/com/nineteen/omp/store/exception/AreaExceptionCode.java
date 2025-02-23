package com.nineteen.omp.store.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AreaExceptionCode implements ExceptionCode {
  AREA_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 지역 입니다."),
  AREA_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 지역을 찾을 수 없습니다."),
  AREA_DELETE_FAIL(HttpStatus.BAD_REQUEST, "지역 삭제에 실패하였습니다."),
  AREA_SI_IS_NULL(HttpStatus.BAD_REQUEST, "지역 시 이름이 NULL 입니다."),
  AREA_GU_IS_NULL(HttpStatus.BAD_REQUEST, "지역 구 이름이 NULL 입니다."),
  AREA_DONG_IS_NULL(HttpStatus.BAD_REQUEST, "지역 동 이름이 NULL 입니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
