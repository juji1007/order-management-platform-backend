package com.nineteen.omp.payment.service.dto;

import org.springframework.data.domain.Page;

public record GetPaymentListResponseCommand(
    Page<GetPaymentResponseCommand> getPaymentResponseCommandPage
) {

}
