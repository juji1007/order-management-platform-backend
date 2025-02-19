package com.nineteen.omp.payment.repository;

import com.nineteen.omp.payment.domain.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

  Optional<Payment> findByOrderId(UUID orderId);
}
