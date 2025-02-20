package com.nineteen.omp.payment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CreatePaymentRequestDto(
    @NotBlank UUID orderId,
    @NotBlank UUID userCouponId,
    @NotBlank String pgProvider,
    @NotBlank String paymentMethod
) {

}
