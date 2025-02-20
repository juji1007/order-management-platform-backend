package com.nineteen.omp.store.domain;


import com.nineteen.omp.user.domain.User;
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

@Entity
@Table(name = "p_store")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  public Category category;

  // TODO : 실행을 위한 임시 컬럼
  public enum Category {
    KOREAN_FOOD,  // 한식
    KIMCHI,        // 김치
    BBQ,           // 바비큐
    SOJU_BAR,      // 소주바
    NOODLES,       // 면류
    DESSERT,       // 디저트
    // 추가적인 카테고리들을 여기에 정의
  }
}
