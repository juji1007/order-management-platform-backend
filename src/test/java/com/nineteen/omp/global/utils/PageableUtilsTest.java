package com.nineteen.omp.global.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class PageableUtilsTest {

  @Test
  void validatePageable() {
    Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Order.asc("updatedAt")));

    pageable = pageable.previousOrFirst();

    assertThat(pageable.getPageNumber()).isEqualTo(0);
  }

}