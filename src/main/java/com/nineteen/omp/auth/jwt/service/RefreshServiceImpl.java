package com.nineteen.omp.auth.jwt.service;

import com.nineteen.omp.auth.jwt.entity.RefreshToken;
import com.nineteen.omp.auth.jwt.repository.RefreshRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RefreshServiceImpl implements RefreshService {

  private final RefreshRepository refreshRepository;
  private final long EXPIRATION_TIME;

  public RefreshServiceImpl(
      RefreshRepository refreshRepository,
      @Value("${jwt.refresh-token-validity}") long expirationTime
  ) {
    this.refreshRepository = refreshRepository;
    EXPIRATION_TIME = expirationTime;
  }

  @Override
  @Transactional
  public void save(Long userId, String refreshToken) {
    RefreshToken refreshTokenEntity = RefreshToken.builder()
        .userId(userId)
        .refreshToken(refreshToken)
        .expirationTime(EXPIRATION_TIME)
        .build();
    refreshRepository.save(refreshTokenEntity);
  }
}
