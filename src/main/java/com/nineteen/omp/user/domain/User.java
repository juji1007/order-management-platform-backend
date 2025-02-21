package com.nineteen.omp.user.domain;


import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.global.entity.BaseEntity;
import com.nineteen.omp.user.service.dto.UpdateUserRequestCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_user SET is_deleted = true WHERE id = ?")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, unique = true, length = 30)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 30)
  private String nickname;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false, length = 30)
  private Role role;

  @Column(nullable = false, unique = true, length = 30)
  private String email;

  @Column(nullable = false, length = 30)
  private String delivery_address;

  public void update(UpdateUserRequestCommand requestCommand) {
    if (requestCommand.nickname() != null) {
      this.nickname = requestCommand.nickname();
    }
    if (requestCommand.email() != null) {
      this.email = requestCommand.email();
    }
    if (requestCommand.delivery_address() != null) {
      this.delivery_address = requestCommand.delivery_address();
    }
  }
}
