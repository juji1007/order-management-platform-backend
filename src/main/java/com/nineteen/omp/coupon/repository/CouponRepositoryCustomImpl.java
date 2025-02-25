package com.nineteen.omp.coupon.repository;

import static com.nineteen.omp.coupon.domain.QCoupon.coupon;

import com.nineteen.omp.coupon.controller.dto.CouponResponseDto;
import com.nineteen.omp.coupon.domain.Coupon;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {

  private final JPAQueryFactory queryFactory;


  @Override
  public Page<CouponResponseDto> searchCoupons(String keyword, Pageable pageable) {
    List<OrderSpecifier<?>> orders = getAllOrderSpecifier(pageable);

    QueryResults<Coupon> results = queryFactory
        .selectFrom(coupon)
        .where(keywordContains(keyword))
        .orderBy(orders.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<CouponResponseDto> content = results.getResults().stream()
        .map(coupon -> new CouponResponseDto(
            coupon.getId(),
            coupon.getName(),
            coupon.getDiscountPrice(),
            coupon.getExpiration()
        ))
        .collect(Collectors.toList());
    long total = results.getTotal();
    return new PageImpl<>(content, pageable, total);
  }

  private BooleanExpression keywordContains(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
      return null;
    }
    return coupon.name.containsIgnoreCase(keyword);
  }

  private List<OrderSpecifier<?>> getAllOrderSpecifier(Pageable pageable) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();
    if (pageable.getSort() != null) {
      for (Sort.Order sortOrder : pageable.getSort()) {
        com.querydsl.core.types.Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

        switch (sortOrder.getProperty()) {
          case "name":
            orders.add(new OrderSpecifier<>(direction, coupon.name));
            break;
          case "discountPrice":
            orders.add(new OrderSpecifier<>(direction, coupon.discountPrice));
            break;
          case "expiration":
            orders.add(new OrderSpecifier<>(direction, coupon.expiration));
            break;
          case "createdAt":
            orders.add(new OrderSpecifier<>(direction, coupon.createdAt));
            break;
          case "updatedAt":
            orders.add(new OrderSpecifier<>(direction, coupon.updatedAt));
            break;
          default:
            break;
        }
      }
    }
    return orders;
  }


}
