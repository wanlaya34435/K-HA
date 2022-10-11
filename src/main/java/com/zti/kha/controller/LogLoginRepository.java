package com.zti.kha.controller;

import com.zti.kha.model.User.LogLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LogLoginRepository extends MongoRepository<LogLogin, String> {

    List<LogLogin> findByUserId(String id);
    Page<LogLogin> findByIdIn(List<String> name, Pageable pageable);




}

