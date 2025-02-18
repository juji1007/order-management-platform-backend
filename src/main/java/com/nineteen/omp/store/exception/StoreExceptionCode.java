package com.nineteen.omp.store.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreExceptionCode implements ExceptionCode {
  //가게 중복 등록?, 운영시간 등록?(시간 값 오류)
  STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."),
  STORE_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 가게 입니다."),
  STORE_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않은 카테고리 입니다."),
  STORE_CATEGORY_IS_NULL(HttpStatus.BAD_REQUEST, "스토어 카테고리가 null입니다."),
  STORE_NAME_IS_NULL(HttpStatus.BAD_REQUEST, "스토어 이름이 null입니다."),
  STORE_ADDRESS_IS_NULL(HttpStatus.BAD_REQUEST, "스토어 주소가 null입니다."),
  STORE_PHONE_IS_NULL(HttpStatus.BAD_REQUEST, "스토어 전화번호가 null입니다.");

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public HttpStatus getHttpStatus() {
    return this.httpStatus;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

}
