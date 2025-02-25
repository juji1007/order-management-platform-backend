package com.nineteen.omp.store.domain;

import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.store.exception.StoreException;
import com.nineteen.omp.store.exception.StoreExceptionCode;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_store")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_store SET is_deleted = true WHERE store_id = ?")
public class Store extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "store_id", updatable = false, nullable = false)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
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
  private String closedDays;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "status", updatable = true, nullable = false)
  private StoreStatus status;


  @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
  private List<StoreProduct> storeProducts = new ArrayList<>();

  @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
  private List<Order> orders = new ArrayList<>();

  public void changeStoreCategory(String storeCategoryName) {
    if (storeCategoryName == null) {
      throw new StoreException(StoreExceptionCode.STORE_CATEGORY_IS_NULL);
    }
    StoreCategory validatedStoreCategory = StoreCategory.fromName(storeCategoryName);
    this.storeCategory = validatedStoreCategory;
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

  public void approveStoreStatus() {
    this.status = status.OPEN;
  }
}
