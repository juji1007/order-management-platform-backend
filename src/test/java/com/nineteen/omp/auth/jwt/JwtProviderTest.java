package com.nineteen.omp.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.global.utils.AesEncryptor;
import com.nineteen.omp.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtProviderTest {

  private JwtProvider jwtProvider;

  private UserDetailsImpl userDetails;

  @BeforeEach
  void setUp() {
    jwtProvider = new JwtProvider(
        1000L,
        1000L,
        "test123test123test123test123test123",
        new AesEncryptor("abtest0123456789"));
    User user = User.builder()
        .id(1L)
        .username("test")
        .password("test")
        .role(Role.USER)
        .build();
    userDetails = new UserDetailsImpl(user);
  }

  @Test
  public void generateAccessToken() throws Exception {
    // given

    // when
    String accessToken = jwtProvider.generateAccessToken(userDetails);

    // then
    assertThat(accessToken).isNotNull();
  }


  @Test
  public void generateRefreshToken() throws Exception {
    // given

    // when
    String refreshToken = jwtProvider.generateRefreshToken(userDetails);

    // then
    assertThat(refreshToken).isNotNull();
  }


  @Test
  public void isAccessToken() throws Exception {
    // given
    String accessToken = jwtProvider.generateAccessToken(userDetails);

    // when
    boolean isAccessToken = jwtProvider.isAccessToken(accessToken);

    // then
    assertThat(isAccessToken).isTrue();
  }


  @Test
  public void isRefreshToken() throws Exception {
    // given
    String refreshToken = jwtProvider.generateRefreshToken(userDetails);

    // when
    boolean isRefreshToken = jwtProvider.isRefreshToken(refreshToken);

    // then
    assertThat(isRefreshToken).isTrue();
  }


  @Test
  public void extractUserId() throws Exception {
    // given
    String token = jwtProvider.generateAccessToken(userDetails);

    // when
    Long userId = jwtProvider.extractUserId(token);

    // then
    assertThat(userId).isEqualTo(userDetails.getUserId());
  }


  @Test
  public void extractRole() throws Exception {
    // given
    String token = jwtProvider.generateAccessToken(userDetails);

    // when
    Role role = jwtProvider.extractRole(token);

    // then
    assertThat(role).isEqualTo(userDetails.getRole());
  }


  @Test
  public void isExpired() throws Exception {
      // given
    String token = jwtProvider.generateAccessToken(userDetails);

    // when
    boolean isExpired = jwtProvider.isExpired(token);

      // then
      assertThat(isExpired).isFalse();
  }
}