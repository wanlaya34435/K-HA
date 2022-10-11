package com.zti.kha.model.Content;

import com.zti.kha.model.Base.BaseContent;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows 8.1 on 2/8/2561.
 */
public class Knowledge extends BaseContent {

    private String thumbnailsPath;
    private String title;
    private int mediaType;
    private String url;
    private String imagePath;
    private String videoPath;
    private String filesPath ;
    @Transient
    private List<String> categoryProfile = new ArrayList<>();
    private List<String> category = new ArrayList<>();

    public String getThumbnailsPath() {
        return thumbnailsPath;
    }

    public void setThumbnailsPath(String thumbnailsPath) {
        this.thumbnailsPath = thumbnailsPath;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getFilesPath() {
        return filesPath;
    }

    public void setFilesPath(String filesPath) {
        this.filesPath = filesPath;
    }
}
