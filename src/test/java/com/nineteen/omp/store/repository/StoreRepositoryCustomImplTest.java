package com.nineteen.omp.store.repository;

import static com.nineteen.omp.fixture.FixtureFactory.getStore;
import static com.nineteen.omp.fixture.FixtureFactory.getStoreProduct;
import static com.nineteen.omp.fixture.FixtureFactory.getUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.global.config.JpaAuditingConfig;
import com.nineteen.omp.global.config.QueryDslConfig;
import com.nineteen.omp.order.domain.Order;
import com.nineteen.omp.order.domain.OrderReview;
import com.nineteen.omp.order.domain.emuns.OrderStatus;
import com.nineteen.omp.order.domain.emuns.OrderType;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.store.controller.dto.SearchStoreResponseDto;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreCategory;
import com.nineteen.omp.store.domain.StoreStatus;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.EntityManager;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
public class StoreRepositoryCustomImplTest {

  private static final Logger log = LoggerFactory.getLogger(StoreRepositoryCustomImplTest.class);

  @Autowired
  private StoreRepositoryCustomImpl storeRepositoryCustom;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private EntityManager em;

  @Test
  public void testSearchAdvancedStore() {
    insertTestData();
//    insertTestDataProductTest();

    PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());
    Page<SearchStoreResponseDto> results = storeRepositoryCustom.searchAdvancedStore(
        "찌개", "한", "KOREAN", 4, pageable);

    log.info("Total Elements: " + results.getTotalElements());
    log.info("Total Pages: " + results.getTotalPages());
    log.info("Total contents: " + results.getContent());
    results.getContent().forEach(result -> {
      log.info("Store id: " + result.storeId());
      log.info("Store Name: " + result.storeName());
      log.info("Store Category: " + result.categoryName());
      log.info("Store Status: " + result.statusName());
      log.info("Store avgRating: " + result.rating());
    });

    assertThat(results).isNotNull();
    assertThat(results.getTotalElements()).isGreaterThan(0);
  }

  public void insertTestData() {
    Random random = new Random();

    for (int i = 0; i < 50; i++) {
      User user = User.builder()
          .username("test_owner" + i)
          .password("password123" + i)
          .nickname("가게 주인")
          .role(Role.OWNER)
          .email("owner" + i + "@example.com")
          .delivery_address("서울시 종로구")
          .build();
      em.persist(user);

      Store store = Store.builder()
          .user(user)
          .name("한식당" + i)
          .storeCategory(StoreCategory.KOREAN)
          .address("서울시 종로구" + i)
          .phone("010-1234-567" + i)
          .status(StoreStatus.OPEN)
          .build();
      em.persist(store);

      for (int j = 0; j < 3; j++) {
        StoreProduct product = StoreProduct.builder()
            .store(store)
            .name("김치찌개" + i + "-" + j)
            .price(10000 + i)
            .description("맛있는 김치찌개")
            .build();
        em.persist(product);
      }

      for (int j = 0; j < 2; j++) {
        Order order = Order.builder()
            .store(store)
            .totalPrice(20000 + i)
            .orderStatus(OrderStatus.DELIVERED)
            .orderType(OrderType.DELIVERY)
            .build();
        em.persist(order);

        for (int k = 0; k < 2; k++) {
          int rating = random.nextInt(5) + 1; // 1부터 5까지 랜덤 값

          OrderReview review = OrderReview.builder()
              .order(order)
              .content("맛있어요!" + i + "-" + j + "-" + k)
              .rating(Integer.valueOf(rating))
              .build();
          em.persist(review);
        }
      }
      em.flush();
      em.clear();
    }
  }

  public void insertTestDataProductTest() {
    Random random = new Random();

    for (int i = 0; i < 3; i++) {
      User user = User.builder()
          .username("test_owner" + i)
          .password("password123" + i)
          .nickname("가게 주인")
          .role(Role.OWNER)
          .email("owner" + i + "@example.com")
          .delivery_address("서울시 종로구")
          .build();
      em.persist(user);

      Store store = Store.builder()
          .user(user)
          .name("한식당" + i)
          .storeCategory(StoreCategory.KOREAN)
          .address("서울시 종로구" + i)
          .phone("010-1234-567" + i)
          .status(StoreStatus.OPEN)
          .build();
      em.persist(store);

      if (i == 0) {
        addProductToStore(store, "육회비빔밥", 10000);
        addProductToStore(store, "불고기", 12000);
        addProductToStore(store, "김치찌개", 11000);
      } else if (i == 1) {
        addProductToStore(store, "육회비빔밥", 10000);
        addProductToStore(store, "된장찌개", 11000);
        addProductToStore(store, "김치찌개", 11000);
      } else if (i == 2) {
        addProductToStore(store, "육회비빔밥", 11000);
        addProductToStore(store, "갈비찜", 15000);
      }

      for (int j = 0; j < 2; j++) {
        Order order = Order.builder()
            .store(store)
            .totalPrice(20000 + i)
            .orderStatus(OrderStatus.DELIVERED)
            .orderType(OrderType.DELIVERY)
            .build();
        em.persist(order);

        for (int k = 0; k < 7; k++) {
//          int rating = random.nextInt(5) + 1;
          int rating = 5;
          OrderReview review = OrderReview.builder()
              .order(order)
              .content("맛있어요!" + i + "-" + j + "-" + k)
              .rating(Integer.valueOf(rating))
              .build();
          em.persist(review);
        }
      }
      em.flush();
      em.clear();
    }
  }

  public void addProductToStore(Store store, String productName, int price) {
    StoreProduct product = StoreProduct.builder()
        .store(store)
        .name(productName)
        .price(price)
        .description("맛있는 " + productName)
        .build();
    em.persist(product);
  }


  @Test
  @DisplayName("findById를 @EntityGraph로 join해서 조회 테스트")
  public void findByIdWithFetchJoin() {
    // given
    User user = getUser();
    Store store = getStore(user);
    StoreProduct storeProduct1 = getStoreProduct(store);
    StoreProduct storeProduct2 = getStoreProduct(store);
    StoreProduct storeProduct3 = getStoreProduct(store);
    em.persist(user);
    em.persist(store);
    em.persist(storeProduct1);
    em.persist(storeProduct2);
    em.persist(storeProduct3);
    em.flush();
    em.clear();

    // when
    Store findStore = storeRepository.findById(store.getId()).get();

    // then
    assertThat(findStore.getStoreProducts()).isNotEmpty();

  }
}
