package com.nineteen.omp.payment.controller;

import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.coupon.exception.UserCouponException;
import com.nineteen.omp.coupon.exception.UserCouponExceptionCode;
import com.nineteen.omp.coupon.repository.UserCouponRepository;
import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.exception.CustomException;
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
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.exception.StoreExceptionCode;
import com.nineteen.omp.store.repository.StoreRepository;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  private final StoreRepository storeRepository;

  @PreAuthorize("permitAll()")
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

  @PreAuthorize("hasAnyRole('MASTER', 'OWNER')")
  @PatchMapping("/{paymentId}/cancel")
  public ResponseEntity<ResponseDto<?>> cancelPayment(
      @PathVariable("paymentId") UUID paymentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    if (userDetails.isOwner()) {
      paymentService.isOwnersPayment(userDetails.getUserId(), paymentId);
    }
    paymentService.cancelPayment(paymentId);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @PreAuthorize("hasAnyRole('MASTER', 'OWNER')")
  @PatchMapping("/{paymentId}/cancel/denied")
  public ResponseEntity<ResponseDto<?>> cancelPaymentRequestDenied(
      @PathVariable("paymentId") UUID paymentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    if (userDetails.isOwner()) {
      paymentService.isOwnersPayment(userDetails.getUserId(), paymentId);
    }
    paymentService.cancelPaymentRequestDenied(paymentId);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @Secured("USER")
  @PatchMapping("/{paymentId}/cancel/request")
  public ResponseEntity<ResponseDto<?>> cancelPaymentRequest(
      @PathVariable("paymentId") UUID paymentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    paymentService.cancelPaymentRequest(userDetails.getUserId(), paymentId);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @PreAuthorize("permitAll()")
  @GetMapping("/{paymentId}")
  public ResponseEntity<ResponseDto<?>> getPaymentDetail(
      @PathVariable("paymentId") UUID paymentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    if (userDetails.isUser()) {
      paymentService.isUsersPayment(userDetails.getUserId(), paymentId);
    } else if (userDetails.isOwner()) {
      paymentService.isOwnersPayment(userDetails.getUserId(), paymentId);
    }
    var responseCommand = paymentService.getPaymentById(paymentId);
    var response = new GetPaymentResponseDto(responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @Secured("MASTER")
  @GetMapping("/search/by/user")
  public ResponseEntity<ResponseDto<?>> searchPaymentListByUserId(
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
    PageableUtils.validatePageable(pageable);

    var responseCommand = paymentService.getUsersPaymentList(userId, pageable);
    var response = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @Secured("MASTER")
  @GetMapping("/search/by/store")
  public ResponseEntity<ResponseDto<?>> searchPaymentListByStoreId(
      @RequestParam(
          name = "storeId",
          required = false,
          defaultValue = ""
      ) UUID storeId,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    PageableUtils.validatePageable(pageable);

    var responseCommand = paymentService.getStoresPaymentList(storeId, pageable);
    var response = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @Secured("MASTER")
  @GetMapping("/search/by/user-nickname")
  public ResponseEntity<ResponseDto<?>> searchPaymentListByUserNickname(
      @RequestParam(
          name = "nickname",
          required = false,
          defaultValue = ""
      ) String nickname,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    PageableUtils.validatePageable(pageable);

    var responseCommand = paymentService.searchPaymentListByUserNickname(nickname, pageable);
    var response = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @Secured("MASTER")
  @GetMapping("/search/by/store-name")
  public ResponseEntity<ResponseDto<?>> searchPaymentListByStoreName(
      @RequestParam(
          name = "storeName",
          required = false,
          defaultValue = ""
      ) String storeName,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    PageableUtils.validatePageable(pageable);

    var responseCommand = paymentService.searchPaymentListByStoreName(storeName, pageable);
    var response = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @Secured("USER")
  @GetMapping("/search/my/users")
  public ResponseEntity<ResponseDto<?>> getUsersPaymentList(
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    PageableUtils.validatePageable(pageable);

    var responseCommand = paymentService.getUsersPaymentList(userDetails.getUserId(), pageable);
    var response = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @Secured("OWNER")
  @GetMapping("/search/my/stores")
  public ResponseEntity<ResponseDto<?>> getStoresPaymentList(
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    PageableUtils.validatePageable(pageable);

    Store store = storeRepository.findByUser_Id(userDetails.getUserId())
        .orElseThrow(() -> new CustomException(StoreExceptionCode.STORE_NOT_FOUND));

    var responseCommand = paymentService.getStoresPaymentList(store.getId(), pageable);
    var response = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  private GetPaymentListResponseDto convertCommandToDto(
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
