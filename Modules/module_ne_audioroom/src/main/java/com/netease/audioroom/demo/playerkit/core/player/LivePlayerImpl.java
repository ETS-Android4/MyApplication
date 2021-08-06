package com.netease.audioroom.demo.playerkit.core.player;

import android.content.Context;

import com.netease.audioroom.demo.playerkit.sdk.model.VideoOptions;
import com.netease.neliveplayer.sdk.NEMediaDataSource;

/**
 * @author netease
 */
public class LivePlayerImpl extends AdvanceLivePlayer {

    public LivePlayerImpl(Context context, String videoPath, VideoOptions options) {
        super(context, videoPath, options);
    }

    public LivePlayerImpl(Context context, NEMediaDataSource mediaDataSource, VideoOptions options) {
        super(context, mediaDataSource, options);
    }
}
