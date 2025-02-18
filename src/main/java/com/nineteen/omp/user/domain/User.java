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

  private String username;

  private String password;

  private String nickname;

  @Enumerated(value = EnumType.STRING)
  private Role role;

  private String email;

  private Boolean is_public;

  private String delivery_address;
