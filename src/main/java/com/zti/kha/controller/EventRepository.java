package com.zti.kha.controller;

import com.zti.kha.model.Content.Event;
import com.zti.kha.model.Content.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findById(String id);
    Event findAllByOrderBySequenceDesc();
    List<Event> findByIdInAndEnableOrderByCreateDateDesc(List<String> id, boolean enable);
    List<Event> findByGroupIdIn(List<String> id);

    Page<Event> findById(String id, Pageable pageable);
}

