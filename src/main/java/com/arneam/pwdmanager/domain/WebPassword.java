package com.arneam.pwdmanager.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@Accessors(fluent = true)
@RedisHash("WebPassword")
@JsonDeserialize(builder = WebPassword.WebPasswordBuilder.class)
public final class WebPassword implements Password {

  @JsonProperty
  private String id;

  @JsonProperty
  private String url;

  @JsonProperty
  private String username;

  @JsonProperty
  private String password;

}
