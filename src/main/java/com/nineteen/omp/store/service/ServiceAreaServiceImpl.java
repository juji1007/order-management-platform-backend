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

@Service
@RequiredArgsConstructor
public class ServiceAreaServiceImpl implements ServiceAreaService {

  private final ServiceAreaRepository serviceAreaRepository;

  @Transactional
  public ServiceAreaResponseDto createServiceArea(ServiceAreaCommand serviceAreaCommand) {
    boolean isExists = serviceAreaRepository.existsByAreaIdAndStoreId(
        serviceAreaCommand.area().getId(),
        serviceAreaCommand.store().getId());
    if (isExists) {
      throw new ServiceAreaException(ServiceAreaExceptionCode.SERVICE_AREA_IS_DUPLICATED);
    }

    ServiceAreaData serviceAreaData = new ServiceAreaData(serviceAreaCommand.area(),
        serviceAreaCommand.store());

    ServiceArea savedServiceArea = serviceAreaRepository.save(serviceAreaData.toEntity());

    return toResponseDto(savedServiceArea);
  }

  @Transactional(readOnly = true)
  public List<ServiceAreaResponseDto> getAreasByStoreId(UUID storeId) {
    boolean isExists = serviceAreaRepository.existsByStoreId(storeId);
    if (!isExists) {
      throw new ServiceAreaException(ServiceAreaExceptionCode.SERVICE_AREA_NOT_FOUND);
    }

    List<ServiceArea> serviceAreas = serviceAreaRepository.findAllByStoreId(storeId);

    if (serviceAreas.isEmpty()) {
      throw new ServiceAreaException(ServiceAreaExceptionCode.SERVICE_AREA_NOT_FOUND);
    }

    return serviceAreas.stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Page<ServiceAreaResponseDto> getStoresByAreaId(UUID areaId, Pageable pageable) {
    return serviceAreaRepository.getStoresByAreaId(areaId, pageable);
  }

  @Transactional
  public void deleteServiceArea(UUID areaId, UUID storeId) {
    ServiceArea serviceArea = serviceAreaRepository.findByAreaIdAndStoreId(areaId, storeId)
        .orElseThrow(
            () -> new ServiceAreaException(ServiceAreaExceptionCode.SERVICE_AREA_NOT_FOUND));

    serviceAreaRepository.delete(serviceArea);
  }

  private ServiceAreaResponseDto toResponseDto(ServiceArea serviceArea) {
    return new ServiceAreaResponseDto(
        serviceArea.getId(),
        serviceArea.getArea().getId(),
        serviceArea.getStore().getId()
    );
  }

}
