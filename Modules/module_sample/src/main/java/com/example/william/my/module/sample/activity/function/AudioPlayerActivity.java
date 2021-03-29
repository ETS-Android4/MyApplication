package com.example.william.my.module.sample.activity.function;

import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.utils.AudioPlayer;

@Route(path = ARouterPath.Sample.Sample_AudioPlayer)
public class AudioPlayerActivity extends BaseResponseActivity {

    private boolean isPlay;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (!isPlay) {
            AudioPlayer.getInstance()
                    .startPlay("https://video.fanqievv.com/user_sound/2021/01/10/1610291672209.mp3", new AudioPlayer.Callback() {
                        @Override
                        public void onStart() {
                            isPlay = true;
                            Log.e(TAG, "onStart");
                        }

                        @Override
                        public void onCompletion(Boolean success) {
                            Log.e(TAG, "onCompletion : " + success);
                        }
                    });
        } else {
            AudioPlayer.getInstance().stopPlay();
        }
    }
}