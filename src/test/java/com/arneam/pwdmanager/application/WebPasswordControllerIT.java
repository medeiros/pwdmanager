package com.arneam.pwdmanager.application;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.arneam.pwdmanager.domain.WebPassword;
import com.arneam.pwdmanager.domain.WebPasswordRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "redis.port=6370")
@AutoConfigureMockMvc
class WebPasswordControllerIT extends TestPasswordController {

  @Autowired
  private WebPasswordController webPasswordController;

  @Autowired
  private WebPasswordRepository webPasswordRepository;

  @AfterEach
  void finalizeIt() {
    webPasswordRepository.deleteAll();
  }

  @Test
  void shouldValidateContextLoading() {
    assertThat(webPasswordController, is(notNullValue()));
  }

  @Test
  void shouldCreateAccount() throws Exception {
    String json = mapper.writeValueAsString(
        WebPassword.builder().id("google").url("google.com").username("jose").password("123safe")
            .build());

    mockMvc.perform(post("/api/webpasswords").contentType(MediaType.APPLICATION_JSON).content(json)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("google")));
  }

  @Test
  void shouldReadAllAccounts() throws Exception {
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

  @Test
  void shouldReadSpecificAccount() throws Exception {
    webPasswordRepository.save(
        WebPassword.builder().id("valor").url("valor.com").username("jose").password("123safe")
            .build());

    mockMvc.perform(get("/api/webpasswords/valor")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo("valor")))
        .andExpect(jsonPath("$.url", equalTo("valor.com")))
        .andExpect(jsonPath("$.username", equalTo("jose")))
        .andExpect(jsonPath("$.password", equalTo("123safe")));
  }

  @Test
  void shouldGetNotFoundExceptionWhenAccountIsNotFound() throws Exception {
    mockMvc.perform(get("/api/webpasswords/xxx")).andExpect(status().isNotFound())
        .andExpect(content().string(CoreMatchers.containsString("Account 'xxx' not found.")));
  }

  @Test
  void shouldDeleteExistingAccount() throws Exception {
    webPasswordRepository.save(
        WebPassword.builder().id("uol").url("uol.com").username("jose").password("123safe")
            .build());

    mockMvc.perform(delete("/api/webpasswords/uol")).andExpect(status().isOk())
        .andExpect(content().string(CoreMatchers.containsString("uol")));

    mockMvc.perform(get("/api/webpasswords/uol")).andExpect(status().isNotFound())
        .andExpect(content().string(CoreMatchers.containsString("Account 'uol' not found.")));
  }

  @Test
  void shouldNotDeleteInexistingAccount() throws Exception {
    mockMvc.perform(delete("/api/webpasswords/infomoney")).andExpect(status().isNotFound())
        .andExpect(content().string(CoreMatchers.containsString("Account 'infomoney' not found.")));
  }

  @Test
  void shouldUpdateExistingAccount() throws Exception {
    webPasswordRepository.save(
        WebPassword.builder().id("godaddy").url("google.com").username("jose").password("123safe")
            .build());

    String json = mapper.writeValueAsString(
        WebPassword.builder().id("godaddy").url("godaddy.com").username("jose").password("123safe")
            .build());

    mockMvc.perform(
        put("/api/webpasswords/godaddy").contentType(MediaType.APPLICATION_JSON).content(json)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("godaddy")));

    mockMvc.perform(get("/api/webpasswords/godaddy")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo("godaddy")))
        .andExpect(jsonPath("$.url", equalTo("godaddy.com")))
        .andExpect(jsonPath("$.username", equalTo("jose")))
        .andExpect(jsonPath("$.password", equalTo("123safe")));
  }

  @Test
  void shouldNotUpdateInexistingAccount() throws Exception {
    String json = mapper.writeValueAsString(
        WebPassword.builder().id("gmail").url("gmail.com").username("jose").password("123safe")
            .build());

    mockMvc.perform(
        put("/api/webpasswords/gmail").contentType(MediaType.APPLICATION_JSON).content(json)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andExpect(
        MockMvcResultMatchers.content()
            .string(CoreMatchers.containsString("Account " + "'gmail' not found.")));
  }

}
