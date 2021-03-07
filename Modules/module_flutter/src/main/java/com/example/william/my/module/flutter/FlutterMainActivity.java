package com.example.william.my.module.flutter;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
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
//        // Instantiate a FlutterEngine.
//        FlutterEngine flutterEngine = new FlutterEngine(this);
//
//        // Configure an initial route.
//        flutterEngine.getNavigationChannel().setInitialRoute("main");
//
//        // Start executing Dart code to pre-warm the FlutterEngine.
//        flutterEngine.getDartExecutor().executeDartEntrypoint(
//                DartExecutor.DartEntrypoint.createDefault()
//        );
//
//        // Cache the FlutterEngine to be used by FlutterActivity.
//        FlutterEngineCache
//                .getInstance()
//                .put("main", flutterEngine);
//
//        startActivity(
//                FlutterActivity
//                        .withCachedEngine("main")
//                        .build(this)
//        );
    }
}