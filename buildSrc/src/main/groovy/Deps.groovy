class Deps {

    //Android
    static material = "com.google.android.material:material:1.2.1"

    static appcompat = "androidx.appcompat:appcompat:${AndroidVersions.appcompat_version}"
    static constraintlayout = "androidx.constraintlayout:constraintlayout:${AndroidVersions.constraintlayout_version}"

    static recyclerview = "androidx.recyclerview:recyclerview:${AndroidVersions.recyclerview_version}"
    static swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:${AndroidVersions.swiperefreshlayout_version}"
    static viewpager2 = "androidx.viewpager2:viewpager2:${AndroidVersions.viewpager2_version}"
    static coordinatorlayout = "androidx.coordinatorlayout:coordinatorlayout:${AndroidVersions.coordinatorlayout_version}"
    static flexbox = "com.google.android:flexbox:${AndroidVersions.flexbox_version}"

    static kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Config.kotlinVersion}"

    static core_ktx = "androidx.core:core-ktx:${AndroidVersions.core_version}"
    static livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${AndroidVersions.lifecycle_version}"
    static viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${AndroidVersions.lifecycle_version}"

    static activity_ktx = "androidx.activity:activity-ktx:${AndroidVersions.activity_version}"
    static fragment_ktx = "androidx.fragment:fragment-ktx:${AndroidVersions.fragment_version}"

    static datastore = "androidx.datastore:datastore:${AndroidVersions.datastore_version}"
    static datastore_pre = "androidx.datastore:datastore-preferences:${AndroidVersions.datastore_version}"

    static room = "androidx.room:room-runtime:${AndroidVersions.room_version}"
    static room_rxjava = "androidx.room:room-rxjava3:${AndroidVersions.room_version}"
    static room_compiler = "androidx.room:room-compiler:${AndroidVersions.room_version}"

    static paging = "androidx.paging:paging-runtime:${AndroidVersions.paging_version}"
    static paging_rxjava = "androidx.paging:paging-rxjava3:${AndroidVersions.paging_version}"

    static navigation = "androidx.navigation:navigation-fragment:${AndroidVersions.navigation_version}"
    static navigation_ui = "androidx.navigation:navigation-ui:${AndroidVersions.navigation_version}"

    static work = "androidx.work:work-runtime:${AndroidVersions.work_version}"

    static kotlin_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${AndroidVersions.coroutines_version}"

    //Base

    static util = "com.blankj:utilcodex:${DepsVersions.util_version}"

    static dokit = "com.didichuxing.doraemonkit:dokitx:${DepsVersions.dokit_version}"
    static dokit_no_op = "com.didichuxing.doraemonkit:dokitx-no-op:${DepsVersions.dokit_version}"

    static gson = "com.google.code.gson:gson:${DepsVersions.gson_version}"

    static fastjson = "com.alibaba:fastjson:${DepsVersions.fastjson_version}"

    //Frame

    static arouter = "com.alibaba:arouter-api:${DepsVersions.arouter_version}"
    static arouter_compiler = "com.alibaba:arouter-compiler:${DepsVersions.arouter_compiler_version}"

    static rxjava = "io.reactivex.rxjava3:rxjava:${DepsVersions.rxjava_version}"
    static rxandroid = "io.reactivex.rxjava3:rxandroid:${DepsVersions.rxandroid_version}"
    static rxlifecycle = "com.trello.rxlifecycle4:rxlifecycle-android-lifecycle:${DepsVersions.rxlifecycle_version}"

    static autodispose = "com.uber.autodispose2:autodispose-androidx-lifecycle:${DepsVersions.autodispose_version}"

    static eventbus = "org.greenrobot:eventbus:${DepsVersions.eventbus_version}"
    static eventbus_processor = "org.greenrobot:eventbus-annotation-processor:${DepsVersions.eventbus_version}"

    static livebus = "com.jeremyliao:live-event-bus-x:${DepsVersions.livebus_version}"

    static smartbus = "com.jeremyliao:smart-event-bus-base:${DepsVersions.smartbus_version}"
    static smartbus_compiler = "com.jeremyliao:smart-event-bus-compiler:${DepsVersions.smartbus_compiler_version}"

    static mmkv = "com.tencent:mmkv-static:${DepsVersions.mmkv_version}"

    static greendao = "org.greenrobot:greendao:${DepsVersions.greendao_version}"

    //Network

    static glide = "com.github.bumptech.glide:glide:${DepsVersions.glide_version}"
    static glide_compiler = "com.github.bumptech.glide:compiler:${DepsVersions.glide_version}"

    static volley = "com.android.volley:volley:${DepsVersions.volley_version}"

    static okhttp = "com.squareup.okhttp3:okhttp:${DepsVersions.okhttp_version}"

    static retrofit = "com.squareup.retrofit2:retrofit:${DepsVersions.retrofit_version}"
    static retrofit_gson = "com.squareup.retrofit2:converter-gson:${DepsVersions.retrofit_version}"
    static retrofit_scalars = "com.squareup.retrofit2:converter-scalars:${DepsVersions.retrofit_version}"
    static retrofit_rxjava = "com.squareup.retrofit2:adapter-rxjava3:${DepsVersions.retrofit_version}"

    static nanohttpd = "org.nanohttpd:nanohttpd:${DepsVersions.nanohttpd_version}"

    static websocket = "org.java-websocket:Java-WebSocket:${DepsVersions.websocket_version}"

    static netty = "io.netty:netty-all:${DepsVersions.netty_version}"

    //Widget

    static background = "com.noober.background:core:${DepsVersions.background_version}"

    static brvah = "com.github.CymChad:BaseRecyclerViewAdapterHelper:${DepsVersions.brvah_version}"

    static badgeview = "q.rorbin:badgeview:${DepsVersions.badgeview_version}"

    static banner = "com.bigkoo:convenientbanner:${DepsVersions.banner_version}"

    static citypicker = "com.zaaach:citypicker:${DepsVersions.citypicker_version}"

    static countdown = "com.github.iwgang:countdownview:${DepsVersions.countdown_version}"

    static easyfloat = "com.github.princekin-f:EasyFloat:${DepsVersions.easyfloat_version}"

    static gsyplayer = "com.shuyu:GSYVideoPlayer:${DepsVersions.gsyplayer_version}"

    static tablayout = "com.flyco.tablayout:FlycoTabLayout_Lib:${DepsVersions.tablayout_version}"

    static matisse = "com.zhihu.android:matisse:${DepsVersions.matisse_version}"

    static photoview = "com.github.chrisbanes:PhotoView:${DepsVersions.photoview_version}"

    static pickerview = "com.contrarywind:Android-PickerView:${DepsVersions.pickerview_version}"

    static popwindow = "com.github.pinguo-zhouwei:CustomPopwindow:${DepsVersions.popwindow_version}"

    static refresh_layout = "com.scwang.smart:refresh-layout-kernel:${DepsVersions.refresh_version}"
    static refresh_classics_header = "com.scwang.smart:refresh-header-classics:${DepsVersions.refresh_version}"
    static refresh_classics_footer = "com.scwang.smart:refresh-footer-classics:${DepsVersions.refresh_version}"

    static swipelayout = "com.daimajia.swipelayout:library:${DepsVersions.swipelayout_version}"

    static swipetoload = "com.github.Aspsine:SwipeToLoadLayout:${DepsVersions.swipetoload_version}"

    static tangram = "com.alibaba.android:tangram:${DepsVersions.tangram_version}"
    static virtualview = "com.alibaba.android:virtualview:${DepsVersions.virtualview_version}"
    static vlayout = "com.alibaba.android:vlayout:${DepsVersions.vlayout_version}"
    static ultraviewpager = "com.alibaba.android:ultraviewpager:${DepsVersions.ultraviewpager_version}"

    static x5 = "com.tencent.tbs.tbssdk:sdk:${DepsVersions.x5_version}"

    static glance = "com.glance.guolindev:glance:${DepsVersions.glance_version}"

    static upgrade = "com.tencent.bugly:crashreport_upgrade:${DepsVersions.upgrade_version}"

    static wechat = "com.tencent.mm.opensdk:wechat-sdk-android-without-mta:${DepsVersions.wechat_version}"

    static jcore = "cn.jiguang.sdk:jcore:${DepsVersions.jcore_version}"
    static jverification = "cn.jiguang.sdk:jverification:${DepsVersions.jverification_version}"

    static imsdk = "com.tencent.imsdk:imsdk:${DepsVersions.imsdk_version}"
    static imsdk_tuikit = "com.tencent.imsdk:tuikit:${DepsVersions.imsdk_tuikit_version}"
}
