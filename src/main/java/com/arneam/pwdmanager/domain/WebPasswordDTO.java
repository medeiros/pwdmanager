package com.arneam.pwdmanager.domain;

import lombok.Data;

@Data
public class WebPasswordDTO {

  private String id;
  private String url;
  private String username;
  private String password;

}
