package com.zti.kha.model.Base;

import java.util.Date;

public class BaseNoti extends  BaseContent{
    public boolean pushnotification = false;
    public boolean pushAlready= false;
    protected Date sendNotiDate = new Date();

    public boolean isPushnotification() {
        return pushnotification;
    }

    public void setPushnotification(boolean pushnotification) {
        this.pushnotification = pushnotification;
    }

    public boolean isPushAlready() {
        return pushAlready;
    }

    public void setPushAlready(boolean pushAlready) {
        this.pushAlready = pushAlready;
    }

    public Date getSendNotiDate() {
        return sendNotiDate;
    }

    public void setSendNotiDate(Date sendNotiDate) {
        this.sendNotiDate = sendNotiDate;
    }
}
