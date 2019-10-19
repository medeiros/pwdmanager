package com.arneam.pwdmanager.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class DevicePasswordTest {

  @Test
  void shouldCreateDevicePasword() {
    String deviceName = "roteador vivo";
    String usernameData = "1212323-2";
    String pwd = "aabbcc12";

    DevicePassword password =
        DevicePassword.builder().device(deviceName).username(usernameData).password(pwd).build();
    assertThat(password.device(), is(equalTo(deviceName)));
    assertThat(password.username(), is(equalTo(usernameData)));
    assertThat(password.password(), is(equalTo(pwd)));
  }

}
