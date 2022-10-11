package com.zti.kha.controller;

import com.zti.kha.model.Content.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends MongoRepository<News, String> {
    Optional<News> findById(String id);
    News findAllByOrderBySequenceDesc();
    List<News> findByIdInAndEnableOrderByCreateDateDesc(List<String> id, boolean enable);
    List<News> findByPin(int pin);


    Page<News> findById(String id, Pageable pageable);
}

