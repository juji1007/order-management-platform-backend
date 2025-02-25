package com.nineteen.omp.store.repository;

import static com.nineteen.omp.order.domain.QOrder.order;
import static com.nineteen.omp.order.domain.QOrderReview.orderReview;
import static com.nineteen.omp.product.domain.QStoreProduct.storeProduct;
import static com.nineteen.omp.store.domain.QStore.store;

import com.nineteen.omp.store.controller.dto.QSearchStoreResponseDto;
import com.nineteen.omp.store.controller.dto.SearchStoreResponseDto;
import com.nineteen.omp.store.controller.dto.StoreResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
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
    QueryResults<SearchStoreResponseDto> results = queryFactory
        .select(new QSearchStoreResponseDto(
            store.id,
            store.name,
            store.storeCategory.stringValue(),
            orderReview.rating.avg().doubleValue(),
            store.status.stringValue()
        ))
        .from(store)
        .join(store.storeProducts, storeProduct)
        .leftJoin(store.orders, order)
        .leftJoin(order.orderReviews, orderReview)
        .where(
            storeProductNameContains(productName),
            nameContains(storeName),
            categoryNameContains(categoryName)
        )
        .groupBy(store.id)
        .having(ratingContains(averageRating))
        .orderBy(orders.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

    Long total = results.getTotal();
    return new PageImpl<>(results.getResults(), pageable, total);
  }

  @Override
  public Page<StoreResponseDto> searchStores(
      String name,
      String categoryName,
      String address,
      Pageable pageable) {
//    List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);
//
//    QueryResults<Store> results = queryFactory
//        .select(store)
//        .from(store)
//        .where(
//            nameContains(name),
//            categoryNameContains(categoryName),
//            addressContains(address)
//        )
//        .orderBy(orders.toArray(new OrderSpecifier[0]))
//        .offset(pageable.getOffset())
//        .limit(pageable.getPageSize())
//        .fetchResults();
//
//    List<StoreResponseDto> content = results.getResults().stream()
//        .map(StoreResponseDto::toResponseDto)
//        .collect(Collectors.toList());
//    long total = results.getTotal();
//    return new PageImpl<>(content, pageable, total);
    return null;
  }

  private BooleanExpression storeProductNameContains(String productName) {
    if (productName == null || productName.isEmpty()) {
      return null;
    }
    return storeProduct.name.containsIgnoreCase(productName);
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

  private BooleanExpression ratingContains(int averageRating) {
    if (averageRating == 0) {
      return null;
    } else if (averageRating == 5) {
      return orderReview.rating.avg().between(averageRating, averageRating);
    }
    return orderReview.rating.avg().goe(averageRating)
        .and(orderReview.rating.avg().lt(averageRating + 1));
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
