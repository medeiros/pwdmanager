package com.arneam.pwdmanager.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class DevicePassword implements Password {

  private String id;
  private String device;
  private String username;
  private String password;

}
