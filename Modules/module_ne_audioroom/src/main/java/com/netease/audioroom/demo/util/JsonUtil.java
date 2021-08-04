package com.netease.audioroom.demo.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

    public static JSONObject parse(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JsonUtil", "parse exception =" + e.getMessage());
            return null;
        }
    }
}
