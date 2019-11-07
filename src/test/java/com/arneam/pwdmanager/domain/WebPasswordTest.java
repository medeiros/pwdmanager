package com.arneam.pwdmanager.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

  @ParameterizedTest
  @CsvSource({",gmail.com,jose,teste123", "gmail,,jose,teste123", "gmail,gmail.com,,teste123",
      "gmail,gmail.com,jose,", ",,,"})
  void shouldNotCreatePasswordIfFieldsAreNull(String id, String url, String user, String pwd) {
    Throwable t = assertThrows(NullPointerException.class,
        () -> WebPassword.builder().id(id).url(url).username(user).password(pwd).build());
    assertThat(t.getMessage(), containsString("is marked non-null but is null"));
  }

}
