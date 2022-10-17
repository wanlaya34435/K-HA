package com.zti.kha.controller;


import com.zti.kha.model.User.Profile;
import com.zti.kha.model.User.ProfileDisplay;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by S on 9/22/2016.
 */
public interface ProfileRepository extends MongoRepository<Profile, String> {
    public Profile findByEmailIgnoreCase(String email);
    public List<Profile> findByUserNameIgnoreCaseAndEnable(String userName, boolean enable);
    public List<Profile> findByReadGroupsGroupIdIn(List<String> id);
    public ProfileDisplay findByUserName(String userName);
    public Profile findByUserNameIgnoreCase(String userName);
    public Profile findByPhoneNumber(String phone);

    Optional<ProfileDisplay> findByIdIs(String id);
    public Profile findByUserNameIs(String userName);
    List<Profile> findByIdAndEnable(String id, boolean enable);
//  public Optional<Profile> findById(String id);
    public List<Profile> findAll();

}
