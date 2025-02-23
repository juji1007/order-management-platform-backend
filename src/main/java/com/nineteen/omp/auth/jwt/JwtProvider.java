package com.nineteen.omp.auth.jwt;

import com.nineteen.omp.auth.domain.Role;
import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.auth.jwt.enums.JwtClaims;
import com.nineteen.omp.global.utils.Encryptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

  private static final String ACCESS_TOKEN = "access_token";
  private static final String REFRESH_TOKEN = "refresh_token";

  private final Long refreshTokenValidity;
  private final Long accessTokenValidity;
  private final SecretKey secretKey;
  private final String PREFIX;

  private final Encryptor encryptor;

  public JwtProvider(
      @Value("${jwt.refresh-token-validity}") Long refreshTokenValidity,
      @Value("${jwt.access-token-validity}") Long accessTokenValidity,
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.prefix}") String prefix,
      @Qualifier("AES") Encryptor encryptor
  ) {
    this.refreshTokenValidity = refreshTokenValidity;
    this.accessTokenValidity = accessTokenValidity;
    this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        SIG.HS256.key().build().getAlgorithm());
    PREFIX = prefix;
    this.encryptor = encryptor;
  }

  public String generateAccessToken(UserDetails userDetails) throws Exception {
    log.info("Generate access token");
    UserDetailsImpl details = (UserDetailsImpl) userDetails;
    return PREFIX + generateJwt(
        details.getUserId(), details.getRole(), accessTokenValidity, ACCESS_TOKEN);
  }

  public String generateRefreshToken(UserDetails userDetails) throws Exception {
    log.info("Generate refresh token");
    UserDetailsImpl details = (UserDetailsImpl) userDetails;
    return generateJwt(
        details.getUserId(), details.getRole(), refreshTokenValidity, REFRESH_TOKEN);
  }

  public String generateAccessToken(String refreshToken) throws Exception {
    log.info("Generate access token with refresh token");
    Long userId = this.extractUserId(refreshToken);
    Role role = this.extractRole(refreshToken);
    return PREFIX + generateJwt(userId, role, accessTokenValidity, ACCESS_TOKEN);
  }

  private String generateJwt(
      Long userId,
      Role role,
      Long tokenValidity,
      String tokenType
  ) throws Exception {
    return Jwts.builder()
        .subject(tokenType)
        .claim(JwtClaims.USER_ID.getKey(),
            encryptor.encrypt(String.valueOf(userId)))
        .claim(JwtClaims.ROLE.getKey(), role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + tokenValidity))
        .signWith(secretKey)
        .compact();
  }

  public String extractTokenType(String token) {
    Claims claims = parseClaims(token);
    return claims.getSubject();
  }

  public Long extractUserId(String token) throws Exception {
    Claims claims = parseClaims(token);
    return Long.parseLong(
        encryptor.decrypt(claims.get(JwtClaims.USER_ID.getKey()).toString()));
  }

  public Role extractRole(String token) {
    Claims claims = parseClaims(token);
    return Role.valueOf(claims.get(JwtClaims.ROLE.getKey()).toString());
  }

  public Boolean isExpired(String token) {
    Claims claims = parseClaims(token);
    return claims.getExpiration().before(new Date());
  }

  private Claims parseClaims(String token) {
    String mainToken = token;
    if (token.startsWith(PREFIX)) {
      mainToken = token.substring(PREFIX.length());
    }
    return Jwts.parser().verifyWith(secretKey).build()
        .parseSignedClaims(mainToken)
        .getPayload();
  }

  public Boolean isAccessToken(String token) {
    return ACCESS_TOKEN.equals(extractTokenType(token));
  }

  public Boolean isRefreshToken(String token) {
    return REFRESH_TOKEN.equals(extractTokenType(token));
  }
}
