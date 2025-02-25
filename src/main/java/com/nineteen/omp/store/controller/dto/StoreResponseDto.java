package com.nineteen.omp.store.controller.dto;

import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreStatus;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

//service -> controller
public record StoreResponseDto(
    UUID id,
    String categoryName,
    String name,
    String address,
    String phone,
    LocalTime openHours,
    LocalTime closeHours,
    String closedDays,
    StoreStatus storeStatus,
    List<ProductResponseDto> products
) {

  //toResponse
  public static StoreResponseDto toResponseDto(Store store) {
    List<StoreProduct> storeProducts = store.getStoreProducts();
    List<ProductResponseDto> productResponseDtos = storeProducts.stream()
        .map(ProductResponseDto::new)
        .toList();
    return new StoreResponseDto(
        store.getId(),
        store.getStoreCategory().getCategoryName(),
        store.getName(),
        store.getAddress(),
        store.getPhone(),
        store.getOpenHours(),
        store.getCloseHours(),
        store.getClosedDays(),
        store.getStatus(),
        productResponseDtos
    );
  }

}
