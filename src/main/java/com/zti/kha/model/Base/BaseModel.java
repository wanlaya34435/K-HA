package com.zti.kha.model.Base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * Created by S on 10/18/2016.
 */

@JsonIgnoreProperties(value = {"lang"})
public class BaseModel extends BaseSimpleModel {
    //TODO might cause language problem
    @Transient
    @JsonIgnore
    protected String lang = "en";


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "createDate")
    protected Date createDate = new Date();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "updateDate")
    protected Date updateDate = new Date();

    @JsonProperty("createDate")
    public Date getCreateDate() {
        return createDate;
    }

    @JsonProperty("createDate")
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonProperty("updateDate")
    public Date getUpdateDate() {
        return updateDate;
    }

    @JsonProperty("updateDate")
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }




    public String editBy="";
    public String getEditBy() {
        return editBy;
    }
    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }
    @JsonProperty(value = "enable", access = JsonProperty.Access.READ_ONLY)
    protected boolean enable = true;
    @JsonProperty("enable")
    public boolean isEnable() {
        return enable;
    }
    @JsonProperty("enable")
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
