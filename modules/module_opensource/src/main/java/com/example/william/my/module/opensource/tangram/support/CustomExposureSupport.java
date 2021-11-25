package com.example.william.my.module.opensource.tangram.support;

import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.support.ExposureSupport;

@Keep
public class CustomExposureSupport extends ExposureSupport {

    private static final String TAG = "CustomExposureSupport";

    public CustomExposureSupport() {
        setOptimizedMode(true);
    }

    @Override
    public void onExposure(@NonNull Card card, int offset, int position) {
        Log.e(TAG, "onExposure: card=" + card.getClass().getSimpleName() + ", offset=" + offset + ", position=" + position);
    }
}
