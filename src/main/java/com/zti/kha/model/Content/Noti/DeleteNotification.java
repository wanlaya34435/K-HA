package com.zti.kha.model.Content.Noti;


import com.zti.kha.model.Base.BaseSimpleModel;

import java.util.Date;

/**
 * Created by Windows 8.1 on 25/8/2561.
 */
public class DeleteNotification extends BaseSimpleModel {
    public String contentId;
    public String userId;
    public Date createDate = new Date();

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
