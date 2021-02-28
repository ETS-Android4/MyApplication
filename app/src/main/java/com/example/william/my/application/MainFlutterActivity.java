package com.example.william.my.application;

import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.FrameLayout;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

/**
 * https://github.com/flutter/flutter/wiki/Experimental:-Add-Flutter-View
 * https://github.com/flutter/flutter/wiki/Experimental:-Add-Flutter-Fragment
 */
public class MainFlutterActivity extends FlutterActivity {

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

        FlutterActivity
                .withNewEngine()
                .initialRoute("/main")
                .build(this);
    }

}