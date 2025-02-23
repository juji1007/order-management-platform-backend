package com.nineteen.omp.ai.client;

import com.nineteen.omp.ai.exception.AiException;
import com.nineteen.omp.ai.exception.AiExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RealAiApiClient implements ExternalApiClient {

  private final RestTemplate restTemplate;
  private final String BASE_URL = "http://api.example.com";

  @Override
  public String fetchData(String uri, String param) {
    String url = BASE_URL + uri + "?query=" + param;
    try {
      return restTemplate.getForObject(url, String.class);
    } catch (Exception e) {
      throw new AiException(AiExceptionCode.AI_REQUEST_FAILED);
    }
  }
}
