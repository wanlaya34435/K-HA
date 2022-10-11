package com.zti.kha.model.Statistic;

import java.util.Date;

public class ReturnStatisticInstall {
    private Date date;
    private int android;
    private int ios;

    public ReturnStatisticInstall(Date date, int android, int ios) {
        this.date = date;
        this.android = android;
        this.ios = ios;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAndroid() {
        return android;
    }

    public void setAndroid(int android) {
        this.android = android;
    }

    public int getIos() {
        return ios;
    }

    public void setIos(int ios) {
        this.ios = ios;
    }
}
