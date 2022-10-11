package com.zti.kha.controller;

import com.zti.kha.model.Address.District;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends MongoRepository<District, String> {
}
