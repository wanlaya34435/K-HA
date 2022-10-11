package com.zti.kha.controller;

import com.zti.kha.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findById(String id);
    Category findByCategoryCode(String id);
    Category findTopByOrderByCreateDateDesc();
    Category findByCategoryCodeAndCategoryType(String code, String type);
    List<Category> findByParentCategoryAndCategoryType(String parent,String type);
    Category findByCategoryNameAndCategoryType(String code, String type);





}

