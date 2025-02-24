package com.nineteen.omp.payment.service.dto;

import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PgProvider;
import lombok.Builder;

@Builder
public record CreatePaymentRequestCommand(
    Order order,
    UserCoupon userCoupon,
    PgProvider pgProvider,
    PaymentMethod paymentMethod
) {

}
