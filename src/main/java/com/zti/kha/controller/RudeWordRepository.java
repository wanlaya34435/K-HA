package com.zti.kha.controller;

import com.zti.kha.model.Common.RudeWord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface RudeWordRepository extends MongoRepository<RudeWord, String> {

ArrayList<RudeWord> findByName(String name);
    Optional<RudeWord> findById(String id);
}

