package com.arneam.pwdmanager.infrastructure.queue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"redis.port=6372" })
class RedisMessageListenerIT {

  @Autowired
  private RedisMessagePublisher redisMessagePublisher;

  @Test
  void shouldTestOnMessage() throws InterruptedException {
    String message = "Message: " + UUID.randomUUID().toString();
    redisMessagePublisher.publish(message);
    Thread.sleep(1000);
    assertThat(RedisMessageSubscriber.messageList().get(0), containsString(message));
  }

}