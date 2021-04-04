package com.example.william.my.module.demo.bean;

import com.google.gson.annotations.SerializedName;

public class ArticleDetailBean {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String desc;
    @SerializedName("link")
    private String link;

    @SerializedName("author")
    private String author;
    @SerializedName("chapterId")
    private long chapterId;
    @SerializedName("chapterName")
    private String chapterName;

    @SerializedName("shareDate")
    private long shareDate;
    @SerializedName("shareUser")
    private String shareUser;
    @SerializedName("superChapterId")
    private long superChapterId;
    @SerializedName("superChapterName")
    private String superChapterName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public long getShareDate() {
        return shareDate;
    }

    public void setShareDate(long shareDate) {
        this.shareDate = shareDate;
    }

    public String getShareUser() {
        return shareUser;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

    public long getSuperChapterId() {
        return superChapterId;
    }

    public void setSuperChapterId(long superChapterId) {
        this.superChapterId = superChapterId;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }
}
