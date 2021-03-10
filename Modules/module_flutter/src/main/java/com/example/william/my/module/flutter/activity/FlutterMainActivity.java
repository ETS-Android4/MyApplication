package com.example.william.my.module.flutter.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.flutter.R;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://flutter.dev/docs/development/add-to-app/android/add-flutter-screen
 * https://flutter.dev/docs/development/add-to-app/android/add-flutter-fragment
 */
@Route(path = ARouterPath.Flutter.Flutter)
public class FlutterMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flutter_activity_flutter);

        Log.e("TAG", "FlutterMainActivity");

        //Step 1: Add FlutterActivity to AndroidManifest.xml
//        startActivity(
//                FlutterActivity.createDefaultIntent(this)
//        );

        //Step 2: Launch FlutterActivity
//        startActivity(
//                FlutterActivity
//                        .withNewEngine()
//                        .initialRoute("/main")
//                        .build(this)
//        );

        //Step 3: (Optional) Use a cached FlutterEngine
//        startActivity(
//                FlutterActivity
//                        .withCachedEngine("main")
//                        .build(this)
//        );
    }
}