package com.zti.kha.model;

import com.zti.kha.model.Base.BaseModel;

/**
 * Created by up on 3/13/17.
 */
public class MainPopUp extends BaseModel {
    private String image="";
    private String description="";
    private String url="";
    private String author="";
    private String title="";


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
