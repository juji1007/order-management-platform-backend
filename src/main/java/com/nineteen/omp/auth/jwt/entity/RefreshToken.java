package com.nineteen.omp.auth.jwt.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshToken")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

  @Id
  private Long userId;

  private String refreshToken;

  @TimeToLive
  private Long expirationTime;

  @Builder
  public RefreshToken(Long userId, String refreshToken, Long expirationTime) {
    this.userId = userId;
    this.refreshToken = refreshToken;
    this.expirationTime = expirationTime;
  }
}
