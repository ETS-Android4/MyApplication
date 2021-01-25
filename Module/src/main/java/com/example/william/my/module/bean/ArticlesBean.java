package com.example.william.my.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticlesBean {

    @SerializedName("data")
    private DataBean data;

    @SerializedName("errorCode")
    private int errorCode;
    @SerializedName("errorMsg")
    private String errorMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static class DataBean {

        @SerializedName("curPage")
        private int curPage;
        @SerializedName("size")
        private int size;

        @SerializedName("datas")
        private List<ArticleBean> datas;

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<ArticleBean> getDatas() {
            return datas;
        }

        public void setDatas(List<ArticleBean> datas) {
            this.datas = datas;
        }

        public static class ArticleBean {

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
    }
}
