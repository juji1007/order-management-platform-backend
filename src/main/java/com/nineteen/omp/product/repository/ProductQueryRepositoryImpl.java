package com.nineteen.omp.product.repository;

import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.QStoreProduct;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.store.domain.QStore;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Page<ProductResponseDto> searchProducts(String keyword, String category,
      Pageable pageable) {

    QStoreProduct storeProduct = QStoreProduct.storeProduct;
    QStore store = QStore.store;

    BooleanExpression predicate = storeProduct.isDeleted.isFalse();

    if (StringUtils.hasText(category)) {
      predicate = predicate.and(storeProduct.store.storeCategory.eq(StoreCategory.valueOf(category)));
    }

    if (StringUtils.hasText(keyword)) {
      predicate = predicate.and(
          storeProduct.name.containsIgnoreCase(keyword));
    }

    JPAQuery<StoreProduct> query = new JPAQuery<>(entityManager)
        .select(storeProduct)
        .from(storeProduct)
        .leftJoin(storeProduct.store, store)
        .where(predicate);

    List<ProductResponseDto> content = query.offset(pageable.getOffset())
        .limit(pageable.getPageSize()).fetch().stream().map(ProductResponseDto::new)
        .collect(Collectors.toList());

    JPAQuery<Long> countQuery = new JPAQuery<>(entityManager)
        .select(storeProduct.count())
        .from(storeProduct)
        .leftJoin(storeProduct.store, store)
        .where(predicate);

    Optional<Long> totalCount = Optional.ofNullable(countQuery.fetchOne());
    long total = totalCount.orElse(0L);

    return new PageImpl<>(content, pageable, total);

  }
}