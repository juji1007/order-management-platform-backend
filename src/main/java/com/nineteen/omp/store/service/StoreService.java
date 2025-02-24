package com.nineteen.omp.store.service;

import com.nineteen.omp.store.controller.dto.StoreResponseDto;
import com.nineteen.omp.store.service.dto.StoreCommand;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {


  StoreResponseDto createStore(StoreCommand storeCommand);

  Page<StoreResponseDto> searchStores(
      String name,
      String categoryName,
      String address,
      Pageable pageable);

  StoreResponseDto getStore(UUID storeId);

  StoreResponseDto updateStore(UUID storeId, StoreCommand storeCommand);

  void deleteStore(UUID storeId);

  StoreResponseDto approveStore(UUID storeId);
}
