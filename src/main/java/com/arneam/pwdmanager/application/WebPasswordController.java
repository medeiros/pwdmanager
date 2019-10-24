package com.arneam.pwdmanager.application;

import com.arneam.pwdmanager.domain.WebPassword;
import com.arneam.pwdmanager.domain.WebPasswordDTO;
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
  public @ResponseBody
  String allWebAccounts() {
    return "all web password accounts";
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> createPassword(@RequestBody() WebPasswordDTO dto) {
    WebPassword webPassword =
        WebPassword.builder().id(dto.getId()).url(dto.getUrl()).username(dto.getUsername())
            .password(dto.getPassword()).build();
    WebPassword savedPassword = webPasswordRepository.save(webPassword);
    return new ResponseEntity<>(savedPassword.id(), HttpStatus.CREATED);
  }

}
