package com.nineteen.omp.store.repository;

import static com.nineteen.omp.store.domain.QArea.area;

import com.nineteen.omp.store.controller.dto.AreaResponseDto;
import com.nineteen.omp.store.domain.Area;
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
public class AreaRepositoryCustomImpl implements AreaRepositoryCustom {

  private final JPAQueryFactory queryFactory;


  @Override
  public Page<AreaResponseDto> searchAreas(String keyword, Pageable pageable) {

    List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

    QueryResults<Area> results = queryFactory
        .selectFrom(area)
        .where(
            keywordContains(keyword)
        )
        .orderBy(orders.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<AreaResponseDto> content = results.getResults().stream()
        .map(area -> new AreaResponseDto(
            area.getId(),
            area.getSi(),
            area.getGu(),
            area.getDong()
        ))
        .collect(Collectors.toList());
    long total = results.getTotal();

    return new PageImpl<>(content, pageable, total);
  }

  //키워드 값은 시, 구, 동 통합 검색
  private BooleanExpression keywordContains(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
      return null;
    }
    return area.si.containsIgnoreCase(keyword)
        .or(area.gu.containsIgnoreCase(keyword))
        .or(area.dong.containsIgnoreCase(keyword));
  }

  private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    if (pageable.getSort() != null) {
      for (Sort.Order sortOrder : pageable.getSort()) {
        com.querydsl.core.types.Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

        switch (sortOrder.getProperty()) {
          case "si":
            orders.add(new OrderSpecifier<>(direction, area.si));
            break;
          case "gu":
            orders.add(new OrderSpecifier<>(direction, area.gu));
            break;
          case "dong":
            orders.add(new OrderSpecifier<>(direction, area.dong));
            break;
          case "createdAt":
            orders.add(new OrderSpecifier<>(direction, area.createdAt));
            break;
          case "updatedAt":
            orders.add(new OrderSpecifier<>(direction, area.updatedAt));
            break;
          default:
            break;
        }

      }
    }
    return orders;
  }
}
