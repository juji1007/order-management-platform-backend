package com.nineteen.omp.order.controller.dto;


import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDto(
    UUID storeId,
    List<OrderProductRequestDto> orderProducts
) {

}
