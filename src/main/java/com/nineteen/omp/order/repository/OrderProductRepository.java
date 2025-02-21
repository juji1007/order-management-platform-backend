package com.nineteen.omp.order.repository;


import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.domain.OrderProduct;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {

  List<OrderProduct> findByOrder(Order orderId);
}
