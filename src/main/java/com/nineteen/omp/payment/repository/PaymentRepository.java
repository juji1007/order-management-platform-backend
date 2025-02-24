package com.nineteen.omp.payment.repository;

import com.nineteen.omp.payment.domain.Payment;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

  @EntityGraph(attributePaths = {"order", "order.user"})
  Page<Payment> findByOrder_User_Id(Long userId, Pageable pageable);

  @EntityGraph(attributePaths = {"order", "order.store"})
  Page<Payment> findByOrder_Store_Id(UUID storeId, Pageable pageable);

  @EntityGraph(attributePaths = {"order", "order.user"})
  Page<Payment> findByOrder_User_Nickname(String nickname, Pageable pageable);

  @EntityGraph(attributePaths = {"order", "order.store"})
  Page<Payment> findByOrder_Store_Name(String storeName, Pageable pageable);
}
