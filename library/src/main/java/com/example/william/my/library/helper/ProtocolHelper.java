package com.example.william.my.library.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.william.my.library.ProtocolConstants;
import com.example.william.my.library.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 私有协议Schema跳转帮助类
 */
public class ProtocolHelper {

    private static final String TAG = ProtocolHelper.class.getSimpleName();

    /**
     * 处理协议跳转
     *
     * @param activity 上下文
     * @param url      跳转地址
     */
    public static void handleProtocolEvent(Activity activity, String url) {
        handleProtocolEvent(activity, url, null);
    }

    /**
     * 处理协议跳转
     *
     * @param activity 上下文
     * @param url      跳转地址
     * @param extraMap 额外需要带的参数
     */
    public static void handleProtocolEvent(Activity activity, String url, Map<String, Object> extraMap) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return;
        }
        if (!TextUtils.isEmpty(url) && url.startsWith(ProtocolConstants.HTTP_SCHEME_HEADER)) {
            // Http开头的网页
            Intent intent = new Intent(activity, BaseActivity.class);
            activity.startActivity(intent);
        } else if (!TextUtils.isEmpty(url) && url.startsWith(ProtocolConstants.APP_SCHEME_HEADER)) {
            // App内部跳转
            String path = ProtocolHelper.getProtocolAction(url);
            Map<String, Object> paramsMap = getProtocolParams(url);
            Intent intent = getPageIntent(activity, path);
            if (intent != null) {
                if (paramsMap != null && !paramsMap.isEmpty()) {
                    for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                        String key = entry.getKey();
                        String value = (String) entry.getValue();
                        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                            intent.putExtra(entry.getKey(), (String) entry.getValue());
                        }
                    }
                }
                if (extraMap != null && !extraMap.isEmpty()) {
                    for (Map.Entry<String, Object> entry : extraMap.entrySet()) {
                        String key = entry.getKey();
                        String value = (String) entry.getValue();
                        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                            intent.putExtra(entry.getKey(), (String) entry.getValue());
                        }
                    }
                }
                activity.startActivity(intent);
            }
        }
    }

    /**
     * 私有协议分析,获取协议事件.
     */
    public static String getProtocolAction(String linkUrl) {
        String result = null;
        try {
            if (linkUrl == null) {
                return null;
            }
            int firstIndex = linkUrl.indexOf("//");
            int lastIndex = linkUrl.indexOf("?");
            if (lastIndex == -1) {
                lastIndex = linkUrl.length();
            }
            if (firstIndex == -1) {
                firstIndex = 0;
            }
            result = linkUrl.substring(firstIndex + 2, lastIndex);
        } catch (Exception e) {
            Log.e(TAG, "Get protocol action error is [" + e.getMessage() + "]");
        }
        return result;
    }

    /**
     * 解析私有协议的参数,其中page字段是需要打开的页面.
     */
    public static Map<String, Object> getProtocolParams(String urlString) {
        Map<String, Object> paramsMap = new HashMap<>();
        try {
            if (urlString == null || urlString.length() == 0) {
                return paramsMap;
            }
            int questIndex = urlString.indexOf('?');
            if (questIndex == -1) {
                return paramsMap;
            }
            String queryString = urlString.substring(questIndex + 1);
            if (queryString.length() > 0) {
                int ampersandIndex;
                int lastAmpersandIndex = 0;
                String subStr;
                String param;
                String value;
                String[] paramPair;
                do {
                    ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
                    if (ampersandIndex > 0) {
                        subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
                        lastAmpersandIndex = ampersandIndex;
                    } else {
                        subStr = queryString.substring(lastAmpersandIndex);
                    }
                    paramPair = subStr.split("=");
                    param = paramPair[0];
                    value = paramPair.length == 1 ? "" : paramPair[1];
                    paramsMap.put(param, value);
                } while (ampersandIndex > 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Get protocol params error is [" + e.getMessage() + "]");
        }
        return paramsMap;
    }

    public static Intent getPageIntent(Activity activity, String page) {
        Intent intent = null;
        if (TextUtils.isEmpty(page)) {
            return null;
        }
        switch (page) {
            case ProtocolConstants.SCHEME_PAGE_MAIN_PAGE:
                intent = new Intent(activity, BaseActivity.class);
                break;
            default:
                break;
        }
        if (activity != null && !TextUtils.isEmpty(activity.getClass().getSimpleName()) && intent != null) {
            intent.putExtra(ProtocolConstants.SCHEME_FROM, activity.getClass().getSimpleName());
        }
        return intent;
    }
}