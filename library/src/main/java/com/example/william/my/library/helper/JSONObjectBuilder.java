package com.example.william.my.library.helper;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectBuilder {

    private final JSONObject mJSONObject;

    public static JSONObjectBuilder build() {
        return new JSONObjectBuilder();
    }

    private JSONObjectBuilder() {
        mJSONObject = new JSONObject();
    }

    public JSONObjectBuilder add(String key, Object value) {
        try {
            if (value != null) {
                mJSONObject.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(mJSONObject.toString());
    }

    public JSONObject create() {
        return mJSONObject;
    }
}