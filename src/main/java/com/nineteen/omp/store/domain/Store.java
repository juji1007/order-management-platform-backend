package com.nineteen.omp.store.domain;

import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.store.exception.StoreException;
import com.nineteen.omp.store.exception.StoreExceptionCode;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "store_id", updatable = false, nullable = false)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Convert(converter = StoreCategoryConverter.class)
  @Column(name = "category", updatable = true, nullable = false)
  private StoreCategory storeCategory;

  @Column(name = "name", updatable = true, nullable = false)
  private String name;
  @Column(name = "address", updatable = true, nullable = false)
  private String address;
  @Column(name = "phone", updatable = true, nullable = false)
  private String phone;

  @Column(name = "open_hours", updatable = true, nullable = true)
  private LocalTime openHours;
  @Column(name = "close_hours", updatable = true, nullable = true)
  private LocalTime closeHours;
  @Column(name = "closed_days", updatable = true, nullable = true)
  private String closedDays; // -> localDate 고려 리스트도 고려

  public void changeStoreCategory(StoreCategory storeCategory) {
    if (storeCategory == null) {
      throw new StoreException(StoreExceptionCode.STORE_CATEGORY_IS_NULL);
    }
    this.storeCategory = storeCategory;
  }

  public void changeStoreName(String name) {
    if (name == null) {
      throw new StoreException(StoreExceptionCode.STORE_NAME_IS_NULL);
    }
    this.name = name;
  }

  public void changeStoreAddress(String address) {
    if (address == null) {
      throw new StoreException(StoreExceptionCode.STORE_ADDRESS_IS_NULL);
    }
    this.address = address;
  }

  public void changeStoreOpenHours(LocalTime openHours) {
    this.openHours = openHours;
  }

  public void changeStoreCloseHours(LocalTime closeHours) {
    this.closeHours = closeHours;
  }

  public void changeStoreClosedDays(String closedDays) {
    this.closedDays = closedDays;
  }
}