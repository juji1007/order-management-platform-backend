package com.nineteen.omp.store.repository;

import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.repository.dto.StoreSearchDto;
import com.nineteen.omp.store.service.dto.StoreResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.nineteen.omp.store.domain.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<StoreResponseDto> searchStores(String keyword, StoreSearchDto searchDto,
      Pageable pageable) {
    List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

    //keyword값 이미 searchDto에서 다 받음 -> 프론트에서 각 필드마다 키워드 넣는 검색
    //keyword값을 이용하기 위해 -> 프론트에서 키워드 1개 검색, 조건에 선택하는 걸로?
    QueryResults<Store> results = queryFactory
        .selectFrom(store)
        .where(
            keywordContains(keyword),
            nameContains(searchDto.name()),
            addressContains(searchDto.address()),
            phoneContains(searchDto.phone()),
            openDuring(searchDto.openHours(), searchDto.closedHours()),
            closedDaysContains(searchDto.closedDays())
        )
        .orderBy(orders.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    List<StoreResponseDto> content = results.getResults().stream()
        .map(store -> new StoreResponseDto(
            store.getId(),
            store.getName(),
            store.getAddress(),
            store.getPhone(),
            store.getOpenHours(),
            store.getCloseHours(),
            store.getClosedDays()
        ))
        .collect(Collectors.toList());
    long total = results.getTotal();

    return new PageImpl<>(content, pageable, total);
  }

  //키워드 값은 이름, 주소 포괄 검색
  private BooleanExpression keywordContains(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
      return null;
    }
    return store.name.contains(keyword)
        .or(store.address.contains(keyword));
  }

  private BooleanExpression nameContains(String name) {
    return name != null ? store.name.containsIgnoreCase(name) : null;
  }

  private BooleanExpression addressContains(String address) {
    return address != null ? store.address.containsIgnoreCase(address) : null;
  }

  private BooleanExpression phoneContains(String phone) {
    return phone != null ? store.phone.containsIgnoreCase(phone) : null;
  }

  //휴무일 검색
  private BooleanExpression closedDaysContains(String closedDays) {
    return closedDays != null ? store.closedDays.containsIgnoreCase(closedDays) : null;
  }

  private BooleanExpression openDuring(LocalTime openHours, LocalTime closedHours) {
    if (openHours != null && closedHours != null) {
      return store.openHours.goe(openHours).and(store.closeHours.loe(closedHours));
    } else if (openHours != null) {
      return store.openHours.goe(openHours);
    } else if (closedHours != null) {
      return store.closeHours.loe(closedHours);
    } else {
      return null;
    }
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
          default:
            break;
        }

        //운영시간 검색시 휴무일 가게들은 우선순위 밑으로
        if ("openHours".equals(sortOrder.getProperty()) || "closedHours".equals(
            sortOrder.getProperty())) {
          orders.add(new OrderSpecifier<>(Order.ASC, store.closedDays.isNull()));
        }
      }
    }
    return orders;
  }
}
