package com.zti.kha.model.Common;

import com.zti.kha.model.Base.BaseModel;
import com.zti.kha.model.User.Profile;
import org.springframework.data.annotation.Transient;

/**
 * Created by up on 3/13/17.
 */
public class Rate extends BaseModel {
    private String userId ="";
    private String contentId ="";
    private String contentType="";
    private int score =0;
    @Transient
    private float totalScore =0;

    @Transient
    private Profile authorProfile = new Profile();

    public int getScore() {
        return score;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Profile getAuthorProfile() {
        return authorProfile;
    }

    public void setAuthorProfile(Profile authorProfile) {
        this.authorProfile = authorProfile;
    }


}
