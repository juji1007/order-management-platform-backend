package com.nineteen.omp.global.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
@Getter
public abstract class BaseEntity {

  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private Long createdBy;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "deleted_by")
  private Long deletedBy;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @PreRemove
  protected void softDelete() {
    if (isDeleted != null && !isDeleted) {
      if (deletedAt == null) {
        deletedAt = LocalDateTime.now();
      }
      if (deletedBy == null) {
        deletedBy = updatedBy;
      }
      isDeleted = true;
    }
  }
}
