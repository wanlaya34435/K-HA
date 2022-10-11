package com.zti.kha.controller;

import com.zti.kha.model.Address.Province;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends MongoRepository<Province, String> {

}
