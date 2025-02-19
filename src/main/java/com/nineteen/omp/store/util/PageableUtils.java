package com.nineteen.omp.store.util;

import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

  public static Pageable createPageable(int page, int size, String sortBy, boolean isAsc) {
    validatePageAndSize(page, size);
    Sort sort = isAsc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    return PageRequest.of(page - 1, size, sort);
  }

  private static void validatePageAndSize(int page, int size) {
    if (page < 1 || size < 1) {
      throw new CustomException(CommonExceptionCode.INVALID_PARAMETER);
    }
  }

}

