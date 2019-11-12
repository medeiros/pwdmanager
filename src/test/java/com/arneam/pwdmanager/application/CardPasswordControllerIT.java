package com.arneam.pwdmanager.application;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.arneam.pwdmanager.domain.CardPassword;
import com.arneam.pwdmanager.domain.CardPasswordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
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
public class CardPasswordControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private CardPasswordController cardPasswordController;

  @Autowired
  private CardPasswordRepository cardPasswordRepository;

  @AfterEach
  void finalizeIt() {
    cardPasswordRepository.deleteAll();
  }

  @Test
  void shouldValidateContextLoading() {
    assertThat(cardPasswordController, is(notNullValue()));
  }

  @Test
  void shouldCreateAccount() throws Exception {
    String json = mapper.writeValueAsString(
        CardPassword.builder().id("nubank").cardName("123123123123").number("7744332")
            .password("123safe").build());

    mockMvc.perform(post("/api/cardpasswords").contentType(MediaType.APPLICATION_JSON).content(json)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("nubank")));
  }

  @Test
  void shouldReadAllAccounts() throws Exception {
    cardPasswordRepository.save(
        CardPassword.builder().id("nubank").cardName("nubank").number("7744332").password("123safe")
            .build());
    cardPasswordRepository.save(
        CardPassword.builder().id("santander").cardName("santander").number("1123")
            .password("aaasafe").build());

    mockMvc.perform(get("/api/cardpasswords").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$[*]", hasSize(2)))
        .andExpect(jsonPath("$[0].id", anyOf(equalTo("nubank"), equalTo("santander"))))
        .andExpect(jsonPath("$[1].id", anyOf(equalTo("nubank"), equalTo("santander"))))
        .andExpect(jsonPath("$[0].cardName", anyOf(equalTo("nubank"), equalTo("santander"))))
        .andExpect(jsonPath("$[1].cardName", anyOf(equalTo("nubank"), equalTo("santander"))))
        .andExpect(jsonPath("$[0].number", anyOf(equalTo("7744332"), equalTo("1123"))))
        .andExpect(jsonPath("$[1].number", anyOf(equalTo("7744332"), equalTo("1123"))))
        .andExpect(jsonPath("$[0].password", anyOf(equalTo("123safe"), equalTo("aaasafe"))))
        .andExpect(jsonPath("$[1].password", anyOf(equalTo("123safe"), equalTo("aaasafe"))));
  }

  @Test
  void shouldReadSpecificAccount() throws Exception {
    cardPasswordRepository.save(
        CardPassword.builder().id("itau").cardName("itau").number("7744332").password("123safe")
            .build());

    mockMvc.perform(get("/api/cardpasswords/itau")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo("itau")))
        .andExpect(jsonPath("$.cardName", equalTo("itau")))
        .andExpect(jsonPath("$.number", equalTo("7744332")))
        .andExpect(jsonPath("$.password", equalTo("123safe")));
  }

  @Test
  void shouldGetNotFoundExceptionWhenAccountIsNotFound() throws Exception {
    mockMvc.perform(get("/api/cardpasswords/xxx")).andExpect(status().isNotFound())
        .andExpect(content().string(CoreMatchers.containsString("Account 'xxx' not found.")));
  }

  @Test
  void shouldDeleteExistingAccount() throws Exception {
    cardPasswordRepository.save(
        CardPassword.builder().id("bradesco").cardName("bradesco").number("7744332")
            .password("123safe").build());

    mockMvc.perform(delete("/api/cardpasswords/bradesco")).andExpect(status().isOk())
        .andExpect(content().string(CoreMatchers.containsString("bradesco")));

    mockMvc.perform(get("/api/cardpasswords/bradesco")).andExpect(status().isNotFound())
        .andExpect(content().string(CoreMatchers.containsString("Account 'bradesco' not found.")));
  }

  @Test
  void shouldNotDeleteInexistingAccount() throws Exception {
    mockMvc.perform(delete("/api/cardpasswords/bb")).andExpect(status().isNotFound())
        .andExpect(content().string(CoreMatchers.containsString("Account 'bb' not found.")));
  }

  @Test
  void shouldUpdateExistingAccount() throws Exception {
    cardPasswordRepository.save(
        CardPassword.builder().id("caixa").cardName("caixa").number("7744332").password("123safe")
            .build());

    String json = mapper.writeValueAsString(
        CardPassword.builder().id("caixa").cardName("caixa").number("7744332").password("123safe")
            .build());

    mockMvc.perform(
        put("/api/cardpasswords/caixa").contentType(MediaType.APPLICATION_JSON).content(json)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("caixa")));

    mockMvc.perform(get("/api/cardpasswords/caixa")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo("caixa")))
        .andExpect(jsonPath("$.cardName", equalTo("caixa")))
        .andExpect(jsonPath("$.number", equalTo("7744332")))
        .andExpect(jsonPath("$.password", equalTo("123safe")));
  }

  @Test
  void shouldNotUpdateInexistingAccount() throws Exception {
    String json = mapper.writeValueAsString(
        CardPassword.builder().id("sofisa").cardName("sofisa").number("7744332").password("123safe")
            .build());

    mockMvc.perform(
        put("/api/cardpasswords/sofisa").contentType(MediaType.APPLICATION_JSON).content(json)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andExpect(
        MockMvcResultMatchers.content()
            .string(CoreMatchers.containsString("Account 'sofisa' not found.")));
  }

}
