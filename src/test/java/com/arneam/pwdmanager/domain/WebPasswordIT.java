package com.arneam.pwdmanager.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.arneam.pwdmanager.RedisConfig;
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
public class WebPasswordIT {

  private static RedisServer redisServer;

  @Autowired
  private WebPasswordRepository webPasswordRepository;

  @BeforeClass
  public static void startRedisServer() {
    redisServer = new RedisServerBuilder().setting("maxmemory 128M").build();
    redisServer.start();
  }

  @AfterClass
  public static void stopRedisServer() {
    redisServer.stop();
  }

  @Test
  public void shouldSaveWebPassword() {
    final WebPassword webPassword =
        WebPassword.builder().id("gmail").url("gmail.com").username("jose").password("123safe")
            .build();
    webPasswordRepository.save(webPassword);
    WebPassword retrievedWebPassword = webPasswordRepository.findById("gmail").get();
    assertThat(webPassword.url(), is(equalTo(retrievedWebPassword.url())));
  }

}
