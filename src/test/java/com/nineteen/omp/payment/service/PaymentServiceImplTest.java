package com.nineteen.omp.payment.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.payment.domain.Payment;
import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PgProvider;
import com.nineteen.omp.payment.repository.PaymentRepository;
import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

  @InjectMocks
  private PaymentServiceImpl paymentService;

  @Mock
  private PaymentRepository paymentRepository;

  @Test
  @DisplayName("결제 생성 테스트")
  void createPayment() {
    // given
    final Order order = mock(Order.class);
    final UserCoupon userCoupon = mock(UserCoupon.class);
    final Integer totalAmount = 1000;
    final Integer discountAmount = 100;

    when(order.getTotalAmount()).thenReturn(totalAmount);
    when(userCoupon.useCoupon(anyInt())).thenReturn(totalAmount - discountAmount);

    CreatePaymentRequestCommand req = CreatePaymentRequestCommand.builder()
        .order(order)
        .userCoupon(userCoupon)
        .pgProvider(PgProvider.MOCK_PAY)
        .paymentMethod(PaymentMethod.CREDIT_CARD)
        .build();

    // when
    paymentService.createPayment(req);

    // then
    verify(paymentRepository).save(any(Payment.class));
  }
}