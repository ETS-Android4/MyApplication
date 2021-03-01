package com.example.william.my.application;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * https://flutter.dev/docs/development/add-to-app/android/add-flutter-screen
 * https://flutter.dev/docs/development/add-to-app/android/add-flutter-fragment
 */
public class MainFlutterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter);

        Log.e("TAG", "MainFlutterActivity");

//        //Step 1: Add FlutterActivity to AndroidManifest.xml
//        startActivity(
//                FlutterActivity.createDefaultIntent(this)
//        );
//
//        //Step 2: Launch FlutterActivity
//        startActivity(
//                FlutterActivity
//                        .withNewEngine()
//                        .initialRoute("/main")
//                        .build(this)
//        );
//
//        //Step 3: (Optional) Use a cached FlutterEngine
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