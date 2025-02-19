package com.nineteen.omp.payment.service;

import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import java.util.UUID;

public interface PaymentService {

  void createPayment(CreatePaymentRequestCommand createPaymentRequestCommand);

  void cancelPayment(UUID paymentId);
}
