package com.nineteen.omp.payment.service;

import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.payment.domain.Payment;
import com.nineteen.omp.payment.domain.PaymentStatus;
import com.nineteen.omp.payment.repository.PaymentRepository;
import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;

  @Override
  @Transactional
  public void createPayment(CreatePaymentRequestCommand createPaymentRequestCommand) {

    int totalAmount =
        calculateTotalAmount(createPaymentRequestCommand.order(),
            createPaymentRequestCommand.userCoupon());

    Payment newPayment = Payment.builder()
        .order(createPaymentRequestCommand.order())
        .userCoupon(createPaymentRequestCommand.userCoupon())
        .totalAmount(totalAmount)
        .pgProvider(createPaymentRequestCommand.pgProvider())
        .status(PaymentStatus.PENDING)
        .method(createPaymentRequestCommand.paymentMethod())
        .build();

    paymentRepository.save(newPayment);

    // PG 사에 결제 요청 (추가 구현 필요)
  }

  private int calculateTotalAmount(Order order, UserCoupon userCoupon) {
    int totalAmount = order.getTotalAmount();

    if (userCoupon != null) {
      totalAmount = userCoupon.useCoupon(totalAmount);
    }
    return totalAmount;
  }
}
