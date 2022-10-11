package com.zti.kha.controller;

import com.zti.kha.model.Address.SubDistrict;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDistrictRepository extends MongoRepository<SubDistrict, String> {
}
