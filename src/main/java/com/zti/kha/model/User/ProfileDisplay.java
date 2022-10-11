package com.zti.kha.model.User;


import com.zti.kha.model.Base.BaseModel;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by S on 9/22/2016.
 */

//TODO need to add setter getter for binding request parameter
public class ProfileDisplay extends BaseModel implements Serializable {
    public String imageProfile;
    public String userName;
    public String email;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String idCard;
    public String khaId="";
    public RoleAdmin role;
    public List<ReadGroup> readGroups = new ArrayList<>();
    public List<ReadGroup> pendingGroups = new ArrayList<>();
    @Transient
    public Group khaProfile;
    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getKhaId() {
        return khaId;
    }

    public void setKhaId(String khaId) {
        this.khaId = khaId;
    }

    public Group getKhaProfile() {
        return khaProfile;
    }

    public void setKhaProfile(Group khaProfile) {
        this.khaProfile = khaProfile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public RoleAdmin getRole() {
        return role;
    }

    public void setRole(RoleAdmin role) {
        this.role = role;
    }

    public List<ReadGroup> getReadGroups() {
        return readGroups;
    }

    public void setReadGroups(List<ReadGroup> readGroups) {
        this.readGroups = readGroups;
    }

    public List<ReadGroup> getPendingGroups() {
        return pendingGroups;
    }

    public void setPendingGroups(List<ReadGroup> pendingGroups) {
        this.pendingGroups = pendingGroups;
    }
}
