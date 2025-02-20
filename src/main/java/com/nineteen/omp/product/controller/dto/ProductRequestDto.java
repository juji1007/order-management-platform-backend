package com.nineteen.omp.product.controller.dto;


import java.util.UUID;

public record ProductRequestDto(

    UUID storeId,
    String name,
    int price,
    String image,
    String description
) {

}