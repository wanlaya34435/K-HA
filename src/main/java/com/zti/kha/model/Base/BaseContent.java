package com.zti.kha.model.Base;

import com.zti.kha.model.User.Group;
import com.zti.kha.model.User.GroupDisplay;
import com.zti.kha.model.User.ProfileDisplay;
import org.springframework.data.annotation.Transient;

/**
 * Created by Windows 8.1 on 2/8/2561.
 */
public class BaseContent extends BaseModel {
    public String author="";
    public int sequence = 0;
    public int cntView = 0;

    public String groupId ="";

    public int pin = 0;
    @Transient
    public ProfileDisplay authorProfile ;
    @Transient
    public GroupDisplay groupProfile ;
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GroupDisplay getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(GroupDisplay groupProfile) {
        this.groupProfile = groupProfile;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getCntView() {
        return cntView;
    }


    public void setCntView(int cntView) {
        this.cntView = cntView;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public ProfileDisplay getAuthorProfile() {
        return authorProfile;
    }

    public void setAuthorProfile(ProfileDisplay authorProfile) {
        this.authorProfile = authorProfile;
    }



}
