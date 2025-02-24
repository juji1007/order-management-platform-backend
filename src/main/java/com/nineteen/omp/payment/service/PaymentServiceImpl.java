package com.nineteen.omp.payment.service;

import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.payment.domain.Payment;
import com.nineteen.omp.payment.domain.PaymentStatus;
import com.nineteen.omp.payment.exception.PaymentExceptionCode;
import com.nineteen.omp.payment.repository.PaymentRepository;
import com.nineteen.omp.payment.service.dto.CreatePaymentRequestCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentListResponseCommand;
import com.nineteen.omp.payment.service.dto.GetPaymentResponseCommand;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    // PG 사에 결제 요청 (추가 구현 필요)

    newPayment.success();

    paymentRepository.save(newPayment);
  }

  @Override
  @Transactional
  public void cancelPayment(UUID paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
    payment.cancelForce();
  }

  @Override
  public GetPaymentResponseCommand getPaymentById(UUID paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
    return new GetPaymentResponseCommand(payment);
  }

  private int calculateTotalAmount(Order order, UserCoupon userCoupon) {
    int totalAmount = order.getTotalPrice();

    if (userCoupon != null) {
      totalAmount = userCoupon.useCoupon(totalAmount);
    }
    return totalAmount;
  }

  @Override
  public GetPaymentListResponseCommand getUsersPaymentList(Long userId, Pageable pageable) {
    Page<Payment> payments = paymentRepository.findByOrder_User_Id(userId, pageable);
    return convertGetPaymentListResponseCommand(payments);
  }

  @Override
  public GetPaymentListResponseCommand getStoresPaymentList(UUID storeId, Pageable pageable) {
    Page<Payment> payments = paymentRepository.findByOrder_Store_Id(storeId, pageable);
    return convertGetPaymentListResponseCommand(payments);
  }

  @Override
  public void isOwnersPayment(Long ownerId, UUID paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
    if (!payment.getOrder().getStore().getUser().getId().equals(ownerId)) {
      throw new CustomException(PaymentExceptionCode.NOT_OWNER_PAYMENT);
    }
  }

  @Override
  @Transactional
  public void cancelPaymentRequest(Long userId, UUID paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
    if (!payment.getOrder().getUser().getId().equals(userId)) {
      throw new CustomException(PaymentExceptionCode.NOT_USER_PAYMENT);
    }
    payment.cancelRequest();
  }

  @Override
  @Transactional
  public void cancelPaymentRequestDenied(UUID paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
    if (!payment.isCancelRequest()) {
      throw new CustomException(PaymentExceptionCode.NOT_VALID_CANCEL_REQUEST);
    }
    payment.cancelRequestDenied();
  }

  @Override
  public void isUsersPayment(Long userId, UUID paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
    if (!payment.getOrder().getUser().getId().equals(userId)) {
      throw new CustomException(PaymentExceptionCode.NOT_USER_PAYMENT);
    }
  }

  @Override
  public GetPaymentListResponseCommand searchPaymentListByUserNickname(
      String nickname,
      Pageable pageable
  ) {
    Page<Payment> payments = paymentRepository.findByOrder_User_Nickname(nickname, pageable);
    return convertGetPaymentListResponseCommand(payments);
  }

  @Override
  public GetPaymentListResponseCommand searchPaymentListByStoreName(
      String storeName,
      Pageable pageable
  ) {
    Page<Payment> payments = paymentRepository.findByOrder_Store_Name(storeName, pageable);
    return convertGetPaymentListResponseCommand(payments);
  }

  private static GetPaymentListResponseCommand convertGetPaymentListResponseCommand(
      Page<Payment> payments
  ) {
    List<GetPaymentResponseCommand> contents = payments.stream()
        .map(GetPaymentResponseCommand::new)
        .toList();
    PageImpl<GetPaymentResponseCommand> responseCommands =
        new PageImpl<>(contents, payments.getPageable(), payments.getTotalElements());
    return new GetPaymentListResponseCommand(responseCommands);
  }

}
