package com.arneam.pwdmanager.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class WebPassword implements Password {

  private String url;
  private String username;
  private String password;

}
