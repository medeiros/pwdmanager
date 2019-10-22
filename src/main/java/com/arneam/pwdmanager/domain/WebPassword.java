package com.arneam.pwdmanager.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@Setter
@Accessors(fluent = true)
@RedisHash("WebPassword")
public class WebPassword implements Password {

  private String id;
  private String url;
  private String username;
  private String password;

}
