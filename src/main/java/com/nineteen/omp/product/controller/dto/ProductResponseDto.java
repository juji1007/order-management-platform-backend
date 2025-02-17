package com.nineteen.omp.product.controller.dto;


import java.util.UUID;

public record ProductResponseDto(

    UUID productId,
    String name,
    int price,
    String image,
    String description
) {

}