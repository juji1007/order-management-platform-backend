package com.nineteen.omp.store.repository;

import com.nineteen.omp.store.controller.dto.StoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

  Page<StoreResponseDto> searchStores(
      String name,
      String categoryName,
      String address,
      Pageable pageable);
}
