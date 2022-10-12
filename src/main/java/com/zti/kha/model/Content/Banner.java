package com.zti.kha.model.Content;


import com.zti.kha.model.Base.BaseContent;

import java.util.Date;

/**
 * Created by Windows 8.1 on 2/8/2561.
 */
public class Banner extends BaseContent {

    private String imageBanner ;
    private String url ;

    private Date startDate;
    private Date endDate;
    public String getImageBanner() {
        return imageBanner;
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

    public void setImageBanner(String imageBanner) {
        this.imageBanner = imageBanner;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
