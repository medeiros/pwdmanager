package com.arneam.pwdmanager.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardPasswordRepository extends CrudRepository<CardPassword, String> {
}
