package com.example.william.my.module.flutter.app;

import android.app.Application;

import com.example.william.my.library.interfaces.IComponentApplication;

public class FlutterApp implements IComponentApplication {

    @Override
    public void init(Application application) {
        initFlutter();
    }

    @Override
    public void initAsync(Application application) {

    }

    private void initFlutter() {
        //Step 3: (Optional) Use a cached FlutterEngine
//        // Instantiate a FlutterEngine.
//        FlutterEngine flutterEngine = new FlutterEngine(application);
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
    }
}
