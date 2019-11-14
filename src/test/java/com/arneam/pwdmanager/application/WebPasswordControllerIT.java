package com.arneam.pwdmanager.application;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.arneam.pwdmanager.domain.WebPassword;
import com.arneam.pwdmanager.domain.WebPasswordRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class WebPasswordControllerIT extends TestPasswordController {

  @Autowired
  WebPasswordControllerIT(WebPasswordRepository repository, WebPasswordController controller) {
    super(repository, controller);
  }

  @Test
  @Override
  void shouldCreateAccount() throws Exception {
    mockPost("/api/webpasswords", jsonOf(
        WebPassword.builder().id("google").url("google.com").username("jose").password("123safe")
            .build()), CoreMatchers.containsString("google"));
  }

  @Test
  void shouldReadAllAccounts() throws Exception {
    repository.save(
        WebPassword.builder().id("gmail").url("gmail.com").username("jose").password("123safe")
            .build());
    repository.save(
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
    repository.save(
        WebPassword.builder().id("valor").url("valor.com").username("jose").password("123safe")
            .build());

    mockMvc.perform(get("/api/webpasswords/valor")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo("valor")))
        .andExpect(jsonPath("$.url", equalTo("valor.com")))
        .andExpect(jsonPath("$.username", equalTo("jose")))
        .andExpect(jsonPath("$.password", equalTo("123safe")));
  }

  @Test
  @Override
  void shouldGetNotFoundExceptionWhenAccountIsNotFound() throws Exception {
    mockGetNotFoundUrl("/api/webpasswords/xxx");
  }

  @Test
  void shouldDeleteExistingAccount() throws Exception {
    repository.save(
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
    repository.save(
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
