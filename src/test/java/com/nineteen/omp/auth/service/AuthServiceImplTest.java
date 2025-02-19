package com.nineteen.omp.auth.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nineteen.omp.auth.exception.AuthExceptionCode;
import com.nineteen.omp.auth.jwt.JwtHeaderHandler;
import com.nineteen.omp.auth.jwt.JwtProvider;
import com.nineteen.omp.auth.jwt.repository.RefreshRepository;
import com.nineteen.omp.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private JwtHeaderHandler jwtHeaderHandler;
  @Mock
  private JwtProvider jwtProvider;
  @Mock
  private RefreshRepository refreshRepository;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }


  @Nested
  @DisplayName("로그아웃 테스트")
  class LogoutTest {

    @Test
    @DisplayName("로그아웃 성공")
    void logoutSuccess() throws Exception {
      // given
      String refreshToken = "test.refresh.token";
      Long userId = 1L;

      when(jwtHeaderHandler.getRefreshToken(request)).thenReturn(refreshToken);
      when(jwtProvider.extractUserId(refreshToken)).thenReturn(userId);

      // when
      authService.logout(request, response);

      // then
      verify(refreshRepository).deleteById(userId);
      verify(jwtHeaderHandler).removeToken(response);
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 로그아웃 시도시 예외 발생")
    void logoutWithInvalidToken() throws Exception {
      // given
      String invalidToken = "invalid.token";

      when(jwtHeaderHandler.getRefreshToken(request)).thenReturn(invalidToken);
      when(jwtProvider.extractUserId(invalidToken))
          .thenThrow(new RuntimeException("Invalid token"));

      // when & then
      assertThrows(CustomException.class, () ->
          authService.logout(request, response));

      verify(refreshRepository, never()).deleteById(any());
      verify(jwtHeaderHandler, never()).removeToken(any());
    }
  }

  @Nested
  @DisplayName("토큰 재발급 테스트")
  class ReissueTest {

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissueSuccess() throws Exception {
      // given
      String refreshToken = "test.refresh.token";
      String newAccessToken = "new.access.token";

      when(jwtHeaderHandler.getRefreshToken(request)).thenReturn(refreshToken);
      when(jwtProvider.generateAccessToken(refreshToken)).thenReturn(newAccessToken);

      // when
      authService.reissue(request, response);

      // then
      verify(jwtHeaderHandler).addAccessToken(response, newAccessToken);
    }

    @Test
    @DisplayName("토큰 생성 실패시 예외 발생")
    void reissueFailure() throws Exception {
      // given
      String refreshToken = "test.refresh.token";

      when(jwtHeaderHandler.getRefreshToken(request)).thenReturn(refreshToken);
      when(jwtProvider.generateAccessToken(refreshToken))
          .thenThrow(new RuntimeException("Token generation failed"));

      // when & then
      assertThrows(CustomException.class, () ->
          authService.reissue(request, response));

      verify(jwtHeaderHandler, never()).addAccessToken(any(), any());
    }

    @Test
    @DisplayName("Refresh 토큰이 없는 경우")
    void reissueWithNoRefreshToken() throws Exception {
      // given
      when(jwtHeaderHandler.getRefreshToken(request))
          .thenThrow(new CustomException(AuthExceptionCode.NOF_FOUND_REFRESH_TOKEN));

      // when & then
      assertThrows(CustomException.class, () ->
          authService.reissue(request, response));

      verify(jwtProvider, never()).generateAccessToken(any(UserDetails.class));
      verify(jwtHeaderHandler, never()).addAccessToken(any(), any());
    }
  }
}