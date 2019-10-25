package com.arneam.pwdmanager.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.arneam.pwdmanager.domain.WebPassword;
import com.arneam.pwdmanager.domain.WebPasswordDTO;
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

  @Test
  void shouldValidateContextLoading() {
    assertThat(webPasswordController, is(notNullValue()));
  }

  @Test
  void shouldSaveAndRetrievePassword() throws Exception {
    WebPassword webPasswordGmail =
        WebPassword.builder().id("gmail").url("gmail.com").username("jose").password("123safe")
            .build();

    WebPasswordDTO dto = new WebPasswordDTO();
    dto.setId(webPasswordGmail.id());
    dto.setPassword(webPasswordGmail.password());
    dto.setUrl(webPasswordGmail.url());
    dto.setUsername(webPasswordGmail.username());

    String json = mapper.writeValueAsString(dto);

    mockMvc.perform(post("/api/webpasswords").contentType(MediaType.APPLICATION_JSON).content(json)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("gmail")));
  }

  @Test
  void shouldCreateWebPasswordService() throws Exception {
    String endpoint = "/api/webpasswords/";
    String expectedText = "all web password accounts";

    mockMvc.perform(get(endpoint)).andExpect(status().isOk()).andExpect(
        MockMvcResultMatchers.content().string(CoreMatchers.containsString(expectedText)));
  }

}
