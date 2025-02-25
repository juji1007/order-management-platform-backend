package com.nineteen.omp.store.repository;

import com.nineteen.omp.store.domain.Store;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, UUID>, StoreRepositoryCustom {

  boolean existsByNameAndAddress(String storeName, String address);

  void deleteById(UUID id);

  Optional<Store> findByUser_Id(Long userId);

  @EntityGraph(attributePaths = {"storeProducts"})
  Optional<Store> findById(UUID storeId);


}
