package com.arneam.pwdmanager.application;

import com.arneam.pwdmanager.domain.AccountNotFoundException;
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

  @GetMapping
  public Iterable<WebPassword> retrieveAllWebAccounts() {
    return webPasswordRepository.findAll();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> createPassword(@RequestBody() WebPassword webPassword) {
    WebPassword savedPassword = webPasswordRepository.save(webPassword);
    return new ResponseEntity<>(savedPassword.id(), HttpStatus.CREATED);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebPassword findById(@PathVariable("id") String id) {
    return webPasswordRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<String> deletePassword(@PathVariable("id") String id) {
    webPasswordRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));

    webPasswordRepository.deleteById(id);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }

  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> updatePassword(@PathVariable("id") String id,
      @RequestBody() WebPassword webPassword) {
    webPasswordRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));

    webPasswordRepository.deleteById(id);
    webPasswordRepository.save(webPassword);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }

  @ExceptionHandler(AccountNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String notFound(AccountNotFoundException ex) {
    return ex.getMessage();
  }

}
