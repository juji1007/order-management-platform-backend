package com.nineteen.omp.payment.service.dto;

import com.nineteen.omp.payment.domain.Payment;

public record ExecutePaymentRequestCommand(
    int totalAmount
) {

  public ExecutePaymentRequestCommand(
      Payment payment
  ) {
    this(
        payment.getTotalAmount()
    );
  }
}
