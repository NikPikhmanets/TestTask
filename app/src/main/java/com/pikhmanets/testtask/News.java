package com.pikhmanets.testtask;

class News {
    private String mTitle;
    private String mPostTime;
    private String mTextNews;
    private String mRefNews;

    News(String title, String postTime, String textNews, String ref) {
        mTitle = title;
        mPostTime = postTime;
        mTextNews = textNews;
        mRefNews = ref;
    }

    String getTitle() {
        return mTitle;
    }

    void setTitle(String title) {
        this.mTitle = title;
    }

    String getPostTime() {
        return mPostTime;
    }

    void setPostTime(String postTime) {
        this.mPostTime = postTime;
    }

    String getTextNews() {
        return mTextNews;
    }

    void setTextNews(String textNews) {
        this.mTextNews = textNews;
    }

    public String getRefNews() {
        return mRefNews;
    }

    public void setRefNews(String refNews) {
        mRefNews = refNews;
    }
}
