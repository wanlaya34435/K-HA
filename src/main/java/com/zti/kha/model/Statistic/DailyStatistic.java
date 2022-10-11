package com.zti.kha.model.Statistic;


import com.zti.kha.model.Base.BaseSimpleModel;

/**
 * Created by Windows 8.1 on 25/8/2561.
 */
public class DailyStatistic extends BaseSimpleModel {

    public String time;
    public int os;
    public String userId;
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
