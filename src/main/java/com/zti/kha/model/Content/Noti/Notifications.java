package com.zti.kha.model.Content.Noti;

import com.zti.kha.model.Base.BaseNoti;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows 8.1 on 2/8/2561.
 */
public class Notifications extends BaseNoti {
    private int status ;
    private String title ;
    private String description ;
    private String errorPush ;
    private String url ;
    private List<String> picturesPath = new ArrayList<>();
    @Transient
    private List<String> categoryProfile=  new ArrayList<>();
    private List<String> category = new ArrayList<>();
    @Transient
    private boolean isOpen=false;
    @Transient
    private boolean isDelete=false;
    private List<String> readGroups = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public List<String> getCategoryProfile() {
        return categoryProfile;
    }

    public void setCategoryProfile(List<String> categoryProfile) {
        this.categoryProfile = categoryProfile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getCategory() {
        return category;
    }

    public List<String> getReadGroups() {
        return readGroups;
    }

    public void setReadGroups(List<String> readGroups) {
        this.readGroups = readGroups;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getErrorPush() {
        return errorPush;
    }

    public void setErrorPush(String errorPush) {
        this.errorPush = errorPush;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<String> getPicturesPath() {
        return picturesPath;
    }

    public void setPicturesPath(List<String> picturesPath) {
        this.picturesPath = picturesPath;
    }


}
