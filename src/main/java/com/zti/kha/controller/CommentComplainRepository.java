package com.zti.kha.controller;

import com.zti.kha.model.ComplainInfo.CommentComplain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentComplainRepository extends MongoRepository<CommentComplain, String> {

    Optional<CommentComplain> findById(String id);
    Page<CommentComplain> findByIdIn(List<String> name, Pageable pageable);
    Page<CommentComplain>  findByIdIs(String id, Pageable pageable);
    List<CommentComplain> findByComplainId(String id);

}

