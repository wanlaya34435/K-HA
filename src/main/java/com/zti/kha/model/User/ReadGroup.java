package com.zti.kha.model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Transient;

import java.util.Date;

public class ReadGroup {
    public String approveBy;
    public String groupId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "createDate")
    protected Date createDate = new Date();
    @Transient
    public String groupName;
    @Transient
    public String groupIcon;
    @Transient
    public String provinceName;
    @Transient
    public String districtName;
    @Transient
    public String subDistrictName="";
    @Transient
    private boolean isDefault=false;

    public ReadGroup() {
    }

    public ReadGroup(String approveBy, String groupId) {
        this.approveBy = approveBy;
        this.groupId = groupId;
    }

    public String getSubDistrictName() {
        return subDistrictName;
    }

    public void setSubDistrictName(String subDistrictName) {
        this.subDistrictName = subDistrictName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public ReadGroup(String groupId) {
        this.groupId = groupId;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
