package com.example.william.my.module.open.activity;

import android.net.http.HttpResponseCache;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.opensource.svgaplayer.utils.log.SVGALogger;

import org.jetbrains.annotations.NotNull;

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
            parser.decodeFromURL(new URL("https://github.com/yyued/SVGA-Samples/blob/master/posche.svga?raw=true"), new SVGAParser.ParseCompletion() {
                @Override
                public void onError() {

                }

                @Override
                public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
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