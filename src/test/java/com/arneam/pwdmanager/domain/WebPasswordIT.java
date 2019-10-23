package com.arneam.pwdmanager.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WebPasswordIT {

  @Autowired
  private WebPasswordRepository webPasswordRepository;
  private WebPassword webPasswordGmail;

  @BeforeEach
  void init() {
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
  public void shouldSaveAndRetrieveAllRecords() {
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
