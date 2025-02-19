package com.nineteen.omp.payment.controller.dto;

import org.springframework.data.domain.Page;

public record GetPaymentListResponseDto(
    Page<GetPaymentResponseDto> getPaymentListResponseDtoPage
) {

}
