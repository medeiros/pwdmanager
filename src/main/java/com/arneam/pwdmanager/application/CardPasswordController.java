package com.arneam.pwdmanager.application;

import com.arneam.pwdmanager.domain.CardPassword;
import com.arneam.pwdmanager.domain.CardPasswordRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cardpasswords")
public class CardPasswordController extends PasswordController<CardPassword, CardPasswordRepository> {

  @Autowired
  public CardPasswordController(@NonNull CardPasswordRepository cardPasswordRepository) {
    super(cardPasswordRepository);
  }

}
