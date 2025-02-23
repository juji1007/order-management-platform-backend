package com.nineteen.omp.store.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StoreCategoryConverter implements AttributeConverter<StoreCategory, Integer> {

  @Override
  public Integer convertToDatabaseColumn(StoreCategory storeCategory) {
    if(storeCategory == null) {
      return null;
    }
    return storeCategory.getCategoryCode();
  }

  @Override
  public StoreCategory convertToEntityAttribute(Integer integer) {
    if(integer == null) {
      return null;
    }
    return StoreCategory.fromCode(integer);
  }
}
