package com.zti.kha.controller;

import com.zti.kha.model.Content.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends MongoRepository<Service, String> {
    Optional<Service> findById(String id);
    Optional<Service> findByName(String name);
    List<Service> findByIdInAndEnableOrderByCreateDateDesc(List<String> id, boolean enable);

    Page<Service> findById(String id, Pageable pageable);
}

