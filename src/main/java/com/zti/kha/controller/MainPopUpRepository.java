package com.zti.kha.controller;

import com.zti.kha.model.MainPopUp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MainPopUpRepository extends MongoRepository<MainPopUp, String> {
    Optional<MainPopUp> findById(String id);

    List<MainPopUp> findByEnable(boolean enable);
}

