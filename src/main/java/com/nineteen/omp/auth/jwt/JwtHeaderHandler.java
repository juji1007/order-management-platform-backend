package com.nineteen.omp.auth.jwt;

import com.nineteen.omp.auth.exception.AuthExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderHandler {

  private static final String REFRESH_TOKEN_COOKIE_HEADER = "Authorization";
  private static final String ACCESS_TOKEN_HEADER = "Access-Token";
  private final int EXPIRE_TIME;

  public JwtHeaderHandler(@Value("${jwt.refresh-token-validity}") int expireTime) {
    EXPIRE_TIME = expireTime / 1000;
  }

  public void addAccessToken(HttpServletResponse response, String accessToken) {
    response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
  }

  public void addRefreshToken(HttpServletResponse response, String refreshToken) {
    response.addCookie(createCookie(REFRESH_TOKEN_COOKIE_HEADER, refreshToken));
  }

  private Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(EXPIRE_TIME);
//    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    return cookie;
  }

  public String getRefreshToken(HttpServletRequest request) {
    String refreshToken = extractCookie(request);
    if (refreshToken == null) {
      throw new CustomException(AuthExceptionCode.NOF_FOUND_REFRESH_TOKEN);
    }
    return refreshToken;
  }

  private String extractCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_HEADER)) {
        return cookie.getValue();
      }
    }
    return null;
  }

  public String getAccessToken(HttpServletRequest request) {
    String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
    if (accessToken == null) {
      throw new CustomException(AuthExceptionCode.NOF_FOUND_ACCESS_TOKEN);
    }
    return accessToken;
  }

  public void removeToken(HttpServletResponse response) {
    this.addAccessToken(response, null);
    this.addRefreshToken(response, null);
  }
}
