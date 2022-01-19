package com.example.william.my.module.flutter.app;

import android.app.Application;

import com.example.william.my.library.interfaces.IComponentApplication;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class FlutterApp implements IComponentApplication {

    @Override
    public void init(Application application) {
        initFlutter(application);
    }

    @Override
    public void initAsync(Application application) {

    }

    private void initFlutter(Application application) {
        //Step 3: 使用 FlutterEngine 缓存
//        // 实例化 FlutterEngine.
//        FlutterEngine flutterEngine = new FlutterEngine(application);
//
//        // 配置初始路由。
//        flutterEngine.getNavigationChannel().setInitialRoute("main");
//
//        // Start executing Dart code to pre-warm the FlutterEngine.
//        flutterEngine.getDartExecutor().executeDartEntrypoint(
//                DartExecutor.DartEntrypoint.createDefault()
//        );
//
//        // 缓存FlutterActivity启动需要的 FlutterEngine
//        FlutterEngineCache
//                .getInstance()
//                .put("main", flutterEngine);
    }
}
