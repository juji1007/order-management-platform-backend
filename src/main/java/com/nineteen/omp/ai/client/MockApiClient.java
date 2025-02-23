package com.nineteen.omp.ai.client;

import org.springframework.stereotype.Component;

@Component
public class MockApiClient implements ExternalApiClient {

  @Override
  public String fetchData(String uri, String param) {
    return "이것은 목 데이터입니다. 이후 실제로 외부 API에 요청을 보내주세요!, 쿼리 : " + param;
  }
}

