package com.arneam.pwdmanager.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class WebPasswordTest {

  @Test
  void shouldCreateWebPasword() {
    String id = "gmail";
    String url = "gmail.com";
    String user = "jose";
    String pwd = "aabbcc12";

    WebPassword password =
        WebPassword.builder().id(id).url(url).username(user).password(pwd).build();
    assertThat(password.id(), is(equalTo(id)));
    assertThat(password.url(), is(equalTo(url)));
    assertThat(password.username(), is(equalTo(user)));
    assertThat(password.password(), is(equalTo(pwd)));
  }

}
