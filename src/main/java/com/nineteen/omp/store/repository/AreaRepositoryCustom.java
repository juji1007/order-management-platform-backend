package com.nineteen.omp.store.repository;

import com.nineteen.omp.store.service.dto.AreaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AreaRepositoryCustom {

  Page<AreaResponseDto> searchAreas(String keyword, Pageable pageable);

}
