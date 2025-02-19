package com.nineteen.omp.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.payment.domain.Payment;
import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PaymentStatus;
import com.nineteen.omp.payment.domain.PgProvider;
import com.nineteen.omp.payment.repository.PaymentRepository;
import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

  @Nested
  @DisplayName("결제 취소 테스트")
  class CancelPayment {

    @Test
    @DisplayName("결제 취소 성공")
    void cancelPayment() {
      // given
      final UUID paymentId = UUID.randomUUID();
      final Payment payment = Payment.builder()
          .id(paymentId)
          .status(PaymentStatus.PENDING)
          .build();

      when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

      // when
      paymentService.cancelPayment(paymentId);

      // then
      assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
    }

    @Test
    @DisplayName("결제 취소 실패 - 결제 정보 없음")
    void cancelPayment_NotFoundPayment() {
      // given
      final UUID paymentId = UUID.randomUUID();

      when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

      // when & then
      assertThrows(CustomException.class, () -> {
        paymentService.cancelPayment(paymentId);
      });
    }
  }

}