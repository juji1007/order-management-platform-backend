package com.nineteen.omp.user.domain;


import com.nineteen.omp.auth.domain.Role;
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

@Entity
@Table(name = "p_user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

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

  @Column(nullable = false)
  private Boolean is_public;

  @Column(nullable = false, length = 30)
  private String delivery_address;
}
