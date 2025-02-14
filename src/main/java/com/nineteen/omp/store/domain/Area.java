package com.nineteen.omp.store.domain;


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
public class Area {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
}