package com.example.william.my.core.download.state;

public enum DownloadState {

    /**
     * 无状态
     */
    NONE(0),
    /**
     * 等待
     */
    WAITING(1),
    /**
     * 下载中
     */
    LOADING(2),
    /**
     * 暂停
     */
    STOP(3),
    /**
     * 错误
     */
    ERROR(4),
    /**
     * 完成
     */
    FINISH(5);

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
