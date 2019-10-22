package com.arneam.pwdmanager.infrastructure.queue;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.arneam.pwdmanager.RedisConfig;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RedisConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RedisMessageListenerIT {

  private static RedisServer redisServer;

  @Autowired
  private RedisMessagePublisher redisMessagePublisher;

  @BeforeClass
  public static void startRedisServer() {
    redisServer = new RedisServerBuilder().setting("maxmemory 256M").build();
    redisServer.start();
  }

  @AfterClass
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