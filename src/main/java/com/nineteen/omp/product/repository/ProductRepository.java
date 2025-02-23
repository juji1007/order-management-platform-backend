package com.nineteen.omp.product.repository;

import com.nineteen.omp.product.domain.StoreProduct;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<StoreProduct, UUID>,
    ProductQueryRepository {

}