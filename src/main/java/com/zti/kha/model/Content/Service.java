package com.zti.kha.model.Content;
import com.zti.kha.model.Base.BaseContent;
import org.springframework.data.annotation.Transient;
import java.util.ArrayList;
import java.util.List;

public class Service extends BaseContent {
    private String thumbnailsPath;
    private String name;
    private String address;
    private double  latitude;
    private double  longitude;
    private String description;

    @Transient
    private List<String> categoryProfile=  new ArrayList<>();
    private List<String> category = new ArrayList<>();

    private String buttonName ;
    private String buttonUrl ;
    private List<String> picturesPath = new ArrayList<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }


    public String getThumbnailsPath() {
        return thumbnailsPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setThumbnailsPath(String thumbnailsPath) {
        this.thumbnailsPath = thumbnailsPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public List<String> getPicturesPath() {
        return picturesPath;
    }

    public void setPicturesPath(List<String> picturesPath) {
        this.picturesPath = picturesPath;
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

    public void setLatitude(double  latitude) {
        this.latitude = latitude;
    }

    public double  getLongitude() {
        return longitude;
    }

    public void setLongitude(double  longitude) {
        this.longitude = longitude;
    }


}
