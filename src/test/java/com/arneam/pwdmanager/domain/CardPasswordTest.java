package com.arneam.pwdmanager.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class CardPasswordTest {

  @Test
  void shouldCreateCardPasword() {
    String cardNameData = "nubank";
    String numberData = "1212323-2";
    String pwd = "aabbcc12";

    CardPassword password =
        CardPassword.builder().cardName(cardNameData).number(numberData).password(pwd).build();
    assertThat(password.cardName(), is(equalTo(cardNameData)));
    assertThat(password.number(), is(equalTo(numberData)));
    assertThat(password.password(), is(equalTo(pwd)));
  }

}
