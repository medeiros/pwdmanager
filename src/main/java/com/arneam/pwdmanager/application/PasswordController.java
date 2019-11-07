package com.arneam.pwdmanager.application;

import com.arneam.pwdmanager.domain.AccountNotFoundException;
import com.arneam.pwdmanager.domain.Password;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class PasswordController<E extends Password, R extends CrudRepository<E, String>> {

  abstract R repo();

  @GetMapping
  public Iterable<E> retrieveAllAccounts() {
    return repo().findAll();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> createAccount(@RequestBody() E account) {
    E savedAccount = repo().save(account);
    return new ResponseEntity<>(savedAccount.id(), HttpStatus.CREATED);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Password findById(@PathVariable("id") String id) {
    return repo().findById(id).orElseThrow(() -> new AccountNotFoundException(id));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<String> deleteAccount(@PathVariable("id") String id) {
    repo().findById(id).orElseThrow(() -> new AccountNotFoundException(id));

    repo().deleteById(id);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }

  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> updateAccount(@PathVariable("id") String id,
      @RequestBody() E account) {
    repo().findById(id).orElseThrow(() -> new AccountNotFoundException(id));

    repo().deleteById(id);
    repo().save(account);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }

  @ExceptionHandler(AccountNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String notFound(AccountNotFoundException ex) {
    return ex.getMessage();
  }

}
