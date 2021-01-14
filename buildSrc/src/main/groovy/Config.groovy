class Config {

    static applicationId = 'com.example.william.my.application'
    static appName = 'MyApplication'

    static minSdkVersion = 19
    static targetSdkVersion = 30

    static compileSdkVersion = 30
    static buildToolsVersion = '30.0.3'

    static versionCode = 1_000_000
    static versionName = '1.0.0'

    static gradleVersion = '4.1.1'
    //static gradleVersion = '7.0.0-alpha03'

    static kotlinVersion = '1.4.30-M1'
    //https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib

    static routerVersion = '1.0.2'
    //https://mvnrepository.com/artifact/com.alibaba/arouter-register

    static busVersion = '2.6'
    //https://mvnrepository.com/artifact/com.blankj/bus-gradle-plugin

    static depConfig = [
            plugin_gradle  : new DepConfig(pluginPath: "com.android.tools.build:gradle:$gradleVersion"),
            plugin_kotlin  : new DepConfig(pluginPath: "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"),
            plugin_arouter : new DepConfig(pluginPath: "com.alibaba:arouter-register:$routerVersion"),
            plugin_bus     : new DepConfig(pluginPath: "com.blankj:bus-gradle-plugin:$busVersion"),

            Module         : new DepConfig(true, ":Module"),

            AndroidUtil    : new DepConfig(true, ":Modules:AndroidUtil"),

            NetWork        : new DepConfig(true, ":Modules:NetWork"),
            CustomView     : new DepConfig(true, ":Modules:CustomView"),

            JetPack        : new DepConfig(true, ":Modules:JetPack"),
            OpenSource     : new DepConfig(true, ":Modules:OpenSource"),
            Sample         : new DepConfig(true, ":Modules:Sample"),

            ModuleWx       : new DepConfig(false, ":Modules:ModuleWx"),
            ModuleJiguang  : new DepConfig(false, ":Modules:ModuleJiguang"),
            ModuleTencentIM: new DepConfig(false, ":Modules:ModuleTencentIM"),

            Kotlin         : new DepConfig(true, ":Modules:Kotlin"),
            //ModuleTV      : new DepConfig(false, ":Modules:ModuleTV"),
    ]
}