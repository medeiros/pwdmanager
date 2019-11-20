package com.arneam.pwdmanager.application;

import com.arneam.pwdmanager.domain.WebPassword;
import com.arneam.pwdmanager.domain.WebPasswordRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webpasswords")
public class WebPasswordController extends PasswordController<WebPassword, WebPasswordRepository> {

  @Autowired
  public WebPasswordController(@NonNull WebPasswordRepository webPasswordRepository) {
    super(webPasswordRepository);
  }

}
