package com.nineteen.omp.ai.service;

import com.nineteen.omp.ai.client.ExternalApiClient;
import com.nineteen.omp.ai.client.MockApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

  private final ExternalApiClient externalApiClient;

  public AiServiceImpl() {
    externalApiClient = new MockApiClient();
  }


  @Override
  public String getAiResponse(String query) {
    return externalApiClient.fetchData("/test", query);
  }
}
