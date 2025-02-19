package com.nineteen.omp.payment.controller.dto;

import java.util.UUID;

public record CreatePaymentRequestDto(
    UUID orderId,
    UUID userCouponId,
    String pgProvider,
    String paymentMethod
) {

}
