package com.nineteen.omp.store.domain;

import com.nineteen.omp.store.exception.StoreException;
import com.nineteen.omp.store.exception.StoreExceptionCode;
import lombok.Getter;

@Getter
public enum StoreCategory {
  KOREAN(0,"한식"),
  CHINESE(1,"중식"),
  JAPANESE(2,"일식"),
  WESTERN(3,"양식"),
  LATE_NIGHT(4,"야식"),
  SNACK(5,"분식");

  private final int categoryCode;
  private final String categoryName;

  StoreCategory(int categoryCode, String categoryName) {
    this.categoryCode = categoryCode;
    this.categoryName = categoryName;
  }

  public static StoreCategory fromCode(int code) {
    for (StoreCategory category : values()) {
      if (category.categoryCode == code) {
        return category;
      }
    }
    throw new StoreException(StoreExceptionCode.STORE_CATEGORY_NOT_FOUND);
  }
}
