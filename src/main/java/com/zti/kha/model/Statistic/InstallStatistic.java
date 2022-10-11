package com.zti.kha.model.Statistic;


import com.zti.kha.model.Base.BaseSimpleModel;

import java.util.Date;

public class InstallStatistic extends BaseSimpleModel {

    public String deviceId;
    public int os;
    public Date time = new Date();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getOs() {
        return os;
    }

    public void setOs(int os) {
        this.os = os;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
