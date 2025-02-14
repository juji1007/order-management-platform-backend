package com.nineteen.omp.category.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store_category")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
}