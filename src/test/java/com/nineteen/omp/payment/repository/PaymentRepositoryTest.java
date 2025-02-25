package com.nineteen.omp.payment.repository;

import static com.nineteen.omp.fixture.FixtureFactory.getCoupon;
import static com.nineteen.omp.fixture.FixtureFactory.getOrder;
import static com.nineteen.omp.fixture.FixtureFactory.getPayment;
import static com.nineteen.omp.fixture.FixtureFactory.getStore;
import static com.nineteen.omp.fixture.FixtureFactory.getUser;
import static com.nineteen.omp.fixture.FixtureFactory.getUserCoupon;
import static org.assertj.core.api.Assertions.assertThat;

import com.nineteen.omp.coupon.domain.Coupon;
import com.nineteen.omp.coupon.domain.UserCoupon;
import com.nineteen.omp.global.config.JpaAuditingConfig;
import com.nineteen.omp.global.config.QueryDslConfig;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.payment.domain.Payment;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
class PaymentRepositoryTest {

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private EntityManager em;

  private User user;
  private Store store;

  private int searchPageSize;

  @BeforeEach
  void setUp() {
    user = getUser();
    store = getStore(user);
    Coupon coupon = getCoupon();
    UserCoupon userCoupon = getUserCoupon(user, coupon);
    UserCoupon userCoupon2 = getUserCoupon(user, coupon);

    Order order = getOrder(user, store);
    Payment payment = getPayment(order, userCoupon);

    Order order2 = getOrder(user, store);
    Payment payment2 = getPayment(order2, userCoupon2);

    em.persist(user);
    em.persist(store);
    em.persist(order);
    em.persist(coupon);
    em.persist(userCoupon);
    em.persist(userCoupon2);
    em.persist(payment);
    em.persist(order2);
    em.persist(payment2);
    em.flush();
    em.clear();

    searchPageSize = 2;
  }

  @Test
  @DisplayName("사용자 id 값으로 결제 정보를 페이지네이션하여 조회한다.")
  void findByOrder_User_Id() {
    // given

    // when
    Pageable pageable = PageRequest.of(0, 10);
    Page<Payment> byOrderUserId = paymentRepository.findByOrder_User_Id(user.getId(), pageable);

    // then
    assertThat(byOrderUserId.getTotalElements()).isEqualTo(searchPageSize);
  }

  @Test
  @DisplayName("상점 id 값으로 결제 정보를 페이지네이션하여 조회한다.")
  void findByOrder_Store_Id() {
    // given

    // when
    Pageable pageable = PageRequest.of(0, 10);
    Page<Payment> byOrderStoreId = paymentRepository.findByOrder_Store_Id(store.getId(), pageable);

    // then
    assertThat(byOrderStoreId.getTotalElements()).isEqualTo(searchPageSize);
  }

  @Test
  @DisplayName("사용자 닉네임으로 결제 정보를 페이지네이션하여 조회한다.")
  void findByOrder_User_Nickname() {
    // given

    // when
    Pageable pageable = PageRequest.of(0, 10);
    Page<Payment> byOrderUserNickname = paymentRepository.findByOrder_User_Nickname(
        user.getNickname(), pageable);

    // then
    assertThat(byOrderUserNickname.getTotalElements()).isEqualTo(searchPageSize);
  }

  @Test
  @DisplayName("상점 이름으로 결제 정보를 페이지네이션하여 조회한다.")
  void findByOrder_Store_Name() {
    // given

    // when
    Pageable pageable = PageRequest.of(0, 10);
    Page<Payment> byOrderStoreName = paymentRepository.findByOrder_Store_Name(store.getName(),
        pageable);

    // then
    assertThat(byOrderStoreName.getTotalElements()).isEqualTo(searchPageSize);
  }
}