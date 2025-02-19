package com.nineteen.omp.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.global.utils.Encryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

  private JwtProvider jwtProvider;

  @Mock
  private Encryptor encryptor;

  private final String secret = "mySecretKey123456789sdfsdfer262626werwerw0123456";
  private final String prefix = "Bearer ";
  private final Long accessTokenValidity = 3600000L;
  private final Long refreshTokenValidity = 86400000L;

  @BeforeEach
  void setUp() {
    jwtProvider = spy(new JwtProvider(
        refreshTokenValidity,
        accessTokenValidity,
        secret,
        prefix,
        encryptor
    ));
  }


  @Test
  @DisplayName("액세스 토큰 생성")
  public void generateAccessToken() throws Exception {
    // given
    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    Long userId = 1L;
    String encryptedUserId = "encryptedUserId";

    when(userDetails.getUserId()).thenReturn(userId);
    when(userDetails.getRole()).thenReturn(Role.USER);
    when(encryptor.encrypt(any())).thenReturn(encryptedUserId);

    // when
    String accessToken = jwtProvider.generateAccessToken(userDetails);

    // then
    assertThat(accessToken).isNotNull();
    assertThat(accessToken.startsWith(prefix)).isTrue();
  }


  @Test
  @DisplayName("리프레시 토큰 생성")
  public void generateRefreshToken() throws Exception {
    // given
    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    Long userId = 1L;
    String encryptedUserId = "encryptedUserId";

    when(userDetails.getUserId()).thenReturn(userId);
    when(userDetails.getRole()).thenReturn(Role.USER);
    when(encryptor.encrypt(any())).thenReturn(encryptedUserId);

    // when
    String refreshToken = jwtProvider.generateRefreshToken(userDetails);

    // then
    assertThat(refreshToken).isNotNull();
    assertThat(refreshToken.startsWith(prefix)).isTrue();
  }


  @Test
  @DisplayName("액세스 토큰 확인")
  public void isAccessToken() throws Exception {
    // given
    String tokenType = "access_token";

    doReturn(tokenType).when(jwtProvider).extractTokenType(any());

    // when
    boolean isAccessToken = jwtProvider.isAccessToken(any());

    // then
    assertThat(isAccessToken).isTrue();
  }


  @Test
  @DisplayName("리프레시 토큰 확인")
  public void isRefreshToken() throws Exception {
    // given
    String tokenType = "refresh_token";

    doReturn(tokenType).when(jwtProvider).extractTokenType(any());

    // when
    boolean isRefreshToken = jwtProvider.isRefreshToken(any());

    // then
    assertThat(isRefreshToken).isTrue();
  }


  @Test
  @DisplayName("토큰에서 userId 추출")
  public void extractUserId() throws Exception {
    // given
    Long userId = 1L;
    // jwt.io 에서 만든 토큰 데이터
    String token = "BEARER "
        + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
        + "eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidXNlcl9pZCI6IjEiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTUxNjIzOTAyMn0."
        + "h2PKF8b83A40XxIsadtCmo4x8ApcwtNqc91_Nfugfl8";

    when(encryptor.decrypt(any())).thenReturn(String.valueOf(userId));

    // when
    Long extractedUserId = jwtProvider.extractUserId(token);

    // then
    assertThat(extractedUserId).isEqualTo(userId);
  }


  @Test
  @DisplayName("토큰에서 Role 추출")
  public void extractRole() throws Exception {
    // given
    Role role = Role.USER;
    // jwt.io 에서 만든 토큰 데이터
    String token = "BEARER "
        + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
        + "eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidXNlcl9pZCI6IjEiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTUxNjIzOTAyMn0."
        + "h2PKF8b83A40XxIsadtCmo4x8ApcwtNqc91_Nfugfl8";

    // when
    Role extractedRole = jwtProvider.extractRole(token);

    // then
    assertThat(extractedRole).isEqualTo(role);
  }


  @Test
  @DisplayName("토큰 만료 확인")
  public void isExpired() throws Exception {
    // given
    // jwt.io 에서 만든 토큰 데이터
    String token = "BEARER "
        + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
        + "eyJzdWIiOiJyZWZyZXNoLXRva2VuIiwidXNlcl9pZCI6IjEiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNzM5ODc4MDQyMDE2fQ."
        + "Tlh3PC5-OVInRpYBqw0fTVMpH9ibp2taXhuRos6V_iw";

    // when
    boolean isExpired = jwtProvider.isExpired(token);

    // then
    assertThat(isExpired).isFalse();
  }
}