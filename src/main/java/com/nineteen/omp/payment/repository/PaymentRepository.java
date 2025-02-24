package com.nineteen.omp.payment.repository;

import com.nineteen.omp.payment.domain.Payment;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

  Optional<Payment> findByOrder_Id(UUID orderId);

  @EntityGraph(attributePaths = {"order", "order.user"})
  @BatchSize(size = 100)
  Page<Payment> findByOrder_User_Id(Long userId, Pageable pageable);
}
