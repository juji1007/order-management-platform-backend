package com.nineteen.omp.store.repository;

import static com.nineteen.omp.store.domain.QStore.store;

import com.nineteen.omp.store.controller.dto.StoreResponseDto;
import com.nineteen.omp.store.domain.Store;
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
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<StoreResponseDto> searchStores(
      String name,
      String categoryName,
      String address,
      Pageable pageable) {
    List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

    //searchDto -> 조건 검색
    //keyword -> 통합 검색(이름)
    QueryResults<Store> results = queryFactory
        .selectFrom(store)
        .where(
            nameContains(name),
            categoryNameContains(categoryName),
            addressContains(address)
        )
        .orderBy(orders.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<StoreResponseDto> content = results.getResults().stream()
        .map(StoreResponseDto::toResponseDto)
        .collect(Collectors.toList());
    long total = results.getTotal();
    return new PageImpl<>(content, pageable, total);
  }

  private BooleanExpression nameContains(String name) {
    if (name == null || name.isEmpty()) {
      return null;
    }
    return store.name.containsIgnoreCase(name);
  }

  private BooleanExpression categoryNameContains(String categoryName) {
    if (categoryName == null || categoryName.isEmpty()) {
      return null;
    }
    return store.storeCategory.stringValue().eq(categoryName);
  }

  private BooleanExpression addressContains(String address) {
    if (address == null || address.isEmpty()) {
      return null;
    }
    return store.address.containsIgnoreCase(address);
  }

  private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    if (pageable.getSort() != null) {
      for (Sort.Order sortOrder : pageable.getSort()) {
        com.querydsl.core.types.Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

        switch (sortOrder.getProperty()) {
          case "name":
            orders.add(new OrderSpecifier<>(direction, store.name));
            break;
          case "address":
            orders.add(new OrderSpecifier<>(direction, store.address));
            break;
          case "phone":
            orders.add(new OrderSpecifier<>(direction, store.phone));
            break;
          case "openHours":
            orders.add(new OrderSpecifier<>(direction, store.openHours));
            break;
          case "closedHours":
            orders.add(new OrderSpecifier<>(direction, store.closeHours));
            break;
          case "closedDays":
            orders.add(new OrderSpecifier<>(direction, store.closedDays));
            break;
          case "createdAt":
            orders.add(new OrderSpecifier<>(direction, store.createdAt));
            break;
          case "updatedAt":
            orders.add(new OrderSpecifier<>(direction, store.updatedAt));
            break;
          default:
            break;
        }

      }
    }
    return orders;
  }
}
