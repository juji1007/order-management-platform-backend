package com.nineteen.omp.payment.controller;

import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.coupon.service.UserCouponService;
import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.service.OrderService;
import com.nineteen.omp.payment.controller.dto.CreatePaymentRequestDto;
import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PgProvider;
import com.nineteen.omp.payment.service.PaymentService;
import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;
  private final OrderService orderService;
  private final UserCouponService userCouponService;

  @PostMapping
  public ResponseEntity<ResponseDto<?>> createPayment(
      @RequestBody CreatePaymentRequestDto request
  ) {

    // 주문 조회
    Order order = orderService.getOrder(request.orderId());
    // 사용자 쿠폰 조회
    UserCoupon userCoupon = userCouponService.getUserCoupon(request.userCouponId());

    // 결제 생성
    CreatePaymentRequestCommand requestCommand = CreatePaymentRequestCommand.builder()
        .order(order)
        .userCoupon(userCoupon)
        .pgProvider(PgProvider.valueOf(request.pgProvider()))
        .paymentMethod(PaymentMethod.valueOf(request.paymentMethod()))
        .build();
    paymentService.createPayment(requestCommand);

    return ResponseEntity.ok(ResponseDto.success());
  }

  @PatchMapping("/{paymentId}")
  public ResponseEntity<ResponseDto<?>> cancelPayment(
      @PathVariable("paymentId") UUID paymentId
  ) {
    paymentService.cancelPayment(paymentId);
    return ResponseEntity.ok(ResponseDto.success());
  }
}
