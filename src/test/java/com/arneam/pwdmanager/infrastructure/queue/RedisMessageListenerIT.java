package com.arneam.pwdmanager.infrastructure.queue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RedisMessageListenerIT {

  private static RedisServer redisServer;

  @Autowired
  private RedisMessagePublisher redisMessagePublisher;

  @BeforeAll
  public static void startRedisServer() {
    redisServer =
        new RedisServerBuilder().setting("maxmemory 256M").setting("timeout 0").setting("tcp" +
            "-keepalive 3000").setting("databases 16").build();
    redisServer.start();
  }

  @AfterAll
  public static void stopRedisServer() {
    redisServer.stop();
  }

  @Test
  public void shouldTestOnMessage() throws InterruptedException {
    String message = "Message: " + UUID.randomUUID().toString();
    redisMessagePublisher.publish(message);
    Thread.sleep(1000);
    assertThat(RedisMessageSubscriber.messageList.get(0), containsString(message));
  }

}