package com.nineteen.omp.order.repository;

import com.nineteen.omp.order.domain.Delivery;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

}
