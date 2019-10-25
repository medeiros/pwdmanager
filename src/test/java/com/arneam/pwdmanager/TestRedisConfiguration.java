package com.arneam.pwdmanager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

@TestConfiguration
public class TestRedisConfiguration {

  @Value("${redis.port}")
  private Integer redisPort;

  private RedisServer redisServer;

  @PostConstruct
  public void postConstruct() {
    this.redisServer = new RedisServerBuilder().port(redisPort).setting("maxmemory 256M").build();
    this.redisServer.start();
  }

  @PreDestroy
  public void preDestroy() {
    this.redisServer.stop();
  }

}
