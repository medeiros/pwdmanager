package com.arneam.pwdmanager.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.jupiter.api.Test;

class WebPasswordControllerTest {

  @Test
  void shouldCreateController() {
    WebPasswordController controller = new WebPasswordController();
    assertThat(controller, is(notNullValue()));
  }

}
