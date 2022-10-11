package com.zti.kha.model.Content;
import com.zti.kha.model.Base.BaseNoti;
import org.springframework.data.annotation.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Windows 8.1 on 2/8/2561.
 */
public class News extends BaseNoti {

    private Date startDate;
    private Date endDate;
    private String thumbnailsPath ;
    private String title ;
    private String description ;
    private String youtube ;

    private List<String> filesPath = new ArrayList<>();
    @Transient
    private List<String> categoryProfile=  new ArrayList<>();
    private List<String> category = new ArrayList<>();
    private List<String> picturesPath = new ArrayList<>();
    private String buttonName ;
    private String buttonUrl ;
    private String facebookLive ;

    public String getButtonName() {
        return buttonName;
    }
    public List<String> getCategoryProfile() {
        return categoryProfile;
    }
    public void setCategoryProfile(List<String> categoryProfile) {
        this.categoryProfile = categoryProfile;
    }
    public List<String> getCategory() {
        return category;
    }
    public void setCategory(List<String> category) {
        this.category = category;
    }
    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }
    public String getButtonUrl() {
        return buttonUrl;
    }
    public Date getStartDate() {
        return startDate;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public List<String> getFilesPath() {
        return filesPath;
    }

    public void setFilesPath(List<String> filesPath) {
        this.filesPath = filesPath;
    }

    public String getFacebookLive() {
        return facebookLive;
    }

    public void setFacebookLive(String facebookLive) {
        this.facebookLive = facebookLive;
    }
    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getThumbnailsPath() {
        return thumbnailsPath;
    }

    public void setThumbnailsPath(String thumbnailsPath) {
        this.thumbnailsPath = thumbnailsPath;
    }

    public String getTitle() {
        return title;
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
