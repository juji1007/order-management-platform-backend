package com.nineteen.omp.order.repository;

import com.nineteen.omp.order.controller.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderQueryRepository {

  // 커스텀 쿼리 메서드를 정의할 수 있습니다.
  Page<OrderResponseDto> searchOrdersByKeyword(String keyword, Pageable pageable);
}