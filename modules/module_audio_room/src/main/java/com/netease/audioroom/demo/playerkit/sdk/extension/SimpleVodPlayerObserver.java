package com.netease.audioroom.demo.playerkit.sdk.extension;

import com.netease.audioroom.demo.playerkit.sdk.VodPlayerObserver;
import com.netease.audioroom.demo.playerkit.sdk.model.MediaInfo;

/**
 * @author netease
 */
public abstract class SimpleVodPlayerObserver implements VodPlayerObserver {
    @Override
    public void onNetStateBad() {

    }

    @Override
    public void onSeekCompleted() {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onPreparing() {

    }

    @Override
    public void onPrepared(MediaInfo mediaInfo) {

    }

    @Override
    public void onError(int code, int extra) {

    }

    @Override
    public void onFirstVideoRendered() {

    }

    @Override
    public void onFirstAudioRendered() {

    }

    @Override
    public void onBufferingStart() {

    }

    @Override
    public void onBufferingEnd() {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onVideoDecoderOpen(int value) {

    }
}
