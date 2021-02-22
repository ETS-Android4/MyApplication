class Config {

    static applicationId = 'com.example.william.my.application'
    static appName = 'MyApplication'

    static minSdkVersion = 19
    static targetSdkVersion = 30

    static compileSdkVersion = 30
    static buildToolsVersion = '30.0.3'

    static versionCode = 1_000_000
    static versionName = '1.0.0'

    static gradleVersion = '4.1.2'
    //static gradleVersion = '7.0.0-alpha03'

    static kotlinVersion = '1.4.21'
    //https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib

    static routerVersion = '1.0.2'
    //https://mvnrepository.com/artifact/com.alibaba/arouter-register

    static greendaoVersion = '3.3.0'
    //https://search.maven.org/search?q=g:org.greenrobot%20AND%20a:greendao-gradle-plugin

    static busVersion = '2.6'
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
            plugin_protobuf: new DepConfig(pluginPath: "com.google.protobuf:protobuf-gradle-plugin:$protobufVersion"),
            plugin_dokit   : new DepConfig(pluginPath: "com.didichuxing.doraemonkit:dokitx-plugin:$dokitVersion"),

            Module         : new DepConfig(true, ":Module"),

            AndroidUtil    : new DepConfig(true, ":Modules:AndroidUtil"),

            NetWork        : new DepConfig(true, ":Modules:NetWork"),
            CustomView     : new DepConfig(true, ":Modules:CustomView"),

            JetPack        : new DepConfig(true, ":Modules:JetPack"),
            OpenSource     : new DepConfig(true, ":Modules:OpenSource"),
            Sample         : new DepConfig(true, ":Modules:Sample"),

            ModuleWx       : new DepConfig(false, ":Modules:ModuleWx"),
            ModuleBugly    : new DepConfig(false, ":Modules:ModuleBugly"),
            ModuleJiguang  : new DepConfig(false, ":Modules:ModuleJiguang"),
            ModuleTencentIM: new DepConfig(false, ":Modules:ModuleTencentIM"),

            Kotlin         : new DepConfig(true, ":Modules:Kotlin"),
            //ModuleTV      : new DepConfig(false, ":Modules:ModuleTV"),
    ]
}