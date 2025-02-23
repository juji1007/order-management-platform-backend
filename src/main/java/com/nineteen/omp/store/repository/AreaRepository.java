package com.nineteen.omp.store.repository;

import com.nineteen.omp.store.domain.Area;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, UUID>, AreaRepositoryCustom {

  boolean existsBySiAndGuAndDong(String si, String gu, String dong);

  void deleteById(UUID areaId);
}
