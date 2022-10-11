package com.zti.kha.controller;

import com.zti.kha.model.Statistic.ViewStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ViewStatisticRepository extends MongoRepository<ViewStatistic, String> {

List<ViewStatistic> findByContentIdAndStatisticType(String id, String type);

    List<ViewStatistic> findByContentIdAndStatisticTypeAndUserId(String id, String type, String userId);

    List<ViewStatistic> findByStatisticTypeAndUserId(String type, String userId);


}

