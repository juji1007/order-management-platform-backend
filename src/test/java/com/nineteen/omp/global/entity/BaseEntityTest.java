package com.nineteen.omp.global.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.nineteen.omp.global.config.JpaAuditingConfig;
import com.nineteen.omp.global.config.QueryDslConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


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

  @Entity
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
