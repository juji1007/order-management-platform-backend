package com.nineteen.omp.order.domain;


import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.order.exception.OrderReviewException;
import com.nineteen.omp.order.exception.OrderReviewExceptionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_order_review")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_order_review SET is_deleted = true WHERE order_review_id = ?")
public class OrderReview extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "order_review_id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(name = "content", length = 100, nullable = false)
  private String content;

  @Column(name = "rating", nullable = false)
  @Max(5)
  @Min(1)
  private Integer rating;

  public void changeContent(String content) {
    if (content == null || content.isEmpty()) {
      throw new OrderReviewException(OrderReviewExceptionCode.ORDER_REVIEW_CONTENT_IS_NULL);
    }
    this.content = content;
  }

  public void changeRating(Integer rating) {
    if (rating == null || rating < 1 || rating > 5) {
      throw new OrderReviewException(OrderReviewExceptionCode.ORDER_REVIEW_RATING_OUT_OF_RANGE);
    }
    this.rating = rating;
  }
}