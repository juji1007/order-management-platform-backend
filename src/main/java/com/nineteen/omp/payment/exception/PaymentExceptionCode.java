package com.nineteen.omp.payment.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionCode implements ExceptionCode {

  NOT_FOUND_PAYMENT(HttpStatus.BAD_REQUEST, "결제 정보를 찾을 수 없습니다."),
  NOT_OWNER_PAYMENT(HttpStatus.BAD_REQUEST, "해당 주문의 가게 주인이 아닙니다."),
  NOT_USER_PAYMENT(HttpStatus.BAD_REQUEST, "해당 주문의 유저가 아닙니다."),
  NOT_VALID_CANCEL_REQUEST(HttpStatus.BAD_REQUEST, "취소 요청이 유효하지 않습니다."),
  OVER_TIME_CANCEL_REQUEST(HttpStatus.BAD_REQUEST, "취소 요청이 시간을 초과하였습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
