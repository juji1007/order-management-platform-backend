package com.nineteen.omp.store.service;

import com.nineteen.omp.store.controller.dto.ServiceAreaRequestDto;
import com.nineteen.omp.store.controller.dto.ServiceAreaResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceAreaService {

  void deleteServiceArea(UUID areaId, UUID storeId);

  Page<ServiceAreaResponseDto> getStoresByAreaId(UUID areaId, Pageable validatedPageable);

  List<ServiceAreaResponseDto> getAreasByStoreId(UUID storeId);

  ServiceAreaResponseDto createServiceArea(ServiceAreaRequestDto serviceAreaRequestDto);
}