package com.pikhmanets.testtask;

class News {
    private String mTitle;
    private String mPostTime;
    private String mTextNews;

    News(String title, String postTime, String textNews) {
        mTitle = title;
        mPostTime = postTime;
        mTextNews = textNews;
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
}
