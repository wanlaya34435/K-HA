package com.zti.kha.controller;

import com.zti.kha.model.Content.Knowledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface KnowledgeRepository extends MongoRepository<Knowledge, String> {
    Optional<Knowledge> findById(String id);

    Page<Knowledge> findById(String id, Pageable pageable);
}

