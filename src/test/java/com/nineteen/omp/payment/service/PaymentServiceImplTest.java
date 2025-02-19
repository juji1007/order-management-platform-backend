package com.nineteen.omp.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
import com.nineteen.omp.payment.service.dto.GetPaymentListResponseCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentResponseCommand;
import com.nineteen.omp.user.domain.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

  @Nested
  @DisplayName("지정 주문의 결제 조회 테스트")
  class GetPaymentByOrderId {

    @Test
    @DisplayName("유저 ID로 결제 목록을 페이징 조회할 수 있다")
    void getPaymentListByUserId() {
      // given
      Long userId = 1L;
      Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

      User user = User.builder()
          .id(userId)
          .build();

      Order order = Order.builder()
          .id(UUID.randomUUID())
          .user(user)
          .build();

      List<Payment> payments = List.of(
          Payment.builder()
              .id(UUID.randomUUID())
              .order(order)
              .status(PaymentStatus.PENDING)
              .method(PaymentMethod.CREDIT_CARD)
              .pgProvider(PgProvider.MOCK_PAY)
              .totalAmount(10000)
              .build(),
          Payment.builder()
              .id(UUID.randomUUID())
              .order(order)
              .status(PaymentStatus.PENDING)
              .method(PaymentMethod.CREDIT_CARD)
              .pgProvider(PgProvider.MOCK_PAY)
              .totalAmount(20000)
              .build()
      );

      Page<Payment> paymentsPage = new PageImpl<>(payments, pageable, payments.size());

      when(paymentRepository.findByOrder_User_Id(userId, pageable))
          .thenReturn(paymentsPage);

      // when
      GetPaymentListResponseCommand result = paymentService.getPaymentListByUserId(userId, pageable);

      // then
      assertAll(
          () -> assertThat(result).isNotNull(),
          () -> assertThat(result.getPaymentResponseCommandPage().getContent())
              .hasSize(2)
              .satisfies(content -> {
                GetPaymentResponseCommand firstPayment = content.get(0);
                assertThat(firstPayment.paymentId()).isEqualTo(payments.get(0).getId());
                assertThat(firstPayment.orderId()).isEqualTo(order.getId());
                assertThat(firstPayment.status()).isEqualTo(PaymentStatus.PENDING);
                assertThat(firstPayment.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
                assertThat(firstPayment.pgProvider()).isEqualTo(PgProvider.MOCK_PAY);
                assertThat(firstPayment.paymentAmount()).isEqualTo(10000);
                assertThat(firstPayment.paymentDate()).isEqualTo(payments.get(0).getUpdatedAt());
              }),
          () -> assertThat(result.getPaymentResponseCommandPage().getTotalElements()).isEqualTo(2),
          () -> assertThat(result.getPaymentResponseCommandPage().getTotalPages()).isEqualTo(1),
          () -> assertThat(result.getPaymentResponseCommandPage().getNumber()).isEqualTo(0),
          () -> assertThat(result.getPaymentResponseCommandPage().getSize()).isEqualTo(10)
      );

      verify(paymentRepository).findByOrder_User_Id(userId, pageable);
    }

    @Test
    @DisplayName("결제 내역이 없는 경우 빈 페이지를 반환한다")
    void getPaymentListByUserId_EmptyList() {
      // given
      Long userId = 1L;
      Pageable pageable = PageRequest.of(0, 10);
      Page<Payment> emptyPage = new PageImpl<>(List.of(), pageable, 0);

      when(paymentRepository.findByOrder_User_Id(userId, pageable))
          .thenReturn(emptyPage);

      // when
      GetPaymentListResponseCommand result = paymentService.getPaymentListByUserId(userId, pageable);

      // then
      assertAll(
          () -> assertThat(result).isNotNull(),
          () -> assertThat(result.getPaymentResponseCommandPage().getContent()).isEmpty(),
          () -> assertThat(result.getPaymentResponseCommandPage().getTotalElements()).isZero(),
          () -> assertThat(result.getPaymentResponseCommandPage().getTotalPages()).isZero()
      );

      verify(paymentRepository).findByOrder_User_Id(userId, pageable);
    }
  }

}