package com.example.william.my.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleBean {

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
        private List<ArticleDetailBean> datas;

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

        public List<ArticleDetailBean> getDatas() {
            return datas;
        }

        public void setDatas(List<ArticleDetailBean> datas) {
            this.datas = datas;
        }
    }
}
