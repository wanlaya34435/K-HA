package com.zti.kha.controller;

import com.zti.kha.model.Statistic.DailyStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface DailyStatisticRepository extends MongoRepository<DailyStatistic, String> {

    List<DailyStatistic> findByTimeBetween(Date departure, Date arrive);




}

