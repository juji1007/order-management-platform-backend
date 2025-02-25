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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_store_product")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_store_product SET is_deleted = true WHERE id = ?")
public class StoreProduct extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Column(nullable = false, length = 20)
  private String name;

  @Column(nullable = false)
  private Integer price;

  @Column(length = 100)
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