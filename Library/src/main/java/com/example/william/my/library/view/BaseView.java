package com.example.william.my.library.view;

public interface BaseView<T> {

    /**
     * 泛型，接收对Presenter的引用
     * 用于fragment，activity可直接new Presenter()
     */
    void setPresenter(T presenter);

    //显示loading
    void showLoading();

    //关闭loading
    void closeLoading();

    //显示提示信息
    void showToast(String message);

}
