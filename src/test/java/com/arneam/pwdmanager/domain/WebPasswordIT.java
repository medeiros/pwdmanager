package com.arneam.pwdmanager.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.arneam.pwdmanager.RedisConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
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
public class WebPasswordIT {

  private static RedisServer redisServer;

  @Autowired
  private WebPasswordRepository webPasswordRepository;
  private WebPassword webPasswordGmail;

  @BeforeClass
  public static void startRedisServer() {
    redisServer = new RedisServerBuilder().setting("maxmemory 128M").build();
    redisServer.start();
  }

  @AfterClass
  public static void stopRedisServer() {
    redisServer.stop();
  }

  @Before
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
