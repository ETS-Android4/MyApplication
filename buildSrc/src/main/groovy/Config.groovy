class Config {

    static gradleVersion = '4.1.2'//'3.6.0'
    //static gradleVersion = '7.0.0-alpha07'
    //https://developer.android.google.cn/studio/releases/gradle-plugin

    static depConfig = [
            //plugin_gradle  : new DepConfig(pluginPath: "com.android.tools.build:gradle:$gradleVersion"),

            Module    : new DepConfig(true, ":module"),
//            ModuleBean       : new DepConfig(true, ":ModuleBean"),
//
//            AndroidUtil      : new DepConfig(true, ":Modules:module_android_util"),
//
//            Network          : new DepConfig(true, ":Modules:module_network"),
//            CustomView       : new DepConfig(true, ":Modules:module_custom_view"),

//            Jetpack          : new DepConfig(true, ":Modules:module_jetpack"),
//            OpenSource: new DepConfig(true, ":modules:module_open_source"),
//            Libraries : new DepConfig(true, ":modules:module_libraries"),
//
//            Widget           : new DepConfig(true, ":Modules:module_widget"),
//            Demo             : new DepConfig(true, ":Modules:module_demo"),
//
//            Kotlin           : new DepConfig(true, ":Modules:module_kotlin"),
//            Sample           : new DepConfig(true, ":Modules:module_sample"),
//
//            ModuleFlutter    : new DepConfig(false, ":Modules:module_flutter"),
//
//            ModuleNeAudioRoom: new DepConfig(false, ":Modules:module_ne_audioroom"),
    ]
}