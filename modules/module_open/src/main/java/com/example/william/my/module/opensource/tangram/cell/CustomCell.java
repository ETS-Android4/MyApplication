package com.example.william.my.module.opensource.tangram.cell;

import androidx.annotation.Keep;

import com.example.william.my.module.opensource.tangram.view.CustomCellView;
import com.tmall.wireless.tangram.MVHelper;
import com.tmall.wireless.tangram.structure.BaseCell;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义 model
 */
@Keep
public class CustomCell extends BaseCell<CustomCellView> {

    private String imageUrl;
    private String text;

    /**
     * 解析数据业务数据，可以将解析值缓存到成员变量里
     */
    @Override
    public void parseWith(JSONObject data, MVHelper resolver) {
        super.parseWith(data, resolver);
        try {
            if (data.has("imageUrl")) {
                imageUrl = data.getString("imageUrl");
            }
            if (data.has("text")) {
                text = data.getString("text");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据样式数据，可以将解析值缓存到成员变量里
     */
    @Override
    public void parseStyle(JSONObject data) {
        super.parseStyle(data);
    }

    /**
     * 绑定数据到自定义 View
     */
    @Override
    public void bindView(CustomCellView view) {
        super.bindView(view);
        if (pos % 2 == 0) {
            view.setBackgroundColor(0xff6200EE);
        } else {
            view.setBackgroundColor(0xff03DAC5);
        }
        view.setTextView(view.getClass().getSimpleName() + pos + ": " + text);
        view.setImageView(imageUrl);
    }

    /**
     * 绑定数据到 View 之后，可选实现
     */
    @Override
    public void postBindView(CustomCellView view) {
        super.postBindView(view);
    }

    /**
     * 校验原始数据，检查组件的合法性
     */
    @Override
    public boolean isValid() {
        return super.isValid();
    }
}
