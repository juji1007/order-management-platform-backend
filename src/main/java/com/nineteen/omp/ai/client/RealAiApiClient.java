package com.nineteen.omp.ai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineteen.omp.ai.exception.AiException;
import com.nineteen.omp.ai.exception.AiExceptionCode;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RealAiApiClient implements ExternalApiClient {

  private final RestTemplate restTemplate;

  private static final String API_KEY = "EXAMPLE_API_KEY";
  private static final String BASE_URL = "https://api.openai.com/v1/chat/completions";

  public RealAiApiClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public String fetchData(String uri, String param) {
    try {
      String responseBody = getResponseBody(param);
      return extractMessageFromResponse(responseBody);
    } catch (Exception e) {
      throw new AiException(AiExceptionCode.AI_REQUEST_FAILED);
    }
  }

  private String getResponseBody(String param) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequestBody(param), headers);
    ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity,
        String.class);
    return response.getBody();
  }

  private String extractMessageFromResponse(String responseBody) throws Exception {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(responseBody);
      JsonNode messageNode = rootNode.path("choices").get(0).path("message");
      return messageNode.path("content").asText();
    } catch (JsonProcessingException e) {
      throw new AiException(AiExceptionCode.AI_RESPONSE_PARSE_FAILED);
    }
  }


  private HttpHeaders createHeaders() {
    if (API_KEY == null) {
      throw new AiException(AiExceptionCode.AI_API_KEY_MISSING);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Authorization", "Bearer " + API_KEY);
    return headers;
  }

  private Map<String, Object> createRequestBody(String param) {
    Map<String, Object> body = new HashMap<>();
    body.put("model", "gpt-4o-mini");

    String PromptOrderMsg = "이 음식에 대해 간단히 설명하고, 고객이 관심을 가질 수 있도록 두 줄 정도로 매력적으로 소개해 주세요. 추가적인 정보는 필요하지 않습니다.";
    String prompt = param + PromptOrderMsg;

    body.put("messages", new Object[]{
        Map.of("role", "user", "content", prompt)
    });

    return body;
  }
}
