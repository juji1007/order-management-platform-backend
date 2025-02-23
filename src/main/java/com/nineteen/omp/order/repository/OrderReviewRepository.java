package com.nineteen.omp.order.repository;

import com.nineteen.omp.order.domain.OrderReview;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderReviewRepository extends JpaRepository<OrderReview, UUID> {

}
