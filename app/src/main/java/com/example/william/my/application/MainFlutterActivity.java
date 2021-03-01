package com.example.william.my.application;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * https://github.com/flutter/flutter/wiki/Experimental:-Add-Flutter-View
 * https://github.com/flutter/flutter/wiki/Experimental:-Add-Flutter-Fragment
 */
public class MainFlutterActivity extends AppCompatActivity {
//public class MainFlutterActivity extends FlutterActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter);

        Log.e("TAG", "ModuleActivity");

//        // Instantiate a new FlutterView.
//        FlutterView flutterView = new FlutterView(this);
//        //flutterView.attachToFlutterEngine(flutterEngine);
//
//        // Add your FlutterView wherever you'd like. In this case we add
//        // the FlutterView to a FrameLayout.
//        FrameLayout frameLayout = findViewById(R.id.frameLayout);
//        frameLayout.addView(flutterView);
//
//
//        // Instantiate your FlutterEngine.
//        FlutterEngine flutterEngine = new FlutterEngine(this);
//
//        // Pre-warm your FlutterEngine by starting Dart execution.
//        flutterEngine
//                .getDartExecutor()
//                .executeDartEntrypoint(
//                        DartExecutor.DartEntrypoint.createDefault()
//                );
//
//        FlutterEngineCache
//                .getInstance()
//                .put("main", flutterEngine);
//
//        FlutterFragment flutterFragment = FlutterFragment
//                .withCachedEngine("main")
//                .build();

//        FlutterActivity
//                .withNewEngine()
//                .initialRoute("/main")
//                .build(this);
    }
}