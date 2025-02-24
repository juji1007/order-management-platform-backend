package com.nineteen.omp.payment.controller;

import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.coupon.exception.UserCouponException;
import com.nineteen.omp.coupon.exception.UserCouponExceptionCode;
import com.nineteen.omp.coupon.repository.UserCouponRepository;
import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.exception.OrderException;
import com.nineteen.omp.order.exception.OrderExceptionCode;
import com.nineteen.omp.order.repository.OrderRepository;
import com.nineteen.omp.payment.controller.dto.CreatePaymentRequestDto;
import com.nineteen.omp.payment.controller.dto.GetPaymentListResponseDto;
import com.nineteen.omp.payment.controller.dto.GetPaymentResponseDto;
import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PgProvider;
import com.nineteen.omp.payment.service.PaymentService;
import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentListResponseCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;
  private final OrderRepository orderRepository;
  private final UserCouponRepository userCouponRepository;

  @PostMapping
  public ResponseEntity<ResponseDto<?>> createPayment(
      @RequestBody @Valid CreatePaymentRequestDto request
  ) {

    // 주문 조회
    Order order = orderRepository.findById(request.orderId())
        .orElseThrow(() -> new OrderException(OrderExceptionCode.ORDER_NOT_FOUND));
    // 사용자 쿠폰 조회
    UserCoupon userCoupon = userCouponRepository.findById(request.userCouponId())
        .orElseThrow(() -> new UserCouponException(UserCouponExceptionCode.USER_COUPON_NOT_FOUND));

    // 결제 생성
    var requestCommand = CreatePaymentRequestCommand.builder()
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

  @GetMapping
  public ResponseEntity<ResponseDto<?>> getPaymentByOrderId(
      @RequestParam("orderId") UUID orderId
  ) {
    var responseCommand = paymentService.getPaymentByOrderId(orderId);
    var response = new GetPaymentResponseDto(responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @GetMapping
  public ResponseEntity<ResponseDto<?>> getPaymentListByUserId(
      @RequestParam(
          name = "userId",
          required = false,
          defaultValue = ""
      ) Long userId,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    /*
     * Master 가 아니면 본인 결제 내역만 조회 가능
     * Master 라면 UserId를 파라미터로 받아 해당 유저의 결제 내역 조회
     */
    PageableUtils.validatePageable(pageable);
    var responseCommand = paymentService.getPaymentListByUserId(userId, pageable);
    var response = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @GetMapping("/all")
  public ResponseEntity<ResponseDto<?>> getPaymentListByUserId(
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    // Master만 전체 결제 내역 조회 가능
    PageableUtils.validatePageable(pageable);
    var responseCommand = paymentService.getPaymentList(pageable);
    var response = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  private static GetPaymentListResponseDto convertCommandToDto(
      Pageable pageable,
      GetPaymentListResponseCommand responseCommand
  ) {
    var responseDtoList = responseCommand.getPaymentResponseCommandPage().stream()
        .map(GetPaymentResponseDto::new)
        .toList();
    var responseDtoPage = new PageImpl<>(
        responseDtoList,
        pageable,
        responseCommand.getPaymentResponseCommandPage().getTotalElements()
    );
    return new GetPaymentListResponseDto(responseDtoPage);
  }
}
