package com.arneam.pwdmanager.domain;

import static java.lang.String.format;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountNotFoundException extends RuntimeException {

  private String id;

  @Override
  public String getMessage() {
    return format("Account '%s' not found.", id);
  }

}
