package com.nineteen.omp.store.domain;


import com.nineteen.omp.category.domain.StoreCategory;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToOne
  @JoinColumn(name = "category_id")
  private StoreCategory storeCategory;

  private String storename; // TEMP: 임시로 추가된 필드
}