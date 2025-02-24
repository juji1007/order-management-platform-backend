package com.nineteen.omp.order.repository;

import com.nineteen.omp.order.controller.dto.OrderResponseDto;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.domain.QOrder;
import com.nineteen.omp.store.domain.QStore;
import com.nineteen.omp.user.domain.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

  private final JPAQueryFactory queryFactory;


  @Override
  public Page<OrderResponseDto> searchOrdersByKeyword(String keyword, Pageable pageable) {
    QOrder order = QOrder.order;
    QUser user = QUser.user;
    QStore store = QStore.store;

    BooleanBuilder builder = new BooleanBuilder();

    if (keyword != null && !keyword.isEmpty()) {
      builder.or(user.username.containsIgnoreCase(keyword));
      builder.or(store.name.containsIgnoreCase(keyword));
    }

    List<Order> orderList = queryFactory
        .selectFrom(order)
        .leftJoin(order.user, user).fetchJoin()
        .leftJoin(order.store, store).fetchJoin()
        .where(builder)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = Optional.ofNullable(
        queryFactory
            .select(order.count())
            .from(order)
            .leftJoin(order.user, user)
            .leftJoin(order.store, store)
            .where(builder)
            .fetchOne()
    ).orElse(0L);

    return new PageImpl<>(
        orderList.stream()
            .map(o -> new OrderResponseDto(o, o.getOrderProducts()))
            .toList(),
        pageable,
        total
    );
  }
}