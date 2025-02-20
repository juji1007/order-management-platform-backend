package com.nineteen.omp.order.domain.emuns;

public enum OrderStatus {
  CREATED,        // 주문 생성
  CONFIRMED,      // 주문 확인
  PREPARING,      // 배송 준비 중
  SHIPPING,       // 배송 중
  DELIVERED,      // 배송 완료
  CANCELLED       // 주문 취소
}
