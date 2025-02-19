package com.nineteen.omp.store.domain;

import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.store.exception.AreaException;
import com.nineteen.omp.store.exception.AreaExceptionCode;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_area")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_area SET is_deleted = true WHERE area_id = ?")
public class Area extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "area_id", nullable = false)
  private UUID id;

  @Column(name = "si", nullable = false)
  private String si;

  @Column(name = "gu", nullable = false)
  private String gu;

  @Column(name = "dong", nullable = false)
  private String dong;

  public void changeNameSi(String si) {
    if (si == null) {
      throw new AreaException(AreaExceptionCode.AREA_SI_IS_NULL);
    }
    this.si = si;
  }

  public void changeNameGu(String gu) {
    if (gu == null) {
      throw new AreaException(AreaExceptionCode.AREA_GU_IS_NULL);
    }
    this.gu = gu;
  }

  public void changeNameDong(String dong) {
    if (dong == null) {
      throw new AreaException(AreaExceptionCode.AREA_DONG_IS_NULL);
    }
    this.dong = dong;
  }
}