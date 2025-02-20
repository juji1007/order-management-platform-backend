package com.nineteen.omp.order.repository;


import com.nineteen.omp.order.domain.OrderProduct;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {

}
