package com.arneam.pwdmanager.application;

import com.arneam.pwdmanager.domain.WebPassword;
import com.arneam.pwdmanager.domain.WebPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webpasswords")
public class WebPasswordController {

  @Autowired
  private WebPasswordRepository webPasswordRepository;

  @GetMapping("/")
  public Iterable<WebPassword> retrieveAllWebAccounts() {
    return webPasswordRepository.findAll();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> createPassword(@RequestBody() WebPassword webPassword) {
    WebPassword savedPassword = webPasswordRepository.save(webPassword);
    return new ResponseEntity<>(savedPassword.id(), HttpStatus.CREATED);
  }

}
