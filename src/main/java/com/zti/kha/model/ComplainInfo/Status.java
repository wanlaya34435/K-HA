package com.zti.kha.model.ComplainInfo;

import com.zti.kha.model.User.ProfileDisplay;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

public class Status {
    private  int statusCode;
    private Date updateDate = new Date();
    private  String editBy;
    private  String remark;
    private String filesPath ;
    private List<String> picturesPath;

    @Transient
    private ProfileDisplay editByProfile;



    public Status(int statusCode, String editBy, String remark, String filesPath, List<String> picturesPath) {
        this.statusCode = statusCode;
        this.editBy = editBy;
        this.remark = remark;
        this.filesPath = filesPath;
        this.picturesPath = picturesPath;
    }

    public String getEditBy() {
        return editBy;
    }

    public String getRemark() {
        return remark;
    }

    public String getFilesPath() {
        return filesPath;
    }

    public void setFilesPath(String filesPath) {
        this.filesPath = filesPath;
    }

    public List<String> getPicturesPath() {
        return picturesPath;
    }

    public void setPicturesPath(List<String> picturesPath) {
        this.picturesPath = picturesPath;
    }

    public ProfileDisplay getEditByProfile() {
        return editByProfile;
    }

    public void setEditByProfile(ProfileDisplay editByProfile) {
        this.editByProfile = editByProfile;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
