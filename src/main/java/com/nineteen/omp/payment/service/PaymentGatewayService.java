package com.nineteen.omp.payment.service;

import com.nineteen.omp.payment.service.dto.ExecutePaymentRequestCommand;
import com.nineteen.omp.payment.service.dto.ExecutePaymentResponseCommand;

public interface PaymentGatewayService {

  ExecutePaymentResponseCommand executePayment(ExecutePaymentRequestCommand command);
}
