package com.example.shiper.model;

public class CommentProduct {
    private String UserName;
    private long CreateDate;
    private String Content;
    private float RateScore;

    public CommentProduct(String userName, long createDate, String content, int rateScore) {
        UserName = userName;
        CreateDate = createDate;
        Content = content;
        RateScore = rateScore;
    }

    public CommentProduct() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public long getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(long createDate) {
        CreateDate = createDate;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public float getRateScore() {
        return RateScore;
    }

    public void setRateScore(float rateScore) {
        RateScore = rateScore;
    }
}
