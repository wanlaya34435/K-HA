package com.zti.kha.model.Content;
import com.zti.kha.model.Base.BaseModel;
import com.zti.kha.model.User.GroupDisplay;
import com.zti.kha.model.User.ProfileDisplay;
import org.springframework.data.annotation.Transient;

public class WebLink extends BaseModel {
    private String image="";
    public int sequence = 0;
    private String url="";
    private String author="";
    public String groupId;
    @Transient
    public GroupDisplay groupProfile ;
    @Transient
    public ProfileDisplay authorProfile ;
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public GroupDisplay getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(GroupDisplay groupProfile) {
        this.groupProfile = groupProfile;
    }

    public ProfileDisplay getAuthorProfile() {
        return authorProfile;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setAuthorProfile(ProfileDisplay authorProfile) {
        this.authorProfile = authorProfile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
