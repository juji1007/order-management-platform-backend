package com.nineteen.omp.auth.jwt.repository;

import com.nineteen.omp.auth.jwt.entity.RefreshToken;
import com.nineteen.omp.global.config.EmbeddedRedisConfig;
import com.nineteen.omp.global.config.RedisConfig;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;

@DataRedisTest
@Import({RedisConfig.class, EmbeddedRedisConfig.class})
class RefreshRepositoryTest {

  @Autowired
  private RefreshRepository refreshRepository;

  @Test
  @DisplayName("RefreshToken 저장 테스트")
  public void saveRefreshToken() throws Exception {
    // given
    long userId = 1L;
    RefreshToken refreshToken = RefreshToken.builder()
        .userId(userId)
        .refreshToken("test.refresh.token")
        .expirationTime(60 * 1000L)
        .build();

    // when
    refreshRepository.save(refreshToken);

    // then
    Assertions.assertThat(refreshRepository.findById(userId).isPresent()).isTrue();
  }

  @Test
  @DisplayName("RefreshToken TimeToLive 테스트")
  public void deleteRefreshToken() throws Exception {
    // given
    long userId = 1L;
    long ttl = 1L;
    RefreshToken refreshToken = RefreshToken.builder()
        .userId(userId)
        .refreshToken("test.refresh.token")
        .expirationTime(ttl)
        .build();
    refreshRepository.save(refreshToken);
    Thread.sleep(ttl * 1000);

    // when
    Optional<RefreshToken> optional = refreshRepository.findById(userId);

    // then
    Assertions.assertThat(optional.isEmpty()).isTrue();
  }
}