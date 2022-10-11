package com.zti.kha.controller;

import com.zti.kha.model.Statistic.InstallStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface InstallStatisticRepository extends MongoRepository<InstallStatistic, String> {

   ArrayList<InstallStatistic>  findByDeviceId(String id);
   ArrayList<InstallStatistic>  findByOs(int id);


}

