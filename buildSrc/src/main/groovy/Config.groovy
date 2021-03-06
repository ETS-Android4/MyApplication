class Config {

    static applicationId = 'com.example.william.my.application'
    static appName = 'MyApplication'

    static minSdkVersion = 19
    static targetSdkVersion = 30

    static compileSdkVersion = 30
    static buildToolsVersion = '30.0.3'

    static versionCode = 1_000_000
    static versionName = '1.0.0'

    static gradleVersion = '3.6.0'//'4.1.2'
    //static gradleVersion = '7.0.0-alpha07'
    //https://developer.android.google.cn/studio/releases/gradle-plugin

    static kotlinVersion = '1.4.31'
    //https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib

    static routerVersion = '1.0.2'
    //https://mvnrepository.com/artifact/com.alibaba/arouter-register

    static greendaoVersion = '3.3.0'
    //https://search.maven.org/search?q=g:org.greenrobot%20AND%20a:greendao-gradle-plugin

    static busVersion = '2.6'
    //https://mvnrepository.com/artifact/com.blankj/bus-gradle-plugin

    static tinkerVersion = '1.2.2'
    //https://mvnrepository.com/artifact/com.blankj/bus-gradle-plugin

    static protobufVersion = '0.8.14'
    //https://mvnrepository.com/artifact/com.google.protobuf/protobuf-gradle-plugin

    static dokitVersion = '3.3.5'
    //https://mvnrepository.com/artifact/com.didichuxing.doraemonkit/dokitx-plugin

    static depConfig = [
            plugin_gradle  : new DepConfig(pluginPath: "com.android.tools.build:gradle:$gradleVersion"),
            plugin_kotlin  : new DepConfig(pluginPath: "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"),
            plugin_arouter : new DepConfig(pluginPath: "com.alibaba:arouter-register:$routerVersion"),
            plugin_greendao: new DepConfig(pluginPath: "org.greenrobot:greendao-gradle-plugin:$greendaoVersion"),
            plugin_bus     : new DepConfig(pluginPath: "com.blankj:bus-gradle-plugin:$busVersion"),
            plugin_tinker  : new DepConfig(pluginPath: "com.tencent.bugly:tinker-support:$tinkerVersion"),
            plugin_protobuf: new DepConfig(pluginPath: "com.google.protobuf:protobuf-gradle-plugin:$protobufVersion"),
            plugin_dokit   : new DepConfig(pluginPath: "com.didichuxing.doraemonkit:dokitx-plugin:$dokitVersion"),

            Module         : new DepConfig(true, ":module"),

            AndroidUtil    : new DepConfig(true, ":modules:module_android_util"),

            Network        : new DepConfig(true, ":modules:module_network"),
            CustomView     : new DepConfig(true, ":modules:module_custom_view"),

            Jetpack        : new DepConfig(true, ":modules:module_jetpack"),
            OpenSource     : new DepConfig(true, ":modules:module_open_source"),
            Sample         : new DepConfig(true, ":modules:module_sample"),

            ModuleWx       : new DepConfig(false, ":modules:module_wx"),
            ModuleBugly    : new DepConfig(false, ":modules:module_bugly"),
            ModuleJiguang  : new DepConfig(false, ":modules:module_jiguang"),
            ModuleTencentIM: new DepConfig(false, ":modules:module_tencent_im"),

            Kotlin         : new DepConfig(true, ":modules:module_kotlin"),
            //ModuleTV      : new DepConfig(false, ":Modules:ModuleTV"),
    ]
}