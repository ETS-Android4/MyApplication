package com.example.william.my.module.flutter.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.flutter.R;
import com.example.william.my.module.router.ARouterPath;

import io.flutter.embedding.android.FlutterActivity;

/**
 * https://flutter.dev/docs/development/add-to-app/android/add-flutter-screen
 * https://flutter.dev/docs/development/add-to-app/android/add-flutter-fragment
 */
@Route(path = ARouterPath.Flutter.Flutter)
public class FlutterMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_flutter);

        //Step 2: 启动FlutterActivity
        startActivity(
                FlutterActivity.createDefaultIntent(this)
        );

//        startActivity(
//                FlutterActivity
//                        .withNewEngine()
//                        .initialRoute("/main")
//                        .build(this)
//        );

        //Step 3: 使用 FlutterEngine 缓存
//        startActivity(
//                FlutterActivity
//                        .withCachedEngine("main")
//                        .build(this)
//        );
        finish();
    }
}