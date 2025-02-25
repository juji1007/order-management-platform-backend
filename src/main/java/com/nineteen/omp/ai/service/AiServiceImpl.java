package com.nineteen.omp.ai.service;

import com.nineteen.omp.ai.client.ExternalApiClient;
import com.nineteen.omp.ai.client.RealAiApiClient;
import com.nineteen.omp.ai.domain.AiRequestHistory;
import com.nineteen.omp.ai.repository.AiRequestHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiServiceImpl implements AiService {

  private final ExternalApiClient externalApiClient;
  private final AiRequestHistoryRepository aiRequestHistoryRepository;

  public AiServiceImpl(AiRequestHistoryRepository aiRequestHistoryRepository,
      RestTemplate restTemplate) {
    this.externalApiClient = new RealAiApiClient(restTemplate);
    this.aiRequestHistoryRepository = aiRequestHistoryRepository;
  }

  @Override
  public String getAiResponse(String query) {

    AiRequestHistory aiRequestHistory = AiRequestHistory.builder()
        .aiServiceName("Open AI Service")
        .query(query)
        .build();

    String response = externalApiClient.fetchData("/test", query);

    response = AiRequestHistory.limitResponseLength(response);

    aiRequestHistory = aiRequestHistory.toBuilder()
        .response(response)
        .build();

    aiRequestHistoryRepository.save(aiRequestHistory);

    return response;
  }
}
