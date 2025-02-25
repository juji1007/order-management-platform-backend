package com.nineteen.omp.order.controller.dto;

import java.util.UUID;

public record OrderProductRequestDto(
    UUID storeProductId,
    int quantity,
    int pricePerItem
) {

}
