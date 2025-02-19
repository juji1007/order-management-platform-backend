package com.nineteen.omp.payment.domain;


import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.order.domain.Order;
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
@SQLDelete(sql = "UPDATE p_payment SET is_deleted = true WHERE payment_id = ?")
public class Payment {

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

}