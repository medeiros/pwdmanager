package com.arneam.pwdmanager.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.arneam.pwdmanager.domain.Password;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
abstract class TestPasswordController<T> {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper mapper;

  CrudRepository<T, String> repository;
  PasswordController passwordController;

  TestPasswordController(CrudRepository<T, String> repository,
      PasswordController passwordController) {
    this.repository = repository;
    this.passwordController = passwordController;
  }

  @AfterEach
  void finalizeIt() {
    repository.deleteAll();
  }

  @Test
  void shouldValidateContextLoading() {
    assertThat(passwordController, is(notNullValue()));
  }

  String jsonOf(Password password) throws JsonProcessingException {
    return mapper.writeValueAsString(password);
  }

  void mockPost(String endpoint, String json, Matcher<? super String> matcher) throws Exception {
    mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(json)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string(matcher));
  }

  @Test
  abstract void shouldCreateAccount() throws Exception;

}
