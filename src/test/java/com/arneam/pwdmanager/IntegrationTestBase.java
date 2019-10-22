package com.arneam.pwdmanager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

public class IntegrationTestBase {

  private static RedisServer redisServer;

  @BeforeAll
  public static void startRedisServer() {
    if (redisServer == null) {
      redisServer =
          new RedisServerBuilder().setting("maxmemory 256M").port(6380).setting("timeout 0")
              .build();
      redisServer.start();
    }
  }

  protected void startRedisIfInactive() {
    if (!redisServer.isActive()) {
      redisServer.start();
    }
  }

  @AfterAll
  public static void stopRedisServer() {
    redisServer.stop();
  }

}
