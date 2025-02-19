package com.nineteen.omp.payment.controller.dto;

public record CreatePaymentRequestDto(
    String orderId,
    String userCouponId,
    String pgProvider,
    String paymentMethod
) {

}
