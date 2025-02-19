package com.nineteen.omp.auth.service;

import com.nineteen.omp.auth.exception.AuthExceptionCode;
import com.nineteen.omp.auth.jwt.JwtHeaderHandler;
import com.nineteen.omp.auth.jwt.JwtProvider;
import com.nineteen.omp.auth.jwt.repository.RefreshRepository;
import com.nineteen.omp.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtHeaderHandler jwtHeaderHandler;
  private final JwtProvider jwtProvider;
  private final RefreshRepository refreshRepository;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = jwtHeaderHandler.getRefreshToken(request);
    Long userId;
    try {
      userId = jwtProvider.extractUserId(refreshToken);
    } catch (Exception e) {
      throw new CustomException(AuthExceptionCode.GENERATE_TOKEN_ERROR);
    }
    refreshRepository.deleteById(userId);
    jwtHeaderHandler.removeToken(response);
  }

  @Override
  public void reissue(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = jwtHeaderHandler.getRefreshToken(request);
    String accessToken = getAccessToken(refreshToken);
    jwtHeaderHandler.addAccessToken(response, accessToken);
  }

  private String getAccessToken(String refreshToken) {
    String accessToken;
    try {
      accessToken = jwtProvider.generateAccessToken(refreshToken);
    } catch (Exception e) {
      throw new CustomException(AuthExceptionCode.GENERATE_TOKEN_ERROR);
    }
    return accessToken;
  }
}
