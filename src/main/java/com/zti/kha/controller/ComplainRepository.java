package com.zti.kha.controller;

import com.zti.kha.model.ComplainInfo.Complain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ComplainRepository extends MongoRepository<Complain, String> {
    Optional<Complain> findByComplainId(String id);
    List<Complain> findByGroupId(String id);
    List<Complain> findByGroupIdAndCurrentStatus(String id,int status);
    List<Complain> findByGroupIdAndCreateDateBetween(String id,Date start,Date end);
    List<Complain> findByCurrentStatus(int status);
    List<Complain> findByCreateDateBetween(Date start,Date end);

    List<Complain> findByIdIn(List<String> id);
    Optional<Complain> findById(String id);
    List<Complain> findByAuthor(String id);
    Page<Complain> findById(String id, Pageable pageable);
   Complain findTopByOrderByCreateDateDesc();


}

