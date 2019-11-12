package com.arneam.pwdmanager.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@Accessors(fluent = true)
@RedisHash("CardPassword")
@JsonDeserialize(builder = CardPassword.CardPasswordBuilder.class)
public final class CardPassword implements Password {

  @JsonProperty
  @NonNull
  private String id;

  @JsonProperty
  @NonNull
  private String cardName;

  @JsonProperty
  @NonNull
  private String number;

  @JsonProperty
  @NonNull
  private String password;

}
