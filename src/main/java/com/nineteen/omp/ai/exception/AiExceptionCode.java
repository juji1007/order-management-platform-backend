package com.nineteen.omp.ai.exception;

import com.nineteen.omp.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AiExceptionCode implements ExceptionCode {
  AI_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 요청이 실패했습니다."),
  AI_PARAM_IS_INVALID(HttpStatus.BAD_REQUEST, "질문을 넣어주세요.");

  private final HttpStatus httpStatus;
  private final String message;
}
