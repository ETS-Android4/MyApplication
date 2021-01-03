package com.example.william.my.core.network.retrofit.download.state;

public enum DownloadState {

    NONE(0),           //无状态
    WAITING(1),        //等待
    LOADING(2),        //下载中
    STOP(3),          //暂停
    ERROR(4),          //错误
    FINISH(5);       //完成

    private final int value;

    public int getValue() {
        return value;
    }

    DownloadState(int value) {
        this.value = value;
    }

    public static DownloadState getActionType(int v) {
        DownloadState[] values = values();
        for (DownloadState state : values) {
            if (state.getValue() == v) {
                return state;
            }
        }
        return null;
    }

}
