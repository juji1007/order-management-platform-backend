package com.nineteen.omp.payment.domain;


import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.payment.exception.PaymentExceptionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_payment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_payment SET is_deleted = true WHERE id = ?")
public class Payment extends BaseEntity {

  private static final int CANCEL_TIME_LIMIT_MIN = 5;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "payment_id", updatable = false, nullable = false)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "order_id", updatable = false, nullable = false)
  private Order order;

  @OneToOne
  @JoinColumn(name = "user_coupon_id", nullable = true)
  private UserCoupon userCoupon;

  @Column(name = "payment_total_amount", nullable = false)
  private Integer totalAmount;

  @Column(name = "pg_provider", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private PgProvider pgProvider;

  @Column(name = "pg_tid", nullable = true)
  private String pgTid;

  @Column(name = "payment_status", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private PaymentStatus status;

  @Column(name = "payment_mothod", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private PaymentMethod method;

  @Column(name = "payment_success_time", nullable = false)
  private LocalTime paymentSuccessTime;

  public void cancelForce() {
    this.status = PaymentStatus.CANCELED;
  }

  public Integer getDiscountAmount() {
    return userCoupon == null ? 0 : userCoupon.getDiscountAmount();
  }

  public UUID getOrderId() {
    return order.getId();
  }

  public void success() {
    this.status = PaymentStatus.SUCCESS;
    this.paymentSuccessTime = LocalTime.now();
  }

  public void cancelRequest() {
    if (!status.equals(PaymentStatus.SUCCESS) && !status.equals(PaymentStatus.CANCEL_DENIED)) {
      throw new CustomException(PaymentExceptionCode.NOT_VALID_CANCEL_REQUEST);
    }
    if (LocalTime.now().isAfter(paymentSuccessTime.plusMinutes(CANCEL_TIME_LIMIT_MIN))) {
      throw new CustomException(PaymentExceptionCode.OVER_TIME_CANCEL_REQUEST);
    }
    this.status = PaymentStatus.CANCEL_REQUEST;
  }

  public void cancelRequestDenied() {
    this.status = PaymentStatus.CANCEL_DENIED;
  }

  public boolean isCancelRequest() {
    return status.equals(PaymentStatus.CANCEL_REQUEST);
  }
}