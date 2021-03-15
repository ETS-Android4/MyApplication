class Config {

    static applicationId = 'com.example.william.my.application'
    static appName = 'MyApplication'

    static minSdkVersion = 19
    static targetSdkVersion = 30

    static compileSdkVersion = 30
    static buildToolsVersion = '30.0.3'

    static versionCode = 1_000_000
    static versionName = '1.0.0'

    static gradleVersion = '4.1.2'//'3.6.0'
    //static gradleVersion = '7.0.0-alpha07'
    //https://developer.android.google.cn/studio/releases/gradle-plugin

    static depConfig = [
            plugin_gradle  : new DepConfig(pluginPath: "com.android.tools.build:gradle:$gradleVersion"),

            Module         : new DepConfig(true, ":module"),

            AndroidUtil    : new DepConfig(true, ":modules:module_android_util"),

            Network        : new DepConfig(true, ":modules:module_network"),
            CustomView     : new DepConfig(true, ":modules:module_custom_view"),

            Jetpack        : new DepConfig(true, ":modules:module_jetpack"),
            OpenSource     : new DepConfig(true, ":modules:module_open_source"),
            Sample         : new DepConfig(true, ":modules:module_sample"),

            ModuleWeChat   : new DepConfig(false, ":modules:module_wechat"),
            ModuleBugly    : new DepConfig(false, ":modules:module_bugly"),
            ModuleSophix   : new DepConfig(false, ":modules:module_sophix"),
            ModuleJiguang  : new DepConfig(false, ":modules:module_jiguang"),
            ModuleTencentIM: new DepConfig(false, ":modules:module_tencent_im"),

            Kotlin         : new DepConfig(true, ":modules:module_kotlin"),
            ModuleFlutter  : new DepConfig(true, ":modules:module_flutter"),
    ]
}