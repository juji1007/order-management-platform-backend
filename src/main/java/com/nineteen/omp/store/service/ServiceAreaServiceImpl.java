package com.nineteen.omp.store.service;

import static com.nineteen.omp.store.controller.dto.ServiceAreaResponseDto.toResponseDto;

import com.nineteen.omp.store.controller.dto.ServiceAreaRequestDto;
import com.nineteen.omp.store.controller.dto.ServiceAreaResponseDto;
import com.nineteen.omp.store.domain.Area;
import com.nineteen.omp.store.domain.ServiceArea;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.exception.AreaException;
import com.nineteen.omp.store.exception.AreaExceptionCode;
import com.nineteen.omp.store.exception.ServiceAreaException;
import com.nineteen.omp.store.exception.ServiceAreaExceptionCode;
import com.nineteen.omp.store.exception.StoreException;
import com.nineteen.omp.store.exception.StoreExceptionCode;
import com.nineteen.omp.store.repository.AreaRepository;
import com.nineteen.omp.store.repository.ServiceAreaRepository;
import com.nineteen.omp.store.repository.StoreRepository;
import com.nineteen.omp.store.repository.dto.ServiceAreaData;
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
@Transactional(readOnly = true)
public class ServiceAreaServiceImpl implements ServiceAreaService {

  private final ServiceAreaRepository serviceAreaRepository;
  private final AreaRepository areaRepository;
  private final StoreRepository storeRepository;

  @Transactional
  public ServiceAreaResponseDto createServiceArea(ServiceAreaRequestDto serviceAreaRequestDto) {

    Area area = areaRepository.findById(serviceAreaRequestDto.areaId())
        .orElseThrow(() -> new AreaException(AreaExceptionCode.AREA_NOT_FOUND));

    Store store = storeRepository.findById(serviceAreaRequestDto.storeId())
        .orElseThrow(() -> new StoreException(StoreExceptionCode.STORE_NOT_FOUND));

    boolean isExists = serviceAreaRepository.existsByAreaIdAndStoreId(
        area.getId(),
        store.getId());
    if (isExists) {
      throw new ServiceAreaException(ServiceAreaExceptionCode.SERVICE_AREA_IS_DUPLICATED);
    }

    ServiceAreaData serviceAreaData = new ServiceAreaData(area, store);
    ServiceArea savedServiceArea = serviceAreaRepository.save(serviceAreaData.toEntity());

    return toResponseDto(savedServiceArea);
  }

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
//        .map(serviceArea -> ServiceAreaResponseDto.toResponseDto(serviceArea))
        .map(ServiceAreaResponseDto::toResponseDto)
        .collect(Collectors.toList());
  }

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

}
