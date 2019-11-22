package com.arneam.pwdmanager.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.arneam.pwdmanager.domain.Password;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "redis.port=6370")
@AutoConfigureMockMvc
@AllArgsConstructor
abstract class TestPasswordController<T> {

  CrudRepository<T, String> repository;
  PasswordController controller;
  MockMvc mockMvc;
  ObjectMapper mapper;

  @AfterEach
  void finalizeIt() {
    repository.deleteAll();
  }

  @Test
  void shouldValidateContextLoading() {
    assertThat(controller, is(notNullValue()));
  }

  String jsonOf(Password password) throws JsonProcessingException {
    return mapper.writeValueAsString(password);
  }

  void mockPost(String endpoint, String json, Matcher<? super String> matcher) throws Exception {
    mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(json)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string(matcher));
  }

  void mockGetNotFoundUrl(String endpoint) throws Exception {
    mockMvc.perform(get(endpoint)).andExpect(status().isNotFound()).andExpect(content().string(
        CoreMatchers.containsString(
            "Account '" + endpoint.substring(endpoint.lastIndexOf("/") + 1) + "' not found.")));
  }

  @Test
  abstract void shouldCreateAccount() throws Exception;

  @Test
  abstract void shouldGetNotFoundExceptionWhenAccountIsNotFound() throws Exception;

}
