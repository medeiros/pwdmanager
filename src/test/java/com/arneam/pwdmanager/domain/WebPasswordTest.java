package com.arneam.pwdmanager.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class WebPasswordTest {

  @Test
  void shouldCreateWebPasword() {
    String url = "gmail.com";
    String user = "jose";
    String pwd = "aabbcc12";

    WebPassword password = WebPassword.builder().url(url).username(user).password(pwd).build();
    assertThat(password.url(), is(equalTo(url)));
    assertThat(password.password(), is(equalTo(pwd)));
  }

}
