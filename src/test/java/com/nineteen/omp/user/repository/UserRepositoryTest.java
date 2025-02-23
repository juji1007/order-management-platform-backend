package com.nineteen.omp.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.global.config.JpaAuditingConfig;
import com.nineteen.omp.global.config.QueryDslConfig;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
class UserRepositoryTest {

  @Autowired
  private EntityManager em;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("입력된 유저 이름을 포함하는 유저 조회 테스트")
  void findAllByNicknameContainsIgnoreCase() {
    // given
    User user1 = User.builder()
        .username("test")
        .password("test")
        .nickname("tEst")
        .role(Role.USER)
        .email("test")
        .delivery_address("test")
        .build();
    User user2 = User.builder()
        .username("test2")
        .password("test2")
        .nickname("Test2")
        .role(Role.USER)
        .email("test2")
        .delivery_address("test2")
        .build();
    User user3 = User.builder()
        .username("test3")
        .password("test3")
        .nickname("3test")
        .role(Role.USER)
        .email("test3")
        .delivery_address("test3")
        .build();

    em.persist(user1);
    em.persist(user2);
    em.persist(user3);
    em.flush();
    em.clear();

    String nickname = "test";

    // when
    var users = userRepository.findAllByNicknameContainsIgnoreCase(nickname, null);

    // then
    assertThat(users).hasSize(3);
  }

}