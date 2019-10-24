package com.arneam.pwdmanager.infrastructure.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

  private static List<String> messageList = new ArrayList<>();

  @Override
  public void onMessage(final Message message, final byte[] bytes) {
    messageList.add(message.toString());
    System.out.print("Message received: " + new String(message.getBody()));
  }

  static List<String> messageList() {
    return Collections.unmodifiableList(messageList);
  }

}
