package com.nineteen.omp.global.utils;

import com.nineteen.omp.global.common.PageSize;
import com.nineteen.omp.global.common.PageSortBy;
import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

  private static final int FIRST_PAGE = 1;

  public static void validatePageable(Pageable pageable) {
    validatePageNumber(pageable.getPageNumber());
    validatePageSize(pageable.getPageSize());
    validateSortBy(pageable.getSort());

    pageable.previousOrFirst();
  }

  private static void validatePageNumber(int pageNumber) {
    if (pageNumber < FIRST_PAGE) {
      throw new CustomException(CommonExceptionCode.INVALID_PAGE_NUMBER);
    }
  }


  private static void validatePageSize(int pageSize) {
    if (!PageSize.isValidSize(pageSize)) {
      throw new CustomException(CommonExceptionCode.INVALID_PAGE_SIZE);
    }
  }

  private static void validateSortBy(Sort sort) {
    for (Sort.Order order : sort) {
      if (!PageSortBy.isValidSortBy(order.getProperty())) {
        throw new CustomException(CommonExceptionCode.INVALID_SORT_BY);
      }
    }
  }

}
