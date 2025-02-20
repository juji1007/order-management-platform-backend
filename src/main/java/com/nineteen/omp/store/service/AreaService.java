package com.nineteen.omp.store.service;

import com.nineteen.omp.store.controller.dto.AreaRequestDto;
import com.nineteen.omp.store.controller.dto.AreaResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AreaService {


  AreaResponseDto createArea(AreaRequestDto areaRequestDto);

  Page<AreaResponseDto> searchAreas(String keyword, Pageable pageable);

  AreaResponseDto getArea(UUID areaId);

  AreaResponseDto updateArea(UUID areaId, AreaRequestDto areaRequestDto);

  void deleteArea(UUID areaId);
}
