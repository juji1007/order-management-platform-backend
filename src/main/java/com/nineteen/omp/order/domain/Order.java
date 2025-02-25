package com.nineteen.omp.order.domain;


import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.order.domain.emuns.OrderStatus;
import com.nineteen.omp.order.domain.emuns.OrderType;
import com.nineteen.omp.order.exception.OrderException;
import com.nineteen.omp.order.exception.OrderExceptionCode;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_order")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_order SET is_deleted = true WHERE id = ?")
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)

  @Column(name = "order_id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "store_id")
  private Store store;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  private int totalPrice;

  private String orderRequestMsg;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderProduct> orderProducts = new ArrayList<>();

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderReview> orderReviews = new ArrayList<>();

  @Builder
  public Order(Store store, User user, int totalPrice, OrderStatus orderStatus,
      OrderType orderType) {
    this.store = store;
    this.user = user;
    this.totalPrice = totalPrice;
    this.orderStatus = orderStatus;
    this.orderType = orderType;
  }

  public Order cancelOrder() {
    if (this.orderStatus == OrderStatus.CANCELLED) {
      throw new OrderException(OrderExceptionCode.ORDER_ALREADY_CANCELLED);
    }
    this.orderStatus = OrderStatus.CANCELLED;
    return this;
  }

  public void completeOrder(OrderType orderType, String orderRequestMsg) {
    if (this.orderStatus == OrderStatus.COMPLETED) {
      throw new OrderException(OrderExceptionCode.ORDER_ALREADY_COMPLETED);
    }
    this.orderStatus = OrderStatus.COMPLETED;
    this.orderType = orderType;
    this.orderRequestMsg = orderRequestMsg;
  }
}
