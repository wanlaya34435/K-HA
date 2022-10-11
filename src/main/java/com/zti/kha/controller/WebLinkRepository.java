package com.zti.kha.controller;

import com.zti.kha.model.Content.WebLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WebLinkRepository extends MongoRepository<WebLink, String> {
   Page<WebLink> findById(String id, Pageable pageable);

    List<WebLink> findByEnable(boolean enable);
}

