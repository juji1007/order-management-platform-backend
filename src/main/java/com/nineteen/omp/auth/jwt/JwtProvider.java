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

  private final Encryptor encryptor;

  public JwtProvider(
      @Value("${jwt.refresh-token-validity}") Long refreshTokenValidity,
      @Value("${jwt.access-token-validity}") Long accessTokenValidity,
      @Value("${jwt.secret}") String secret,
      @Qualifier("AES") Encryptor encryptor
  ) {
    this.refreshTokenValidity = refreshTokenValidity;
    this.accessTokenValidity = accessTokenValidity;
    this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        SIG.HS256.key().build().getAlgorithm());
    this.encryptor = encryptor;
  }

  public String generateAccessToken(UserDetails userDetails) throws Exception {
    log.info("Generate access token");
    return generateJwt((UserDetailsImpl) userDetails, accessTokenValidity, ACCESS_TOKEN);
  }

  public String generateRefreshToken(UserDetails userDetails) throws Exception {
    log.info("Generate refresh token");
    return generateJwt((UserDetailsImpl) userDetails, refreshTokenValidity, REFRESH_TOKEN);
  }

  private String generateJwt(
      UserDetailsImpl userDetails,
      Long tokenValidity,
      String tokenType
  ) throws Exception {
    return Jwts.builder()
        .subject(tokenType)
        .claim(JwtClaims.USER_ID.getKey(),
            encryptor.encrypt(String.valueOf(userDetails.getUserId())))
        .claim(JwtClaims.ROLE.getKey(), userDetails.getRole())
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
    return Jwts.parser().verifyWith(secretKey).build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public Boolean isAccessToken(String token) {
    return ACCESS_TOKEN.equals(extractTokenType(token));
  }

  public Boolean isRefreshToken(String token) {
    return REFRESH_TOKEN.equals(extractTokenType(token));
  }

}
