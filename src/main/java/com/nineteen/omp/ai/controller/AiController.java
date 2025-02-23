package com.nineteen.omp.ai.controller;

import com.nineteen.omp.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiController {

  private final AiService aiService;

  @GetMapping("/response")
  public String getAiResponse(@RequestParam String query) {
    return aiService.getAiResponse(query);
  }
}
