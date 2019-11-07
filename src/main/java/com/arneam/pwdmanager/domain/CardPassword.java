package com.arneam.pwdmanager.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class CardPassword implements Password {

  private String id;
  private String cardName;
  private String number;
  private String password;

}
