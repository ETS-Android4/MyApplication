class Config {
    //static gradleVersion = '7.0.3'
    //https://developer.android.google.cn/studio/releases/gradle-plugin

    static depConfig = [
            //plugin_gradle  : new DepConfig(pluginPath: "com.android.tools.build:gradle:$gradleVersion"),

            Module    : new DepConfig(true, ":module"),
            ModuleBean: new DepConfig(true, ":module_bean"),

            Util      : new DepConfig(true, ":modules:module_util"),
            Opensource: new DepConfig(true, ":modules:module_opensource"),

            Lib       : new DepConfig(true, ":modules:module_lib"),
            Demo      : new DepConfig(true, ":modules:module_demo"),

            Network   : new DepConfig(true, ":modules:module_network"),
//            Jetpack   : new DepConfig(true, ":modules:module_jetpack"),
//            Kotlin    : new DepConfig(true, ":modules:module_kotlin"),
//            Sample    : new DepConfig(true, ":modules:module_sample"),

            //AudioRoom : new DepConfig(true, ":modules:module_audio_room"),

            //ModuleFlutter    : new DepConfig(false, ":modules:module_flutter"),
    ]
}