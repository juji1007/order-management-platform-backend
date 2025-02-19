package com.nineteen.omp.auth.handler;

import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.auth.jwt.JwtHeaderHandler;
import com.nineteen.omp.auth.jwt.JwtProvider;
import com.nineteen.omp.auth.jwt.service.RefreshService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtProvider jwtProvider;
  private final JwtHeaderHandler jwtHeaderHandler;
  private final RefreshService refreshService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException, ServletException {
    log.info("인증 성공 처리 시작");

    UserDetailsImpl userDetails =
        (UserDetailsImpl) authentication.getPrincipal();

    // 토큰 생성
    TokenPair tokens = generateTokens(userDetails);
    if (tokens == null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    // Refresh 토큰 저장
    refreshService.save(userDetails.getUserId(), tokens.refreshToken());

    // 헤더에 토큰 추가
    jwtHeaderHandler.addAccessToken(response, tokens.accessToken());
    jwtHeaderHandler.addRefreshToken(response, tokens.refreshToken());

    // 응답
    response.setStatus(HttpServletResponse.SC_OK);
    log.info("인증 성공 처리 완료 - userId: {}", userDetails.getUserId());
  }

  private record TokenPair(String accessToken, String refreshToken) {

  }

  private TokenPair generateTokens(UserDetailsImpl userDetails) {
    try {
      String accessToken = jwtProvider.generateAccessToken(userDetails);
      String refreshToken = jwtProvider.generateRefreshToken(userDetails);
      return new TokenPair(accessToken, refreshToken);
    } catch (Exception e) {
      log.error("토큰 생성 실패 - userId: {}", userDetails.getUserId(), e);
      return null;
    }
  }
}
