package com.nineteen.omp.payment.controller.dto;

import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PaymentStatus;
import com.nineteen.omp.payment.domain.PgProvider;
import com.nineteen.omp.payment.service.dto.GetPaymentResponseCommand;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetPaymentResponseDto(
    UUID paymentId,
    PaymentStatus status,
    PaymentMethod paymentMethod,
    PgProvider pgProvider,
    Integer paymentAmount,
    Integer discountAmount,
    LocalDateTime paymentDate
) {

  public GetPaymentResponseDto(
      GetPaymentResponseCommand command
  ) {
    this(
        command.paymentId(),
        command.status(),
        command.paymentMethod(),
        command.pgProvider(),
        command.paymentAmount(),
        command.discountAmount(),
        command.paymentDate()
    );
  }
}
