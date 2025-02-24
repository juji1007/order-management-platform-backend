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
import com.nineteen.omp.payment.exception.PaymentExceptionCode;
import com.nineteen.omp.payment.repository.PaymentRepository;
import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import com.nineteen.omp.user.domain.User;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

  @InjectMocks
  private PaymentServiceImpl paymentService;

  @Mock
  private PaymentRepository paymentRepository;

  @Test
  @DisplayName("결제 둥록 테스트")
  void createPayment() {
    // given
    final Order order = mock(Order.class);
    final UserCoupon userCoupon = mock(UserCoupon.class);
    final Integer totalAmount = 1000;
    final Integer discountAmount = 100;

    when(order.getTotalPrice()).thenReturn(totalAmount);
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

    @Nested
    @DisplayName("사용자가 결제 취소를 요청하면")
    class Context_WhenUserRequestCancel {

      private final Long userId = 1L;
      private final UUID paymentId = UUID.randomUUID();

      private Order getOrder() {
        User user = mock(User.class);
        Order order = mock(Order.class);
        when(user.getId()).thenReturn(userId);
        when(order.getUser()).thenReturn(user);
        return order;
      }

      @Test
      @DisplayName("결제 취소 요청 성공")
      void cancelPayment() {
        // given
        Order order = getOrder();
        final Payment payment = Payment.builder()
            .id(paymentId)
            .order(order)
            .paymentSuccessTime(LocalTime.now())
            .status(PaymentStatus.SUCCESS)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // when
        paymentService.cancelPaymentRequest(userId, paymentId);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCEL_REQUEST);
      }

      @Test
      @DisplayName("결제 취소 요청 실패 - 결제 정보 없음")
      void cancelPayment_NotFoundPayment() {
        // given
        final UUID paymentId = UUID.randomUUID();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> {
          paymentService.cancelPaymentRequest(userId, paymentId);
        });
      }

      @Test
      @DisplayName("결제 취소 요청 실패 - 결제 상태가 성공이 아님")
      void cancelPayment_NotSuccess() {
        // given
        Order order = getOrder();
        final UUID paymentId = UUID.randomUUID();
        final Payment payment = Payment.builder()
            .id(paymentId)
            .order(order)
            .status(PaymentStatus.PENDING)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // when & then
        assertThrows(CustomException.class, () -> {
          paymentService.cancelPaymentRequest(userId, paymentId);
          ;
        }).getMessage().equals(PaymentExceptionCode.NOT_VALID_CANCEL_REQUEST.getMessage());
      }

      @Test
      @DisplayName("결제 취소 요청 실패 - 취소 가능 시간 5분 초과")
      void cancelPayment_OverTime() {
        // given
        Order order = getOrder();
        final UUID paymentId = UUID.randomUUID();
        final Payment payment = Payment.builder()
            .id(paymentId)
            .order(order)
            .status(PaymentStatus.SUCCESS)
            .paymentSuccessTime(LocalTime.now().minusMinutes(6))
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // when & then
        assertThrows(CustomException.class, () -> {
          paymentService.cancelPaymentRequest(userId, paymentId);
        });
      }
    }

    @Nested
    @DisplayName("결제 취소 요청이 거부되면")
    class Context_WhenCancelRequestDenied {

      @Test
      @DisplayName("결제 요청 거부 성곻")
      void cancelPayment() {
        // given
        final UUID paymentId = UUID.randomUUID();
        final Payment payment = Payment.builder()
            .id(paymentId)
            .status(PaymentStatus.CANCEL_REQUEST)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // when
        paymentService.cancelPaymentRequestDenied(paymentId);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCEL_DENIED);
      }

      @Test
      @DisplayName("결제 요청 거부 실패 - 결제 정보 없음")
      void cancelPayment_NotFoundPayment() {
        // given
        final UUID paymentId = UUID.randomUUID();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> {
          paymentService.cancelPaymentRequestDenied(paymentId);
        });
      }
    }


    @Nested
    @DisplayName("결제 취소 요청이 승인되면")
    class Context_WhenCancelRequestApproved {

      @Test
      @DisplayName("결제 요청 승인 성공")
      void cancelPayment() {
        // given
        final UUID paymentId = UUID.randomUUID();
        final Payment payment = Payment.builder()
            .id(paymentId)
            .status(PaymentStatus.CANCEL_REQUEST)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // when
        paymentService.cancelPayment(paymentId);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
      }

      @Test
      @DisplayName("결제 요청 승인 실패 - 결제 정보 없음")
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

  @Nested
  @DisplayName("결제 상세 조회 테스트")
  class GetPayment {

    @Test
    @DisplayName("결제 조회 성공")
    void getPaymentById() {
      // given
      final UUID paymentId = UUID.randomUUID();
      final Payment payment = Payment.builder()
          .id(paymentId)
          .status(PaymentStatus.PENDING)
          .build();

      when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

      // when
      final var result = paymentService.getPaymentById(paymentId);

      // then
      assertThat(result.paymentId()).isEqualTo(paymentId);
    }

    @Test
    @DisplayName("결제 조회 실패 - 결제 정보 없음")
    void getPaymentById_NotFoundPayment() {
      // given
      final UUID paymentId = UUID.randomUUID();

      when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

      // when & then
      assertThrows(CustomException.class, () -> {
        paymentService.getPaymentById(paymentId);
      });
    }
  }

  @Nested
  @DisplayName("결제 목록 조회")
  class GetPayments {

    private Pageable pageable;
    private Page<Payment> payments;

    @BeforeEach
    void setUp() {
      pageable = PageRequest.of(0, 10);
      Payment payment = Payment.builder()
          .id(UUID.randomUUID())
          .status(PaymentStatus.SUCCESS)
          .method(PaymentMethod.CREDIT_CARD)
          .pgProvider(PgProvider.MOCK_PAY)
          .totalAmount(1000)
          .paymentSuccessTime(LocalTime.now())
          .pgTid("pgTid")
          .build();
      ArrayList<Payment> content = new ArrayList<>(List.of(new Payment[]{
          payment, payment, payment
      }));
      payments = new PageImpl<>(content, pageable, content.size());
    }

    @Test
    @DisplayName("사용자별 결제 목록 조회")
    void getPaymentsByUser() {
      // given
      final Long userId = 1L;

      when(paymentRepository.findByOrder_User_Id(userId, pageable))
          .thenReturn(payments);

      // when
      var response = paymentService.getUsersPaymentList(userId, pageable);

      // then
      verify(paymentRepository).findByOrder_User_Id(userId, pageable);
      assertThat(response.getPaymentResponseCommandPage().getContent())
          .isNotEmpty();
    }

    @Test
    @DisplayName("매장별 결제 목록 조회")
    void getPaymentsByStore() {
      // given
      final UUID storeId = UUID.randomUUID();

      when(paymentRepository.findByOrder_Store_Id(storeId, pageable))
          .thenReturn(payments);

      // when
      var response = paymentService.getStoresPaymentList(storeId, pageable);

      // then
      verify(paymentRepository).findByOrder_Store_Id(storeId, pageable);
      assertThat(response.getPaymentResponseCommandPage().getContent())
          .isNotEmpty();
    }

    @Nested
    @DisplayName("결제 목록 검색")
    class SearchPayments {

      @Test
      @DisplayName("사용자 닉네임으로 결제 목록 검색")
      void getPaymentsByUserNickname() {
        // given
        final String nickname = "nickname";

        when(paymentRepository.findByOrder_User_Nickname(nickname, pageable))
            .thenReturn(payments);

        // when
        var response = paymentService.searchPaymentListByUserNickname(nickname, pageable);

        // then
        verify(paymentRepository).findByOrder_User_Nickname(nickname, pageable);
        assertThat(response.getPaymentResponseCommandPage().getContent())
            .isNotEmpty();
      }

      @Test
      @DisplayName("매장 이름으로 결제 목록 검색")
      void getPaymentsByStoreName() {
        // given
        final String storeName = "storeName";

        when(paymentRepository.findByOrder_Store_Name(storeName, pageable))
            .thenReturn(payments);

        // when
        var response = paymentService.searchPaymentListByStoreName(storeName, pageable);

        // then
        verify(paymentRepository).findByOrder_Store_Name(storeName, pageable);
        assertThat(response.getPaymentResponseCommandPage().getContent())
            .isNotEmpty();
      }
    }


  }
}
