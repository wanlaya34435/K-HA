package com.zti.kha.model;


import com.zti.kha.model.Base.BaseModel;
import com.zti.kha.model.User.GroupDisplay;
import org.springframework.data.annotation.Transient;

public class Contact extends BaseModel {
    private String imageBanner;
    private String name1;
    private String name2;
    private String facebook;
    private String imageLogo;
    private String description;
    private String address;
    private String phoneNumber1;
    private String phoneNumber2;
    private String email;
    private String website;
    private String latitude;
    private String longitude;
    public String groupId;
    public String youtube;
    public String fax;
    public String line;
    @Transient
    public GroupDisplay groupProfile ;
    public String getImageBanner() {
        return imageBanner;
    }

    public GroupDisplay getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(GroupDisplay groupProfile) {
        this.groupProfile = groupProfile;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(String imageLogo) {
        this.imageLogo = imageLogo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageBanner(String imageBanner) {
        this.imageBanner = imageBanner;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
