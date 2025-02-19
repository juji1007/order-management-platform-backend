package com.nineteen.omp.auth.jwt.service;

public interface RefreshService {

  void save(Long userId, String refreshToken);
}
