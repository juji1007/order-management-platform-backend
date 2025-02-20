package com.nineteen.omp.coupon.domain;

import com.nineteen.omp.user.domain.User;
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
@Table(name = "p_user_coupon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "user_coupon_id", updatable = false, nullable = false)
  private UUID id;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "user_id", nullable = false)
//  private User user;
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id", nullable = false)
  private Coupon coupon;

  @Column(name = "status", nullable = false)
  private boolean status;

  public void changeStatus(boolean status) {
    this.status = status;
  }
}