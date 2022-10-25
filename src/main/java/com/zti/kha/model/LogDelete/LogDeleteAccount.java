package com.zti.kha.model.LogDelete;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zti.kha.model.Base.BaseSimpleModel;

import java.util.Date;

public class LogDeleteAccount extends BaseSimpleModel {
    public String userId;
    public String userName;
    public String firstName;
    public String lastName;
    public String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "createDate")
    protected Date createDate = new Date();
    public String remark;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
