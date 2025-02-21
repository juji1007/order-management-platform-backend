package com.nineteen.omp.order.domain;


import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.order.domain.emuns.OrderStatus;
import com.nineteen.omp.order.domain.emuns.OrderType;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.user.domain.User;
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
  private UUID id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "store_id")
  private Store store;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  private int totalPrice;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  private List<OrderProduct> orderProducts;

  @Builder
  public Order(Store store, User user, int totalPrice, OrderStatus orderStatus,
      OrderType orderType) {
    this.store = store;
    this.user = user;
    this.totalPrice = totalPrice;
    this.orderStatus = orderStatus;
    this.orderType = orderType;
  }


  public void calculateTotalPrice(List<OrderProduct> orderProducts) {
    this.totalPrice = orderProducts.stream()
        .mapToInt(
            orderProduct -> orderProduct.getQuantity() * orderProduct.getStoreProduct().getPrice())
        .sum();
  }

  public Order cancelOrder() {
    if (this.orderStatus == OrderStatus.CANCELLED) {
      throw new IllegalStateException("Order is already cancelled");
    }
    this.orderStatus = OrderStatus.CANCELLED;
    return this;
  }
}
