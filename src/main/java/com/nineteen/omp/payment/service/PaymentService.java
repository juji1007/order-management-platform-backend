package com.nineteen.omp.payment.service;

import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;

public interface PaymentService {

  void createPayment(CreatePaymentRequestCommand createPaymentRequestCommand);
}
