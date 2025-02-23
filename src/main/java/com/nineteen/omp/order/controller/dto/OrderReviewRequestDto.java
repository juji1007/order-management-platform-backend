package com.nineteen.omp.order.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record OrderReviewRequestDto(
    @NotNull(message = "주문 아이디는 필수 입력값 입니다.")
    UUID orderId,

    @NotBlank(message = "주문 리뷰 내용은 필수 입력값 입니다.")
    @Size(max = 100, message = "주문 리뷰 내용은 최대 100자까지 입력 가능합니다.")
    String content,

    @NotNull(message = "주문 별점은 필수 입력값 입니다.")
    @Max(5)
    @Min(1)
    Integer rating
) {

}
