package com.example.william.my.core.banner.listener;

public interface BannerOnBannerListener<T> {
    /**
     * 点击事件
     *
     * @param data     数据实体
     * @param position 当前位置
     */
    void OnBannerClick(T data, int position);
}
