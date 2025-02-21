package com.nineteen.omp.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineteen.omp.auth.handler.CustomAuthenticationSuccessHandler;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.user.controller.dto.LoginRequestDto;
import com.nineteen.omp.user.exception.UserExceptionCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final CustomAuthenticationSuccessHandler successHandler;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
      CustomAuthenticationSuccessHandler successHandler) {
    this.authenticationManager = authenticationManager;
    this.successHandler = successHandler;
    setFilterProcessesUrl("/api/v1/users/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      String requestBody = new String(request.getInputStream().readAllBytes());

      LoginRequestDto loginRequest = new ObjectMapper().readValue(requestBody,
          LoginRequestDto.class);

      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(loginRequest.username(),
              loginRequest.password());

      return authenticationManager.authenticate(authenticationToken);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (AuthenticationException e) {
      throw new CustomException(UserExceptionCode.USER_NOT_FOUND);
    }
  }

  // 로그인 성공 시 호출
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    SecurityContextHolder.getContext().setAuthentication(authResult);

    successHandler.onAuthenticationSuccess(request, response, authResult);
  }

  // 로그인 실패 시 호출(작동 안함)
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("{\"message\": \"login failed!\"}");
  }

}
