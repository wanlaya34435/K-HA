package com.zti.kha.controller;

import com.zti.kha.model.Content.Noti.DeleteNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeleteNotificationRepository extends MongoRepository<DeleteNotification, String> {


    List<DeleteNotification> findByContentIdAndUserId(String id, String userId);
    List<DeleteNotification> findByUserId(String userId);



}

