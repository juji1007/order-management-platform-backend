package com.nineteen.omp.coupon.domain;


import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.user.domain.User;
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
public class UserCoupon extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  public int useCoupon(int totalAmount) {
    super.softDelete();
    return totalAmount - coupon.getDiscountAmount();
  }

  public Integer getDiscountAmount() {
    return coupon.getDiscountAmount();
  }
}