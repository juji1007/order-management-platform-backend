package com.nineteen.omp.order.repository;

import com.nineteen.omp.order.controller.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderQueryRepository {

  Page<OrderResponseDto> searchOrdersByKeyword(String keyword, Pageable pageable);
}