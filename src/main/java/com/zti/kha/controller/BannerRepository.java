package com.zti.kha.controller;

import com.zti.kha.model.Content.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends MongoRepository<Banner, String> {
    Optional<Banner> findById(String id);

    List<Banner> findByIdInAndEnableOrderByCreateDateDesc(List<String> id, boolean enable);

    Page<Banner> findById(String id, Pageable pageable);
}

