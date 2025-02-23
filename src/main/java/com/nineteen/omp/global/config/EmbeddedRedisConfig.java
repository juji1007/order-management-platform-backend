package com.nineteen.omp.global.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
//@Profile("local")
@Slf4j
public class EmbeddedRedisConfig {

  @Value("${spring.data.redis.port}")
  private int redisPort;

  private RedisServer redisService;

  @PostConstruct
  public void redisServer() {
    redisService = RedisServer.builder()
        .port(redisPort)
        .setting("maxmemory 128M") // maxmemory 설정
        .build();
    redisService.start();
  }

  @PreDestroy
  public void stopRedis() {
    if (redisService != null) {
      redisService.stop();
    }
  }
}
