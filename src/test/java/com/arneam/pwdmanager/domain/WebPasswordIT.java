package com.arneam.pwdmanager.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WebPasswordIT {

  private static RedisServer redisServer;

  @Autowired
  private WebPasswordRepository webPasswordRepository;
  private WebPassword webPasswordGmail;

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

  @BeforeEach
  public void init() {
    this.webPasswordGmail =
        WebPassword.builder().id("gmail").url("gmail.com").username("jose").password("123safe")
            .build();
  }

  @Test
  public void shouldSaveAndRetrieveRecord() {
    webPasswordRepository.save(webPasswordGmail);
    WebPassword retrievedWebPassword = webPasswordRepository.findById("gmail").get();
    assertThat(webPasswordGmail.url(), is(equalTo(retrievedWebPassword.url())));
  }

  @Test
  public void shouldUpdateAndRetrieveRecord() {
    webPasswordRepository.save(webPasswordGmail);
    webPasswordGmail.password("safe123");
    webPasswordRepository.save(webPasswordGmail);
    WebPassword retrievedWebPassword = webPasswordRepository.findById("gmail").get();
    assertThat(webPasswordGmail.password(), is(equalTo(retrievedWebPassword.password())));
  }

  @Test
  public void shouldSaveAndRetreiveAllRecords() {
    final WebPassword webPasswordTwitter =
        WebPassword.builder().id("twitter").url("twitter.com").username("jose").password("aaasafe")
            .build();
    webPasswordRepository.save(webPasswordGmail);
    webPasswordRepository.save(webPasswordTwitter);

    List<WebPassword> passwords = new ArrayList<>();
    webPasswordRepository.findAll().forEach(passwords::add);

    assertThat(passwords.size(), is(equalTo(2)));
  }

  @Test
  public void shouldDeleteAndNotRetrieveDeletedRecord() {
    webPasswordRepository.save(webPasswordGmail);
    webPasswordRepository.deleteById("gmail");
    final WebPassword password = webPasswordRepository.findById("gmail").orElse(null);
    assertThat(password, is(nullValue()));
  }

}
