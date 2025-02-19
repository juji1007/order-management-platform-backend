package com.nineteen.omp.payment.service.dto;

import com.nineteen.omp.payment.domain.Payment;
import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PaymentStatus;
import com.nineteen.omp.payment.domain.PgProvider;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetPaymentResponseCommand(
    UUID paymentId,
    UUID orderId,
    PaymentStatus status,
    PaymentMethod paymentMethod,
    PgProvider pgProvider,
    Integer paymentAmount,
    Integer discountAmount,
    LocalDateTime paymentDate
) {

  public GetPaymentResponseCommand(
      Payment payment
  ) {
    this(
        payment.getId(),
        payment.getOrderId(),
        payment.getStatus(),
        payment.getMethod(),
        payment.getPgProvider(),
        payment.getTotalAmount(),
        payment.getDiscountAmount(),
        payment.getUpdatedAt()
    );
  }

}
