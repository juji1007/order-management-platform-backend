package com.nineteen.omp.store.domain;

import com.nineteen.omp.global.entity.BaseEntity;
import jakarta.persistence.Column;
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
@Table(name = "p_area")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Area extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "si", nullable = false)
  private String si;

  @Column(name = "gu", nullable = false)
  private String gu;

  @Column(name = "dong", nullable = false)
  private String dong;
}



