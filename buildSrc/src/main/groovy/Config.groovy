class Config {

    static gradleVersion = '4.1.2'//'3.6.0'
    //static gradleVersion = '7.0.0-alpha07'
    //https://developer.android.google.cn/studio/releases/gradle-plugin

    static depConfig = [
            //plugin_gradle  : new DepConfig(pluginPath: "com.android.tools.build:gradle:$gradleVersion"),

            Module       : new DepConfig(true, ":module"),

            AndroidUtil  : new DepConfig(true, ":modules:module_android_util"),

            Network      : new DepConfig(true, ":modules:module_network"),
            CustomView   : new DepConfig(true, ":modules:module_custom_view"),

            Jetpack      : new DepConfig(true, ":modules:module_jetpack"),
            OpenSource   : new DepConfig(true, ":modules:module_open_source"),

            Widget       : new DepConfig(true, ":modules:module_widget"),
            Demo         : new DepConfig(true, ":modules:module_demo"),

            Kotlin       : new DepConfig(true, ":modules:module_kotlin"),
            Sample       : new DepConfig(true, ":modules:module_sample"),

            ModuleFlutter: new DepConfig(false, ":modules:module_flutter"),

            ModuleNeRtc  : new DepConfig(true, ":modules:module_ne_rtc"),
            ModuleNeAudio: new DepConfig(false, ":modules:module_ne_audio"),
    ]
}