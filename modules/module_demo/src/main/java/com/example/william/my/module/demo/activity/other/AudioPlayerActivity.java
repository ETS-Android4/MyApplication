package com.example.william.my.module.demo.activity.other;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.library.utils.AudioPlayer;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Demo.Demo_AudioPlayer)
public class AudioPlayerActivity extends BaseResponseActivity {

    private boolean isPlay;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (!isPlay) {
            AudioPlayer.getInstance()
                    .startPlay(Urls.Url_Audio, new AudioPlayer.Callback() {
                        @Override
                        public void onStart() {
                            isPlay = true;
                        }

                        @Override
                        public void onCompletion(Boolean success) {

                        }
                    });
        } else {
            AudioPlayer.getInstance().stopPlay();
        }
    }
}