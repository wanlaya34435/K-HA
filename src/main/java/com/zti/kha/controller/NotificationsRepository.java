package com.zti.kha.controller;

import com.zti.kha.model.Content.Noti.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationsRepository extends MongoRepository<Notifications, String> {
    Optional<Notifications> findById(String id);
    Notifications findAllByOrderBySequenceDesc();
    List<Notifications> findByEnable(boolean enable);
    List<Notifications> findByIdInAndEnableOrderByCreateDateDesc(List<String> id, boolean enable);

    Page<Notifications> findById(String id, Pageable pageable);
}

