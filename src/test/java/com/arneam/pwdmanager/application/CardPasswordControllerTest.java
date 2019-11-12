package com.arneam.pwdmanager.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.jupiter.api.Test;

class CardPasswordControllerTest {

  @Test
  void shouldCreateController() {
    CardPasswordController controller = new CardPasswordController();
    assertThat(controller, is(notNullValue()));
  }

}
