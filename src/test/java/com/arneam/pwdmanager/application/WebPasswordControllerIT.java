package com.arneam.pwdmanager.application;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.arneam.pwdmanager.domain.WebPassword;
import com.arneam.pwdmanager.domain.WebPasswordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "redis.port=6370")
@AutoConfigureMockMvc
class WebPasswordControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private WebPasswordController webPasswordController;

  @Autowired
  private WebPasswordRepository webPasswordRepository;

  @Test
  void shouldValidateContextLoading() {
    assertThat(webPasswordController, is(notNullValue()));
  }

  @Test
  void shouldCreateAndReadPassword() throws Exception {
    WebPassword webPasswordGmail =
        WebPassword.builder().id("gmail").url("gmail.com").username("jose").password("123safe")
            .build();

    String json = mapper.writeValueAsString(webPasswordGmail);

    mockMvc.perform(post("/api/webpasswords").contentType(MediaType.APPLICATION_JSON).content(json)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("gmail")));
  }

  @Test
  void shouldRetrieveAllPasswords() throws Exception {
    webPasswordRepository.save(
        WebPassword.builder().id("gmail").url("gmail.com").username("jose").password("123safe")
            .build());
    webPasswordRepository.save(
        WebPassword.builder().id("twitter").url("twitter.com").username("joao").password("aaasafe")
            .build());

    mockMvc.perform(get("/api/webpasswords").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$[*]", hasSize(2)))
        .andExpect(jsonPath("$[0].id", anyOf(equalTo("gmail"), equalTo("twitter"))))
        .andExpect(jsonPath("$[1].id", anyOf(equalTo("gmail"), equalTo("twitter"))))
        .andExpect(jsonPath("$[0].url", anyOf(equalTo("gmail.com"), equalTo("twitter.com"))))
        .andExpect(jsonPath("$[1].url", anyOf(equalTo("gmail.com"), equalTo("twitter.com"))))
        .andExpect(jsonPath("$[0].username", anyOf(equalTo("jose"), equalTo("joao"))))
        .andExpect(jsonPath("$[1].username", anyOf(equalTo("jose"), equalTo("joao"))))
        .andExpect(jsonPath("$[0].password", anyOf(equalTo("123safe"), equalTo("aaasafe"))))
        .andExpect(jsonPath("$[1].password", anyOf(equalTo("123safe"), equalTo("aaasafe"))));
  }

  // TODO finish testing other crud methods: https://dimitr.im/testing-your-rest-controllers-and-clients-with-spring

}
