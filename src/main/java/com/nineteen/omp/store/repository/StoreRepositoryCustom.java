package com.nineteen.omp.store.repository;

import com.nineteen.omp.store.repository.dto.StoreSearchDto;
import com.nineteen.omp.store.service.dto.StoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

  Page<StoreResponseDto> searchStores(String keyword, StoreSearchDto searchDto, Pageable pageable);
}
