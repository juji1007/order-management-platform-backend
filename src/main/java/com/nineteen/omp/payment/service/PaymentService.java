package com.nineteen.omp.payment.service;

import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentListResponseCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentResponseCommand;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

  void createPayment(CreatePaymentRequestCommand createPaymentRequestCommand);

  void cancelPayment(UUID paymentId);

  GetPaymentResponseCommand getPaymentById(UUID orderId);

  GetPaymentListResponseCommand getUsersPaymentList(Long userId, Pageable pageable);

  GetPaymentListResponseCommand getStoresPaymentList(UUID storeId, Pageable pageable);

  void isOwnersPayment(Long ownerId, UUID paymentId);

  void cancelPaymentRequest(Long userId, UUID paymentId);

  void cancelPaymentRequestDenied(UUID paymentId);

  void isUsersPayment(Long userId, UUID paymentId);

  GetPaymentListResponseCommand searchPaymentListByUserNickname(String nickname, Pageable pageable);

  GetPaymentListResponseCommand searchPaymentListByStoreName(String storeName, Pageable pageable);
}
