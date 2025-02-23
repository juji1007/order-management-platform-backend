package com.nineteen.omp.global.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.global.config.JpaAuditingConfig;
import com.nineteen.omp.global.config.QueryDslConfig;
import com.nineteen.omp.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
@DisplayName("BaseEntity 클래스의")
class BaseEntityTest {

  @Autowired
  private EntityManager em;

  @Nested
  @DisplayName("Describe: createdAt은")
  class CreatedAtDescribe {

    @Nested
    @DisplayName("Context: Entity를 Persist하면")
    class CreatedAtContext {

      @Test
      @DisplayName("It: 자동으로 갱신된다.")
      void update_createdAt_when_entity_saved() {
        //given
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);

        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUserId()).thenReturn(1L);

        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        TestEntity testEntity = new TestEntity();

        //when
        em.persist(testEntity);

        //then
        assertThat(testEntity.getCreatedAt()).isNotNull();
      }
    }
  }

  @Nested
  @DisplayName("Describe: updatedAt은")
  class UpdatedAtDescribe {

    @Nested
    @DisplayName("Context: Entity를 수정하면")
    class UpdatedAtContext {

      @Test
      @DisplayName("It: 자동으로 갱신된다.")
      void update_updatedAt_when_entity_updated() {
        //given
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);

        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUserId()).thenReturn(1L);

        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        TestEntity testEntity = new TestEntity("before");
        em.persist(testEntity);
        LocalDateTime beforeUpdate = testEntity.getUpdatedAt();

        //when
        testEntity.updateSomethingField("after");
        em.flush();

        //then
        TestEntity updated = em.find(TestEntity.class, testEntity.getId());
        assertThat(updated.getUpdatedAt()).isNotEqualTo(beforeUpdate);
      }
    }
  }

  @Nested
  @DisplayName("Describe: deletedAt은")
  class DeletedAtDescribe {

    @Nested
    @DisplayName("Context: 삭제하면")
    class DeletedAtContext {

      @Test
      @DisplayName("It: 자동으로 갱신된다.")
      void update_deletedAt_when_entity_deleted() {
        //given
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);

        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUserId()).thenReturn(1L);

        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        TestEntity testEntity = new TestEntity();
        em.persist(testEntity);
        LocalDateTime beforeDelete = testEntity.getDeletedAt();

        //when
        em.remove(testEntity);

        //then
        assertThat(beforeDelete).isNull();
        assertThat(testEntity.getDeletedAt()).isNotNull();
      }
    }
  }

  @Nested
  @DisplayName("Describe: createdBy은")
  class CreatedByDescribe {

    @Nested
    @DisplayName("Context: Entity를 Persist하면")
    class CreatedByContext {

      @Test
      @DisplayName("It: 자동으로 갱신된다.")
      void update_createdBy_when_entity_saved() {
        //given
        User user = User.builder()
            .id(1L)
            .username("test")
            .password("test")
            .role(Role.USER)
            .nickname("test")
            .email("email")
            .delivery_address("address")
            .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            new UserDetailsImpl(user), null, null
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TestEntity testEntity = new TestEntity();

        //when
        em.persist(testEntity);

        //then
        assertThat(testEntity.getCreatedBy()).isNotNull();
      }
    }
  }

  @Nested
  @DisplayName("Describe: updatedBy은")
  class UpdatedByDescribe {

    @Nested
    @DisplayName("Context: Entity를 수정하면")
    class UpdatedByContext {

      @Test
      @DisplayName("It: 자동으로 갱신된다.")
      void update_updatedBy_when_entity_updated() {
        //given
        User user = User.builder()
            .id(1L)
            .username("test")
            .password("test")
            .role(Role.USER)
            .nickname("test")
            .email("email")
            .delivery_address("address")
            .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            new UserDetailsImpl(user), null, null
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TestEntity testEntity = new TestEntity("before");
        em.persist(testEntity);

        User user2 = User.builder()
            .id(2L)
            .username("test")
            .password("test")
            .role(Role.USER)
            .nickname("test")
            .email("email")
            .delivery_address("address")
            .build();
        Authentication authentication2 = new UsernamePasswordAuthenticationToken(
            new UserDetailsImpl(user2), null, null
        );
        SecurityContextHolder.getContext().setAuthentication(authentication2);

        Long beforeUpdate = testEntity.getUpdatedBy();

        //when
        testEntity.updateSomethingField("after");
        em.flush();

        //then
        TestEntity updated = em.find(TestEntity.class, testEntity.getId());
        assertThat(updated.getUpdatedBy()).isNotEqualTo(beforeUpdate);
      }
    }
  }

  @Nested
  @DisplayName("Describe: deletedBy은")
  class DeletedByDescribe {

    @Nested
    @DisplayName("Context: 삭제하면")
    class DeletedByContext {

      @Test
      @DisplayName("It: 자동으로 갱신된다.")
      void update_deletedBy_when_entity_deleted() {
        //given
        User user = User.builder()
            .id(1L)
            .username("test")
            .password("test")
            .role(Role.USER)
            .nickname("test")
            .email("email")
            .delivery_address("address")
            .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            new UserDetailsImpl(user), null, null
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TestEntity testEntity = new TestEntity();
        em.persist(testEntity);

        Long beforeDelete = testEntity.getDeletedBy();

        //when
        em.remove(testEntity);

        //then
        assertThat(beforeDelete).isNull();
        assertThat(testEntity.getDeletedBy()).isNotNull();
      }
    }
  }

  @Nested
  @DisplayName("Describe: Entity는")
  class EntityDescribe {

    @Nested
    @DisplayName("Context: 삭제하면")
    class IsDeletedContext {

      @Test
      @DisplayName("It: isDeleted가 true로 변경된다.")
      void update_isDeleted_when_entity_deleted() {
        //given
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);

        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUserId()).thenReturn(1L);

        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        TestEntity testEntity = new TestEntity();
        em.persist(testEntity);

        //when
        em.remove(testEntity);

        //then
        assertThat(testEntity.getIsDeleted()).isTrue();
      }

      @Test
      @DisplayName("It: 조회되지 않는다.")
      void not_found_when_entity_deleted() {
        //given
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);

        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUserId()).thenReturn(1L);

        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        TestEntity testEntity = new TestEntity();
        em.persist(testEntity);

        //when
        em.remove(testEntity);
        em.flush();

        //then
        TestEntity deleted = em.find(TestEntity.class, testEntity.getId());
        assertThat(deleted).isNull();
      }
    }
  }

  @Entity(name = "test_entity")
  @SQLRestriction("is_deleted = false")
  @SQLDelete(sql = "UPDATE test_entity SET is_deleted = true WHERE id = ?")
  private static class TestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String somethingField;

    public TestEntity() {
    }

    public TestEntity(String somethingField) {
      this.somethingField = somethingField;
    }

    public Long getId() {
      return id;
    }

    public void updateSomethingField(String somethingField) {
      this.somethingField = somethingField;
    }

  }
}