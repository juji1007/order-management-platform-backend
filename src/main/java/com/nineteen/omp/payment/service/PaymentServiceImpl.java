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

    paymentRepository.save(newPayment);

    // PG 사에 결제 요청 (추가 구현 필요)
  }

  @Override
  @Transactional
  public void cancelPayment(UUID paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
    payment.cancel();
  }

  @Override
  public GetPaymentResponseCommand getPaymentByOrderId(UUID orderId) {
    Payment payment = paymentRepository.findByOrder_Id(orderId)
        .orElseThrow(() -> new CustomException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
    return new GetPaymentResponseCommand(payment);
  }

  private int calculateTotalAmount(Order order, UserCoupon userCoupon) {
    int totalAmount = order.getTotalAmount();

    if (userCoupon != null) {
      totalAmount = userCoupon.useCoupon(totalAmount);
    }
    return totalAmount;
  }

  @Override
  public GetPaymentListResponseCommand getPaymentListByUserId(Long userId, Pageable pageable) {
    Page<Payment> payments = paymentRepository.findByOrder_User_Id(userId, pageable);
    List<GetPaymentResponseCommand> contents = payments.stream()
        .map(GetPaymentResponseCommand::new)
        .toList();
    PageImpl<GetPaymentResponseCommand> responseCommands =
        new PageImpl<>(contents, pageable, payments.getTotalElements());
    return new GetPaymentListResponseCommand(responseCommands);
  }

  @Override
  public GetPaymentListResponseCommand getPaymentList(Pageable pageable) {
    Page<Payment> payments = paymentRepository.findAll(pageable);
    List<GetPaymentResponseCommand> contents = payments.stream()
        .map(GetPaymentResponseCommand::new)
        .toList();
    PageImpl<GetPaymentResponseCommand> responseCommands =
        new PageImpl<>(contents, pageable, payments.getTotalElements());
    return new GetPaymentListResponseCommand(responseCommands);
  }

}
