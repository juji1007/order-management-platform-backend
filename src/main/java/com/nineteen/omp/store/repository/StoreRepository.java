package com.nineteen.omp.store.repository;

import com.nineteen.omp.store.domain.Store;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

}
