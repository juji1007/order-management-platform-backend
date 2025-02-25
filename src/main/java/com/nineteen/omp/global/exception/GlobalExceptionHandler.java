package com.nineteen.omp.global.exception;

import com.nineteen.omp.global.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handleCustomException(CustomException e) {
    ExceptionCode exceptionCode = e.getException();
    return handleExceptionInternal(exceptionCode);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
    log.warn("handleIllegalArgument", e);
    ExceptionCode exceptionCode = CommonExceptionCode.INVALID_PARAMETER;
    return handleExceptionInternal(e.getMessage(), exceptionCode);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleAllException(Exception ex) {
    log.warn("handleAllException", ex);
    ExceptionCode exceptionCode = CommonExceptionCode.INTERNAL_SERVER_ERROR;
    return handleExceptionInternal(ex.getMessage(), exceptionCode);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
  ) {

    log.info("MethodArgumentNotValidException 발생", ex);

    // 첫 번째 발생한 오류 메시지를 가져오기
    String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();

    // 커스텀 에러 응답 생성
    return ResponseEntity.badRequest()
        .body(ResponseDto.exception(errorMessage, "INVALID_PARAMETER"));
  }


  private ResponseEntity<?> handleExceptionInternal(ExceptionCode exceptionCode) {
    return ResponseEntity.status(exceptionCode.getHttpStatus())
        .body(makeErrorResponse(exceptionCode));
  }

  private ResponseDto<?> makeErrorResponse(ExceptionCode exceptionCode) {
    return ResponseDto.exception(exceptionCode.getMessage(), exceptionCode);
  }

  private ResponseEntity<?> handleExceptionInternal(String message, ExceptionCode exceptionCode) {
    return ResponseEntity.status(exceptionCode.getHttpStatus())
        .body(makeErrorResponse(message, exceptionCode));
  }

  private ResponseDto<?> makeErrorResponse(String message, ExceptionCode exceptionCode) {
    return ResponseDto.exception(message, exceptionCode);
  }

}
