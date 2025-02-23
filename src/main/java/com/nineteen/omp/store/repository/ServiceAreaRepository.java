package com.nineteen.omp.store.repository;

import com.nineteen.omp.store.controller.dto.ServiceAreaResponseDto;
import com.nineteen.omp.store.domain.ServiceArea;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceAreaRepository extends JpaRepository<ServiceArea, UUID> {

  boolean existsByAreaIdAndStoreId(UUID areaId, UUID storeId);

  boolean existsByStoreId(UUID storeId);

  List<ServiceArea> findAllByStoreId(UUID storeId);

  Optional<ServiceArea> findByAreaIdAndStoreId(UUID areaId, UUID storeId);

  Page<ServiceAreaResponseDto> getStoresByAreaId(UUID areaId, Pageable pageable);

}
