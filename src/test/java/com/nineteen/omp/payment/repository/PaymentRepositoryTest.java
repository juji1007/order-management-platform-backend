package com.nineteen.omp.payment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nineteen.omp.category.domain.StoreCategory;
import com.nineteen.omp.global.config.JpaAuditingConfig;
import com.nineteen.omp.global.config.QueryDslConfig;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.payment.domain.Payment;
import com.nineteen.omp.payment.domain.PaymentMethod;
import com.nineteen.omp.payment.domain.PaymentStatus;
import com.nineteen.omp.payment.domain.PgProvider;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.EntityManager;
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

  @Test
  @DisplayName("사용자 id 값으로 결제 정보를 페이지네이션하여 조회한다.")
  void findByOrder_User_Id() {
    // given
    User user = User.builder()
        .build();
    StoreCategory storeCategory = StoreCategory.builder().build();
    Store store = Store.builder().storeCategory(storeCategory).build();
    Order order = Order.builder()
        .user(user)
        .store(store)
        .build();
    Payment payment = Payment.builder()
        .order(order)
        .totalAmount(1000)
        .pgProvider(PgProvider.MOCK_PAY)
        .status(PaymentStatus.PENDING)
        .method(PaymentMethod.CREDIT_CARD)
        .build();

    Order order2 = Order.builder()
        .user(user)
        .store(store)
        .build();
    Payment payment2 = Payment.builder()
        .order(order2)
        .totalAmount(1000)
        .pgProvider(PgProvider.MOCK_PAY)
        .status(PaymentStatus.PENDING)
        .method(PaymentMethod.CREDIT_CARD)
        .build();

    em.persist(user);
    em.persist(storeCategory);
    em.persist(store);
    em.persist(order);
    em.persist(payment);
    em.persist(order2);
    em.persist(payment2);
    em.flush();
    em.clear();

    // when
    Pageable pageable = PageRequest.of(0, 10);
    Page<Payment> byOrderUserId = paymentRepository.findByOrder_User_Id(user.getId(), pageable);

    // then
    assertThat(byOrderUserId.getTotalElements()).isEqualTo(2);

  }
}