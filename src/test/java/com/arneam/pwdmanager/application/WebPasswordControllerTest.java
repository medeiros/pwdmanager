package com.arneam.pwdmanager.application;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.arneam.pwdmanager.domain.WebPasswordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WebPasswordControllerTest {

  @Mock WebPasswordRepository webPasswordRepository;

  @BeforeEach
  void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void shouldCreateController() {
    WebPasswordController controller = new WebPasswordController(webPasswordRepository);
    assertThat(controller, is(notNullValue()));
  }

  @Test
  void shouldNotCreateControllerWhenRepoIsNull() {
    Throwable t = assertThrows(NullPointerException.class, () -> new WebPasswordController(null));
    assertThat(t.getMessage(), is(equalTo("webPasswordRepository is marked non-null but is null")));
  }

}
