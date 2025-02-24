package com.nineteen.omp.fixture;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.coupon.domain.Coupon;
import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.domain.OrderProduct;
import com.nineteen.omp.order.domain.OrderReview;
import com.nineteen.omp.order.domain.emuns.OrderStatus;
import com.nineteen.omp.order.domain.emuns.OrderType;
import com.nineteen.omp.payment.domain.Payment;
import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PaymentStatus;
import com.nineteen.omp.payment.domain.PgProvider;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.store.domain.Area;
import com.nineteen.omp.store.domain.ServiceArea;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreCategory;
import com.nineteen.omp.store.domain.StoreStatus;
import com.nineteen.omp.user.domain.User;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FixtureFactory {

  private static Long seq = 1L;

  public static User getUser() {
    return User.builder()
        .username("username" + seq)
        .password("password")
        .nickname("nickname" + seq)
        .email("email")
        .delivery_address("address")
        .role(Role.USER)
        .build();
  }

  public static User getMaster() {
    return User.builder()
        .username("username" + seq)
        .password("password")
        .nickname("nickname" + seq)
        .email("email")
        .delivery_address("address")
        .role(Role.MASTER)
        .build();
  }

  public static User getOwner() {
    return User.builder()
        .username("username" + seq)
        .password("password")
        .nickname("nickname" + seq)
        .email("email")
        .delivery_address("address")
        .role(Role.OWNER)
        .build();
  }

  public static Coupon getCoupon() {
    return Coupon.builder()
        .name("coupon" + seq++)
        .discountPrice(1000)
        .expiration(LocalDateTime.now().plusDays(1))
        .build();
  }

  public static UserCoupon getUserCoupon() {
    return UserCoupon.builder()
        .coupon(getCoupon())
        .user(getUser())
        .status(true)
        .build();
  }

  public static UserCoupon getUserCoupon(User user, Coupon coupon) {
    return UserCoupon.builder()
        .coupon(coupon)
        .user(user)
        .status(true)
        .build();
  }

  public static Order getOrder() {
    return Order.builder()
        .store(getStore())
        .user(getUser())
        .totalPrice((int) (10000 + seq))
        .orderStatus(OrderStatus.CREATED)
        .orderType(OrderType.DELIVERY)
        .orderProducts(new ArrayList<>())
        .build();
  }

  public static Order getOrder(User user, Store store) {
    return Order.builder()
        .store(store)
        .user(user)
        .totalPrice((int) (10000 + seq))
        .orderStatus(OrderStatus.CREATED)
        .orderType(OrderType.DELIVERY)
        .orderProducts(List.of(getOrderProduct()))
        .build();
  }

  public static OrderProduct getOrderProduct() {
    return OrderProduct.builder()
        .order(getOrder())
        .storeProduct(getStoreProduct())
        .price(1000)
        .quantity(10)
        .build();
  }

  public static StoreProduct getStoreProduct() {
    return StoreProduct.builder()
        .store(getStore())
        .name("product" + seq++)
        .description("description")
        .image("image")
        .price(1000)
        .build();
  }

  public static Store getStore() {
    return Store.builder()
        .user(getUser())
        .status(StoreStatus.OPEN)
        .openHours(LocalTime.of(9, 0))
        .closeHours(LocalTime.of(21, 0))
        .closedDays("Sunday")
        .name("store" + seq++)
        .phone("phone")
        .build();
  }

  public static Store getStore(User user) {
    return Store.builder()
        .user(user)
        .status(StoreStatus.OPEN)
        .openHours(LocalTime.of(9, 0))
        .closeHours(LocalTime.of(21, 0))
        .closedDays("Sunday")
        .name("store" + seq++)
        .phone("phone")
        .address("address")
        .storeCategory(StoreCategory.KOREAN)
        .build();
  }

  public static OrderReview getOrderReview() {
    return OrderReview.builder()
        .order(getOrder())
        .content("content")
        .rating(5)
        .build();
  }

  public static Payment getPayment() {
    return Payment.builder()
        .order(getOrder())
        .status(PaymentStatus.SUCCESS)
        .method(PaymentMethod.CREDIT_CARD)
        .pgProvider(PgProvider.MOCK_PAY)
        .totalAmount(10000)
        .userCoupon(getUserCoupon())
        .pgTid("pgTid")
        .build();
  }

  public static Payment getPayment(Order order, UserCoupon userCoupon) {
    return Payment.builder()
        .order(order)
        .status(PaymentStatus.SUCCESS)
        .method(PaymentMethod.CREDIT_CARD)
        .pgProvider(PgProvider.MOCK_PAY)
        .totalAmount(10000)
        .userCoupon(userCoupon)
        .pgTid("pgTid" + seq++)
        .paymentSuccessTime(LocalTime.now())
        .build();
  }

  public static Area getArea() {
    return Area.builder()
        .si(seq++ + " si")
        .gu(seq + " gu")
        .dong(seq + " dong")
        .build();
  }

  public static ServiceArea getServiceArea() {
    return ServiceArea.builder()
        .area(getArea())
        .store(getStore())
        .build();
  }

}
