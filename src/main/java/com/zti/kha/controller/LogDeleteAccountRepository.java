package com.zti.kha.controller;

import com.zti.kha.model.LogDelete.LogDeleteAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogDeleteAccountRepository extends MongoRepository<LogDeleteAccount, String> {

    Page<LogDeleteAccount> findById(String id, Pageable pageable);


}

