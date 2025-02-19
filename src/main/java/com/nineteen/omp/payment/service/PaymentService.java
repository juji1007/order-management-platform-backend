package com.nineteen.omp.payment.service;

import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentResponseCommand;
import java.util.UUID;

public interface PaymentService {

  void createPayment(CreatePaymentRequestCommand createPaymentRequestCommand);

  void cancelPayment(UUID paymentId);

  GetPaymentResponseCommand getPaymentByOrderId(UUID orderId);
}
