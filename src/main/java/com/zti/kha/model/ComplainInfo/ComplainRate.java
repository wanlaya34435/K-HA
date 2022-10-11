package com.zti.kha.model.ComplainInfo;

public class ComplainRate {
    private int rateUpdate;
    private String commentUpdate="";
    private int rateDuration;
    private String commentDuration="";

    public int getRateUpdate() {
        return rateUpdate;
    }

    public void setRateUpdate(int rateUpdate) {
        this.rateUpdate = rateUpdate;
    }

    public String getCommentUpdate() {
        return commentUpdate;
    }

    public void setCommentUpdate(String commentUpdate) {
        this.commentUpdate = commentUpdate;
    }

    public int getRateDuration() {
        return rateDuration;
    }

    public void setRateDuration(int rateDuration) {
        this.rateDuration = rateDuration;
    }

    public String getCommentDuration() {
        return commentDuration;
    }

    public void setCommentDuration(String commentDuration) {
        this.commentDuration = commentDuration;
    }
}
