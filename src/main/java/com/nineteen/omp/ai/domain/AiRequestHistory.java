package com.nineteen.omp.ai.domain;


import com.nineteen.omp.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_ai_request_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiRequestHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String aiServiceName;

  @Column(nullable = false, length = 500)
  private String query;

  @Column(nullable = false, length = 500)
  private String response;

  public AiRequestHistoryBuilder toBuilder() {
    return AiRequestHistory.builder()
        .aiServiceName(this.aiServiceName)
        .query(this.query)
        .response(this.response);
  }

  public static String limitResponseLength(String response) {
    return response.length() > 500 ? response.substring(0, 500) : response;
  }
}