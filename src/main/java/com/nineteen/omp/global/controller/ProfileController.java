package com.nineteen.omp.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProfileController {

  @Value("${spring.profiles.active}")
  private final String profile;

  @GetMapping("/profile")
  public String getProfile() {
    return profile;
  }
}
