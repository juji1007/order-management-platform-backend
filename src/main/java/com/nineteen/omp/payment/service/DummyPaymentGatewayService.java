package com.nineteen.omp.payment.service;

import com.nineteen.omp.payment.service.dto.ExecutePaymentRequestCommand;
import com.nineteen.omp.payment.service.dto.ExecutePaymentResponseCommand;
import org.springframework.stereotype.Component;

@Component
public class DummyPaymentGatewayService implements PaymentGatewayService {

  @Override
  public ExecutePaymentResponseCommand executePayment(ExecutePaymentRequestCommand command) {
    return new ExecutePaymentResponseCommand("MOCK_PG_TID");
  }
}
