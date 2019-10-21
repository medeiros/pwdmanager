package com.arneam.pwdmanager.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebPasswordRepository extends CrudRepository<WebPassword, String> {
}
