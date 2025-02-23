package com.nineteen.omp.auth.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.auth.jwt.JwtHeaderHandler;
import com.nineteen.omp.auth.jwt.JwtProvider;
import com.nineteen.omp.auth.jwt.service.RefreshService;
import com.nineteen.omp.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

  @InjectMocks
  private CustomAuthenticationSuccessHandler handler;

  @Mock
  private JwtProvider jwtProvider;
  @Mock
  private JwtHeaderHandler jwtHeaderHandler;
  @Mock
  private RefreshService refreshService;
  @Mock
  private Authentication authentication;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private UserDetailsImpl userDetails;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    userDetails = new UserDetailsImpl(
        User.builder()
            .id(1L)
            .username("test")
            .password("test")
            .role(Role.USER)
            .build()
    );
  }

  @Test
  @DisplayName("인증 성공 시 토큰이 정상적으로 생성되고 저장된다")
  void successfulAuthentication() throws Exception {
    // given
    String accessToken = "test.access.token";
    String refreshToken = "test.refresh.token";

    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(jwtProvider.generateAccessToken(userDetails)).thenReturn(accessToken);
    when(jwtProvider.generateRefreshToken(userDetails)).thenReturn(refreshToken);

    // when
    handler.onAuthenticationSuccess(request, response, authentication);

    // then
    verify(refreshService).save(userDetails.getUserId(), refreshToken);
    verify(jwtHeaderHandler).addAccessToken(response, accessToken);
    verify(jwtHeaderHandler).addRefreshToken(response, refreshToken);
    assertThat(response.getStatus()).isEqualTo(MockHttpServletResponse.SC_OK);
  }

  @Test
  @DisplayName("토큰 생성 실패 시 예외가 발생한다")
  void tokenGenerationFailure() throws Exception {
    // given
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(jwtProvider.generateAccessToken(userDetails))
        .thenThrow(new RuntimeException("Token generation failed"));

    // when
    handler.onAuthenticationSuccess(request, response, authentication);

    // then
    assertThat(response.getStatus()).isEqualTo(MockHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  @DisplayName("성공 시 응답에 적절한 상태 코드가 설정된다")
  void successResponseStatus() throws Exception {
    // given
    String accessToken = "test.access.token";
    String refreshToken = "test.refresh.token";

    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(jwtProvider.generateAccessToken(userDetails)).thenReturn(accessToken);
    when(jwtProvider.generateRefreshToken(userDetails)).thenReturn(refreshToken);

    // when
    handler.onAuthenticationSuccess(request, response, authentication);

    // then
    assertThat(response.getStatus()).isEqualTo(MockHttpServletResponse.SC_OK);
  }
}