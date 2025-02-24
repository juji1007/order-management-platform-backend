package com.nineteen.omp.store.repository;

import static com.nineteen.omp.store.domain.QStore.store;
import static com.nineteen.omp.order.domain.QOrder.order;
import static com.nineteen.omp.order.domain.QOrderReview.orderReview;
import static com.nineteen.omp.order.domain.QOrderProduct.orderProduct;
import static com.nineteen.omp.store.domain.QServiceArea.serviceArea;
import static com.nineteen.omp.store.domain.QArea.area;
import static com.nineteen.omp.product.domain.QStoreProduct.storeProduct;

import com.nineteen.omp.store.controller.dto.QSearchStoreResponseDto;
import com.nineteen.omp.store.controller.dto.SearchStoreResponseDto;
import com.nineteen.omp.store.controller.dto.StoreResponseDto;
import com.nineteen.omp.store.domain.Store;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
  public Page<SearchStoreResponseDto> searchAdvancedStore(
      String productName,
      String storeName,
      String categoryName,
      int averageRating,
      Pageable pageable) {

    List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

    //검색 조건 : 상품이름, 가게이름, 카테고리 이름, 별점
//    //상품이름은 상품이랑 가게 조인 가능함, 별점은 오더랑 오더리뷰랑 조인
//    //상품이름, 가게이름, 카테고리이름은 booleanExpression 이용
//    //별점은 fetchjoin 써야함
    QueryResults<SearchStoreResponseDto> results = queryFactory
        .select(new QSearchStoreResponseDto(
            store.name,
            store.storeCategory.stringValue(),
            null,
            orderReview.rating.avg().intValue(),
            store.status.stringValue()
            ))
        .from(store)
        .join(storeProduct.store, store)
        .join(orderReview.order, order)
        .join(order.store, store)
        .where(
            storeProduct.name.containsIgnoreCase(productName),
            categoryNameContains(categoryName)
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
    return null;
  }

  @Override
  public Page<StoreResponseDto> searchStores(
      String name,
      String categoryName,
      String address,
      Pageable pageable) {
    List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

    QueryResults<Store> results = queryFactory
        .select(store)
        .from(store)
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
