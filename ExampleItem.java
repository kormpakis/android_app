package com.example.user.recyclerviewjsonvolley;

public class ExampleItem {
    private String mImageUrl;
    private String mTitle;
    private String mExcerpt;
    private String mUrl;
    private int mId;

    //public ExampleItem(String imageUrl, String title, String excerpt, String url) {
    public ExampleItem(String imageUrl, String title, String excerpt, String url, int id) {
        mImageUrl = imageUrl;
        mTitle = title;
        mExcerpt = excerpt;
        mUrl = url;
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getExcerpt () {
        return mExcerpt;
    }

    public String getUrl() {
        return mUrl;
    }
}