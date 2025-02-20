package com.nineteen.omp.product.domain;


import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.store.domain.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store_product")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProduct extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false)
  private Integer price;

  private String image;

  @Column(length = 500)
  private String description;

  public StoreProductBuilder toBuilder() {
    return StoreProduct.builder()
        .id(this.id)
        .store(this.store)
        .name(this.name)
        .price(this.price)
        .image(this.image)
        .description(this.description);
  }

}