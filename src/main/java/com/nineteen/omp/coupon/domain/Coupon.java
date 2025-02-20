package com.nineteen.omp.coupon.domain;


import com.nineteen.omp.coupon.exception.CouponException;
import com.nineteen.omp.coupon.exception.CouponExceptionCode;
import com.nineteen.omp.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_coupon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_coupon SET is_deleted = true WHERE coupon_id = ?")
public class Coupon extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "coupon_id", nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "discount_price", nullable = false)
  private int discountPrice;

  @Column(name = "expiration", nullable = false)
  private LocalDateTime expiration;


  public void changeName(String name) {
    if (name == null) {
      throw new CouponException(CouponExceptionCode.COUPON_NAME_IS_NULL);
    }
    this.name = name;
  }

  public void changeDiscountPrice(Integer discountPrice) {
    if (discountPrice == null) {
      throw new CouponException(CouponExceptionCode.COUPON_DISCOUNTPRICE_IS_NULL);
    }
    if (discountPrice <= 0) {
      throw new CouponException(CouponExceptionCode.COUPON_DISCOUNTPRICE_INVALID);
    }
    this.discountPrice = discountPrice;
  }

  public void changeExpiration(LocalDateTime expiration) {
    if (expiration == null) {
      throw new CouponException(CouponExceptionCode.COUPON_EXPIRATION_IS_NULL);
    }
    if (expiration.isBefore(LocalDateTime.now())) {
      throw new CouponException(CouponExceptionCode.COUPON_EXPIRATION_INVALID);
    }
    this.expiration = expiration;
  }
}