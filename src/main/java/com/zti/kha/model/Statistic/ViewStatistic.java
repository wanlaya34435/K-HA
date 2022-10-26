package com.zti.kha.model.Statistic;


import com.zti.kha.model.Base.BaseSimpleModel;

import java.util.Date;

/**
 * Created by Windows 8.1 on 25/8/2561.
 */
public class ViewStatistic extends BaseSimpleModel {
    public String contentId;
    public String statisticType;
    public String time;
    public int os;
    public String userId;
    public Date createDate = new Date();

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(String statisticType) {
        this.statisticType = statisticType;
    }

    public int getOs() {
        return os;
    }

    public void setOs(int os) {
        this.os = os;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
