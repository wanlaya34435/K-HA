package com.zti.kha.model.ComplainInfo;

import com.zti.kha.model.Base.BaseModel;
import com.zti.kha.model.User.ProfileDisplay;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by up on 3/13/17.
 */
public class CommentComplain extends BaseModel {
    private String description ="";
    private String userId ="";
    private String complainId ="";
    private List<String> picturesPath = new ArrayList<>();

    @Transient
    private ProfileDisplay authorProfile = new ProfileDisplay();

    public String getDescription() {
        return description;
    }

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public List<String> getPicturesPath() {
        return picturesPath;
    }

    public void setPicturesPath(List<String> picturesPath) {
        this.picturesPath = picturesPath;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public ProfileDisplay getAuthorProfile() {
        return authorProfile;
    }

    public void setAuthorProfile(ProfileDisplay authorProfile) {
        this.authorProfile = authorProfile;
    }

}
