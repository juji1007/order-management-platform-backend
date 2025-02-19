package com.nineteen.omp.auth.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.auth.jwt.JwtHeaderHandler;
import com.nineteen.omp.auth.jwt.JwtProvider;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

  @InjectMocks
  private JwtFilter jwtFilter;

  @Mock
  private JwtProvider jwtProvider;
  @Mock
  private JwtHeaderHandler jwtHeaderHandler;
  @Mock
  private UserRepository userRepository;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain filterChain;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filterChain = new MockFilterChain();
  }


  @Test
  @DisplayName("로그인 요청이 들어왔을 때 인증 절차를 건너뛴다.")
  public void loginPathTest() throws Exception {
    // given
    request.setRequestURI("/login");

    // when
    jwtFilter.doFilterInternal(request, response, filterChain);

    // then
    verify(jwtHeaderHandler, never()).getAccessToken(any());
    verify(jwtHeaderHandler, never()).getRefreshToken(any());
    assertThat(response.getStatus()).isEqualTo(MockHttpServletResponse.SC_OK);
  }


  @Test
  @DisplayName("토큰이 없을 때 인증 실패로 응답한다.")
  public void noTokenTest() throws Exception {
    // given
    request.setRequestURI("/api/v1/someEndpoint");

    when(jwtHeaderHandler.getAccessToken(any())).thenReturn(null);
    when(jwtHeaderHandler.getRefreshToken(any())).thenReturn(null);

    // when
    jwtFilter.doFilterInternal(request, response, filterChain);

    // then
    verify(jwtProvider, never()).extractUserId(any());
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
  }

  @Test
  @DisplayName("Access 토큰이 만료되었을 때 인증 실패로 응답한다.")
  public void expiredAccessTokenTest() throws Exception {
    // given
    String accessToken = "expired.access.token";
    String refreshToken = "test.refresh.token";
    request.setRequestURI("/api/v1/someEndpoint");

    when(jwtHeaderHandler.getAccessToken(any())).thenReturn(accessToken);
    when(jwtHeaderHandler.getRefreshToken(any())).thenReturn(refreshToken);
    when(jwtProvider.isExpired(any())).thenReturn(true);

    // when
    jwtFilter.doFilterInternal(request, response, filterChain);

    // then
    verify(jwtProvider, never()).extractUserId(any());
    verify(userRepository, never()).findById(any());
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
  }

  @Test
  @DisplayName("토큰 간의 사용자 불일치로 인증 실패로 응답한다.")
  public void mismatchedUserTest() throws Exception {
    // given
    String accessToken = "test.access.token";
    String refreshToken = "test.refresh.token";
    long userId = 1L;
    request.setRequestURI("/api/v1/someEndpoint");

    when(jwtHeaderHandler.getAccessToken(any())).thenReturn(accessToken);
    when(jwtHeaderHandler.getRefreshToken(any())).thenReturn(refreshToken);
    when(jwtProvider.isExpired(any())).thenReturn(false);
    when(jwtProvider.extractUserId(accessToken)).thenReturn(userId);
    when(jwtProvider.extractUserId(refreshToken)).thenReturn(userId + 1);

    // when
    jwtFilter.doFilterInternal(request, response, filterChain);

    // then
    verify(userRepository, never()).findById(any());
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
  }

  @Test
  @DisplayName("사용자 정보가 없을 때 인증 실패로 응답한다.")
  public void noUserTest() throws Exception {
    // given
    String accessToken = "test.access.token";
    String refreshToken = "test.refresh.token";
    long userId = 1L;
    request.setRequestURI("/api/v1/someEndpoint");

    when(jwtHeaderHandler.getAccessToken(any())).thenReturn(accessToken);
    when(jwtHeaderHandler.getRefreshToken(any())).thenReturn(refreshToken);
    when(jwtProvider.isExpired(any())).thenReturn(false);
    when(jwtProvider.extractUserId(accessToken)).thenReturn(userId);
    when(jwtProvider.extractUserId(refreshToken)).thenReturn(userId);
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // when
    jwtFilter.doFilterInternal(request, response, filterChain);

    // then
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
  }

  @Test
  @DisplayName("토큰이 유효하고 사용자 정보가 있을 때 인증 성공으로 응답한다.")
  public void successTest() throws Exception {
    // given
    String accessToken = "test.access.token";
    String refreshToken = "test.refresh.token";
    long userId = 1L;
    request.setRequestURI("/api/v1/someEndpoint");

    when(jwtHeaderHandler.getAccessToken(any())).thenReturn(accessToken);
    when(jwtHeaderHandler.getRefreshToken(any())).thenReturn(refreshToken);
    when(jwtProvider.isExpired(any())).thenReturn(false);
    when(jwtProvider.extractUserId(accessToken)).thenReturn(userId);
    when(jwtProvider.extractUserId(refreshToken)).thenReturn(userId);
    User user = User.builder()
        .id(userId)
        .username("test")
        .password("test")
        .role(Role.USER)
        .build();
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    // when
    jwtFilter.doFilterInternal(request, response, filterChain);

    // then
    assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
  }

}