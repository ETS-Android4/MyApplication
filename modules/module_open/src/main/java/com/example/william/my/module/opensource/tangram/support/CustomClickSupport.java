package com.example.william.my.module.opensource.tangram.support;

import android.view.View;

import androidx.annotation.Keep;

import com.example.william.my.module.utils.T;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

@Keep
public class CustomClickSupport extends SimpleClickSupport {

    public CustomClickSupport() {
        setOptimizedMode(true);
    }

    @Override
    public void defaultClick(View targetView, BaseCell cell, int eventType) {
        super.defaultClick(targetView, cell, eventType);
        T.show("您点击了组件，type=" + cell.stringType + ", pos=" + cell.pos);
    }
}
