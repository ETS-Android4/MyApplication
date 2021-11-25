package com.example.william.my.module.opensource.activity;

import android.net.http.HttpResponseCache;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.opensource.svgaplayer.utils.log.SVGALogger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * https://github.com/svga/SVGAPlayer-Android
 */
@Route(path = ARouterPath.OpenSource.OpenSource_SvagPlayer)
public class SVGAPlayerActivity extends AppCompatActivity {

    private SVGAImageView mSVGAImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_svga);
        mSVGAImageView = findViewById(R.id.svga);

        SVGALogger.INSTANCE.setLogEnabled(true);
        SVGAParser.Companion.shareParser().init(this);

        File cacheDir = new File(getApplicationContext().getCacheDir(), "svga");
        try {
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SVGAParser parser = SVGAParser.Companion.shareParser();

        try {
            parser.decodeFromURL(new URL("http://upload.fanqievv.com/resource/mobile/image/avatarframe/lmxingkong.svga"), new SVGAParser.ParseCompletion() {
                @Override
                public void onError() {

                }

                @Override
                public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    mSVGAImageView.setImageDrawable(drawable);
                    mSVGAImageView.startAnimation();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}