package com.zti.kha.controller;


import com.zti.kha.model.User.Group;
import com.zti.kha.model.User.GroupDisplay;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, String> {
    Optional<Group> findById(String id);
    Group findByName(String name);
    List<Group> findByMain(boolean fault);
    GroupDisplay findByIdIs(String id);


}

