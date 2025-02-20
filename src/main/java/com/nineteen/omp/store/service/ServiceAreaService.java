package com.nineteen.omp.store.service;

import com.nineteen.omp.store.controller.dto.ServiceAreaResponseDto;
import com.nineteen.omp.store.domain.ServiceArea;
import com.nineteen.omp.store.exception.ServiceAreaException;
import com.nineteen.omp.store.exception.ServiceAreaExceptionCode;
import com.nineteen.omp.store.repository.ServiceAreaRepository;
import com.nineteen.omp.store.repository.dto.ServiceAreaData;
import com.nineteen.omp.store.service.dto.ServiceAreaCommand;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceAreaService {

  void deleteServiceArea(UUID areaId, UUID storeId);

  Page<ServiceAreaResponseDto> getStoresByAreaId(UUID areaId, Pageable validatedPageable);

  List<ServiceAreaResponseDto> getAreasByStoreId(UUID storeId);

  ServiceAreaResponseDto createServiceArea(ServiceAreaCommand serviceAreaCommand);
}
