package com.nineteen.omp.payment.service;

import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentListResponseCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentResponseCommand;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

  void createPayment(CreatePaymentRequestCommand createPaymentRequestCommand);

  void cancelPayment(UUID paymentId);

  GetPaymentResponseCommand getPaymentByOrderId(UUID orderId);

  GetPaymentListResponseCommand getPaymentListByUserId(Long userId, Pageable pageable);

  GetPaymentListResponseCommand getPaymentList(Pageable pageable);
}
