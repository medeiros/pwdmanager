package com.arneam.pwdmanager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

@TestConfiguration
public class TestRedisConfiguration {

  private RedisServer redisServer;

  public TestRedisConfiguration() {
    this.redisServer = new RedisServerBuilder().setting("maxmemory 256M").build();
  }

  @PostConstruct
  public void postConstruct() {
    this.redisServer.start();
  }

  @PreDestroy
  public void preDestroy() {
    this.redisServer.stop();
  }

}
