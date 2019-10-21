package com.arneam.pwdmanager.infrastructure.queue;

public interface MessagePublisher {

  void publish(final String message);

}
