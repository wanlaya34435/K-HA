package com.zti.kha.controller;
import com.zti.kha.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Windows 8.1 on 10/10/2560.
 */
public interface ContactRepository extends MongoRepository<Contact, String> {

    Optional<Contact> findById(String id);
    Page<Contact> findById(String id, Pageable pageable);

    List<String> findByGroupIdIn(List<String> id);

}
