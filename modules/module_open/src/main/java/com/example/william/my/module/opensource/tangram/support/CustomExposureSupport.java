package com.example.william.my.module.opensource.tangram.support;

import androidx.annotation.Keep;

import com.example.william.my.module.utils.L;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.support.ExposureSupport;

@Keep
public class CustomExposureSupport extends ExposureSupport {

    private static final String TAG = "CustomExposureSupport";

    public CustomExposureSupport() {
        setOptimizedMode(true);
    }

    @Override
    public void onExposure(Card card, int offset, int position) {
        L.e(TAG, "onExposure: card=" + card.getClass().getSimpleName() + ", offset=" + offset + ", position=" + position);
    }
}
